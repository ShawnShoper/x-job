package org.x.job.util.zookeeper;


import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * zookeeper pools
 * 
 * @author ShawnShoper
 */
public class ZKPool
{
	static Logger logger = LogFactory.getLogger(ZKPool.class);
	private static ConcurrentMap<String, ConcurrentMap<String, ZKClient>> zkGroups = new ConcurrentHashMap<String, ConcurrentMap<String, ZKClient>>();
	private static final String DEFAULT = "default";

	public static ZKClient creatZkClient(String host, int port, int timeout)
	{
		return creatZkClient(null, host, port, timeout, null);
	}

	public static ZKClient creatZkClient(String name, String host, int port,
			int timeout)
	{
		return creatZkClient(name, host, port, timeout, null);
	}

	public static ZKClient creatZkClient(String name, String host, int port,
			int timeout, ZKWatcher watcher)
	{
		return creatZkClient(null, name, host, port, timeout, watcher);
	}

	public static ZKClient creatZkClient(String group, String name, String host,
			int port, int timeout, ZKWatcher watcher)
	{
		if (name == null)
		{
			throw new NullPointerException("zookeeper name can't be null.");
		}
		ZKClient zkClient = new ZKClient(host, port, timeout, watcher, name);
		addZKClient(group, name, zkClient);
		return zkClient;
	}

	/**
	 * 
	 * @param group
	 * @param name
	 * @param zkClient
	 */
	private static void addZKClient(String group, String name,
			ZKClient zkClient)
	{
		logger.info("Add zkclient to zk groups ,gourp is '{}', zk name '{}',",
				group, name);
		group = group == null ? DEFAULT : group;
		ConcurrentMap<String, ZKClient> groups = zkGroups.get(group);
		if (groups == null)
		{
			groups = new ConcurrentHashMap<String, ZKClient>();
		}
		if (groups.containsKey(name))
		{
			logger.warn(
					"zk pools have contains the zk name...override the old and close it....");
			close(name);
		}
		groups.put(name, zkClient);
		zkGroups.put(group, groups);
	}

	public static ZKClient getZookeeper(String group, String name)
	{
		ZKClient zkClient = null;
		group = group == null ? DEFAULT : group;
		if (zkGroups.containsKey(group))
		{
			zkClient = zkGroups.get(group).get(name);
		}
		return zkClient;
	}

	public static void close(String name)
	{
		close(null, name);
	}

	public static void close(String group, String name)
	{
		group = group == null ? DEFAULT : group;
		logger.info("Request to close group '{}' and name '{}' zookeeper.....",
				group, name);
		if (zkGroups.containsKey(group))
		{
			Map<String, ZKClient> zkMap = zkGroups.get(group);
			if (zkMap.containsKey(name))
			{
				ZKClient zkClient = zkMap.get(name);
				zkClient.close();
			}
		}
	}
}
