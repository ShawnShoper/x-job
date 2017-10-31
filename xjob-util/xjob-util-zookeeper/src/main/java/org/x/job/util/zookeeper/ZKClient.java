package org.x.job.util.zookeeper;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.*;

/**
 * zookeeper client...
 *
 * @author ShawnShoper
 */
public class ZKClient {
	private static final Logger logger = LoggerFactory
			.getLogger(ZKClient.class);
	private final String COLON = ":";
	private ZooKeeper zooKeeper;
	private ZKWatcher zkWatcher;
	private String host;
	private int port;
	private int timeout;
	private ZKStatus status;
	private String monitorPath;
	private String name;

	/**
	 * inital a default client , host 127.0.0.1 port 2181 timeout 5s null
	 * watcher
	 */
	public ZKClient () {
		this("127.0.0.1", 2181, 5000, null, null);
	}

	public ZKClient (String host, int port, int timeout, ZKWatcher watcher) {
		this(host, port, timeout, watcher, null);
	}

	/**
	 * Create a new zk client
	 *
	 * @param host
	 * @param port
	 * @param timeout
	 * @param watcher
	 */
	public ZKClient (String host, int port, int timeout, ZKWatcher watcher,
					 String name) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.status = ZKStatus.Creating;
		this.name = name == null ? "default" : name;
		logger.info(
				"Prepare to create zookeeper connection,host ={},port ={},timeout ={},watcher ={}",
				host, port, timeout, watcher
		);
		this.zooKeeper = buildClient(host, port, timeout, watcher, name);
	}

	/**
	 * close client
	 *
	 * @throws InterruptedException
	 */
	public void close () {
		if (this.zooKeeper != null) {
			logger.info("zookeeper session closing....");
			this.status = ZKStatus.Closing;
			try {
				this.zooKeeper.close();
				executorService.shutdownNow();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				this.status = ZKStatus.Closed;
			}
		}
	}

	/**
	 * Default connection monitor watcher...
	 *
	 * @author ShawnShoper
	 */
	public class DefaultWatcher implements Watcher {
		@Override
		public void process (WatchedEvent event) {
			// 检查下 zkClient 的状态，如果是关闭断开状态，那么就不执行任何 watch
			if (status == ZKStatus.Disconnected)
				return;
			try {
				// 再次注入监听...避免监听失效....
				if (event.getPath() != null) {
					zooKeeper.exists(event.getPath(), true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (zkWatcher != null) {
				if (KeeperState.Expired == event.getState()) {
					if (logger.isDebugEnabled())
						logger.debug("Watcher process session expired...");
					status = ZKStatus.Expired;
					zkWatcher.sessionExpired();
				} else if (EventType.NodeChildrenChanged == event.getType()) {
					if (logger.isDebugEnabled())
						logger.debug(
								"Watcher process childrenNodeChange...path is {}",
								event.getPath()
						);
					zkWatcher.childrenNodeChangeProcess(event);
				} else if (EventType.NodeCreated == event.getType()) {
					if (logger.isDebugEnabled())
						logger.debug(
								"Watcher process nodeCreate...path is {}",
								event.getPath()
						);
					zkWatcher.nodeCreateProcess(event);
				} else if (EventType.NodeDataChanged == event.getType()) {
					if (logger.isDebugEnabled())
						logger.debug(
								"Watcher process dataChange...path is {}",
								event.getPath()
						);
					zkWatcher.dataChangeProcess(event);
				} else if (EventType.NodeDeleted == event.getType()) {
					if (logger.isDebugEnabled())
						logger.debug(
								"Watcher process nodeDelete...path is {}",
								event.getPath()
						);
					zkWatcher.nodeDeleteProcess(event);
				}
			}
		}
	}

	// fail connect count....
	private int failCount = 0;
	ExecutorService executorService = Executors.newCachedThreadPool();
	/**
	 * create new zookeeper connection.....
	 *
	 * @param host
	 * @param port
	 * @param timeout
	 * @param watcher
	 * @return
	 */
	private ZooKeeper buildClient (String host, int port, int timeout,
								   ZKWatcher watcher, String name) {
		ZooKeeper zk = null;

		for (; ; ) {
			try {
				this.zkWatcher = watcher;
				this.name = name;
				zk = new ZooKeeper(host + this.COLON + port, timeout,
								   new DefaultWatcher()
				);
				// zk实例创建好以后，并非正真连接成功，所以需要判断当前zk的状态...
				CheckZookeperStates checkZookeperStates = new CheckZookeperStates(
						zk);
				Future<Boolean> future = executorService
						.submit(checkZookeperStates);
				Boolean isConnected = future.get(
						timeout,
						TimeUnit.MILLISECONDS
				);
				if (isConnected) {
					this.status = ZKStatus.Connected;
					logger.info(
							"Create zookeeper connection successful....Target host is {}",
							host
					);
					break;
				}
			} catch (Throwable t) {
				logger.error("Create a zookeeper connection fail,cause by "
									 + t.getMessage());
				t.printStackTrace();
			} finally {

			}
			logger.info("Retry connect...{} times.... ", ++failCount);
			try {
				logger.info("Sleep 1s for next try to connect..");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.info("Thread run error,try recursive call self...");
			}
		}
		// reset the fail count...
		failCount = 0;
		return zk;
	}

	/**
	 * 检查Zookeeper连接是否成功..
	 *
	 * @author ShawnShoper
	 */
	class CheckZookeperStates implements Callable<Boolean> {
		private ZooKeeper zk;

		public CheckZookeperStates (ZooKeeper zk) {
			this.zk = zk;
		}

		@Override
		public Boolean call () throws Exception {
			if (zk != null) {
				while (!States.CONNECTED.equals(zk.getState())) {
					TimeUnit.MILLISECONDS.sleep(10);
				}
			}
			return true;
		}

	}

	/**
	 * Empty byte arrays
	 */
	public final static byte[] EMPTY_NODE_DATA = new byte[0];

	/**
	 * create a no data node
	 *
	 * @param path
	 * @param createMode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean createNode (String path, CreateMode createMode)
			throws KeeperException, InterruptedException {
		logger.info(
				"Creating a new node with no data,node path is \"{}\",create mode is {}",
				path, createMode.name()
		);
		return createNode(path, "", createMode);
	}

	/**
	 * create a no data node
	 *
	 * @param path
	 * @param createMode
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean createNode (String path, String data, CreateMode createMode)
			throws KeeperException, InterruptedException {
		if (status != ZKStatus.Connected)
			return false;
		if (exists(path)) {
			return false;
		} else {
			String intact = "";
			StringTokenizer tokenizer = new StringTokenizer(path, "/");
			while (tokenizer.hasMoreTokens()) {
				intact += "/" + tokenizer.nextToken();
				if (!exists(intact)) {
					if(tokenizer.hasMoreTokens()) {
						zooKeeper.create(intact, "".getBytes(),
								Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT
						);
					}else{
						zooKeeper.create(intact, data.getBytes(),
								Ids.OPEN_ACL_UNSAFE, createMode
						);
					}
				}
			}
			return true;
		}
	}

	/**
	 * edit data for node
	 *
	 * @param path
	 * @param data
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void editData (String path, String data)
			throws KeeperException, InterruptedException {
		monitorPath = path;
		zooKeeper.setData(path, data.getBytes(),
						  getNodeVersion(path).getVersion()
		);
	}

	public void editData (String path, byte[] data)
			throws KeeperException, InterruptedException {
		monitorPath = path;
		zooKeeper.setData(path, data, getNodeVersion(path).getVersion());

	}

	/**
	 * show node's data
	 *
	 * @param nodePath
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public byte[] showData (String nodePath)
			throws KeeperException, InterruptedException {
		monitorPath = nodePath;
		return zooKeeper.getData(nodePath, true, getNodeVersion(nodePath));
	}

	public Stat getNodeVersion (String path)
			throws KeeperException, InterruptedException {
		return zooKeeper.exists(path, true);
	}

	/**
	 * check the path is exists
	 *
	 * @param path
	 * @return a boolean for is exists
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public boolean exists (String path)
			throws KeeperException, InterruptedException {
		if (status != ZKStatus.Connected)
			return false;
		monitorPath = path;
		Stat stat = zooKeeper.exists(path, true);
		return null != stat;
	}

	/**
	 * Get children list...
	 *
	 * @param path
	 * @param watcher
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public List<String> getChildren (String path, Watcher watcher)
			throws KeeperException, InterruptedException {
		monitorPath = path;
		return zooKeeper.getChildren(path, watcher);
	}

	/**
	 * @param path
	 * @param isWatcher
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public List<String> getChildren (String path, boolean isWatcher)
			throws KeeperException, InterruptedException {
		monitorPath = path;
		return zooKeeper.getChildren(path, isWatcher);
	}

	/**
	 * Delete node by path
	 *
	 * @param path
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void deleteNode (String path)
			throws InterruptedException, KeeperException {
		monitorPath = path;
		zooKeeper.delete(path, getNodeVersion(path).getVersion());
	}

	@Override
	public String toString () {
		return "ZKClient [host=" + host + ", port=" + port + ", timeout="
				+ timeout + ", status=" + status + ", monitorPath="
				+ monitorPath + ", name=" + name + "]";
	}

}
