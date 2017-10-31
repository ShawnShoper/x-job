package org.x.job.util.zookeeper;

import org.apache.zookeeper.WatchedEvent;

/**
 * Empty abstract zookeeper watcher...Subclass inherit and need method to implement..
 * @author ShawnShoper
 *
 */
public class ZKWatcher{
	
	public void sessionExpired(){};
	public void childrenNodeChangeProcess(WatchedEvent event){};

	public void dataChangeProcess(WatchedEvent event){};

	public void nodeDeleteProcess(WatchedEvent event){};

	public void nodeCreateProcess(WatchedEvent event){};
}
