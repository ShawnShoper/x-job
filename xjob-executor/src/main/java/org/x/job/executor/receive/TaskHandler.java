package org.x.job.executor.receive;

import java.util.List;
import java.util.Map;

/**
 * 任务参数托管
 * @author Eightmonth
 */
public class TaskHandler {

    private static ThreadLocal<List<String>> machines = new ThreadLocal<>(); //all machines

    private static ThreadLocal<List<List<String>>> masterJobs = new ThreadLocal<>(); // master undestribute

    private static ThreadLocal<List<String>> slaveJobs = new ThreadLocal<>(); // master destribute to slave

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

    public static ThreadLocal<List<List<String>>> getMasterJobs() {
        return masterJobs;
    }

    public static void setMasterJobs(ThreadLocal<List<List<String>>> masterJobs) {
        TaskHandler.masterJobs = masterJobs;
    }

    public static ThreadLocal<List<String>> getSlaveJobs() {
        return slaveJobs;
    }

    public static void setSlaveJobs(ThreadLocal<List<String>> slaveJobs) {
        TaskHandler.slaveJobs = slaveJobs;
    }
}
