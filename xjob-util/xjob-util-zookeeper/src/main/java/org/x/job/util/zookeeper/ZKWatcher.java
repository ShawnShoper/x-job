package org.x.job.util.zookeeper;

import org.apache.zookeeper.WatchedEvent;

/**
 * Empty abstract zookeeper watcher...Subclass inherit and need method to implement..
 *
 * @author ShawnShoper
 */
public class ZKWatcher {

    public void sessionExpired() throws Exception {
    }


    public void childrenNodeChangeProcess(WatchedEvent event) throws Exception {
    }


    public void dataChangeProcess(WatchedEvent event) throws Exception {
    }


    public void nodeDeleteProcess(WatchedEvent event) throws Exception {
    }


    public void nodeCreateProcess(WatchedEvent event) throws Exception {
    }

}
