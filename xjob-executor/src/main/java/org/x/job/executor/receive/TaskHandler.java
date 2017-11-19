package org.x.job.executor.receive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务参数托管
 * @author Eightmonth
 */
public class TaskHandler {

    private static List<String> machines = new ArrayList<>(); //all machines

    private static List<String> job = new ArrayList<>(); // jobID

    private static Map<String, Object> others = new HashMap<>(); // all others

    public static List<String> getMachines() {
        return machines;
    }

    public static void setMachines(List<String> machines) {
        TaskHandler.machines = machines;
    }

    public static List<String> getJob() {
        return job;
    }

    public static void setJob(List<String> job) {
        TaskHandler.job = job;
    }

    public static Map<String, Object> getOthers() {
        return others;
    }

    public static void setOthers(Map<String, Object> others) {
        TaskHandler.others = others;
    }
}
