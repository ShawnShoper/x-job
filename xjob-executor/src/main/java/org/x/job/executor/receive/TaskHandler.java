package org.x.job.executor.receive;

import java.util.List;
import java.util.Map;

/**
 * 任务参数托管
 * @author Eightmonth
 */
public class TaskHandler {

    private static ThreadLocal<List<String>> machines = new ThreadLocal<>(); //all machines

    private static ThreadLocal<List<String>> job = new ThreadLocal<>(); // jobID

    private static ThreadLocal<Map<String, Object>> others = new ThreadLocal<>(); // all others

    public static ThreadLocal<Map<String, Object>> getOthers() {
        return others;
    }

    public static void setOthers(ThreadLocal<Map<String, Object>> others) {
        TaskHandler.others = others;
    }

    public static ThreadLocal<List<String>> getMachines() {
        return machines;
    }

    public static void setMachines(ThreadLocal<List<String>> machines) {
        TaskHandler.machines = machines;
    }

    public static ThreadLocal<List<String>> getJob() {
        return job;
    }

    public static void setJob(ThreadLocal<List<String>> job) {
        TaskHandler.job = job;
    }
}
