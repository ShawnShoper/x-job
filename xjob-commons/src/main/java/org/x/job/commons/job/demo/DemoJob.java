package org.x.job.commons.job.demo;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.x.job.commons.job.model.BaseJob;
import org.x.job.commons.job.model.JobParam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 样例任务种类
 * @author Eightmonth
 */
public class DemoJob extends BaseJob{

    private int i;

    private String s;

    public DemoJob(int i) {
        this.i = i;
    }

    public DemoJob(String s) { this.s = s;}

    @Override
    public void afterProperties(JobParam param) throws Exception {
        super.param = param;
    }

    @Override
    public Object doJob() {
        System.out.println("job"+s);
        return null;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(10);
        ZkClient zkClient = new ZkClient("127.0.0.1");
        System.out.println("connect success");
        String path = "/te";
        String callback = null;
        for(int i=0; i < 10; i++){
            try {
                es.execute(new ZkThread(zkClient, path, callback, i));
                if(callback != null)
                    System.out.println("path : "+ callback);
            } catch (Exception e){
                System.out.println(i+" is loser !!");
            }
        }
        System.out.println("wait...");
        Thread.sleep(Long.MAX_VALUE);
    }

    static class ZkThread implements Runnable{
        private ZkClient zkClient;
        private String path;
        private String callback;
        private int no;
        public ZkThread(ZkClient zkClient, String path, String callback, int no){
            this.zkClient = zkClient;
            this.path = path;
            this.callback = callback;
            this.no = no;
        }
        @Override
        public void run() {
            callback = zkClient.create(path,"1", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(no + " is winner! path : " + callback);
        }
    }
}
