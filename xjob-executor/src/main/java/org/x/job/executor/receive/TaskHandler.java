package org.x.job.executor.receive;

import org.x.job.commons.job.Job;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    private static ThreadLocal<List<String>> machines = new ThreadLocal<>(); //all machines

    private static ThreadLocal<List<Job>> jobs = new ThreadLocal<>(); // all jobs

    public static ThreadLocal<List<String>> getMachines() {
        return machines;
    }

    public static void setMachines(ThreadLocal<List<String>> machines) {
        TaskHandler.machines = machines;
    }

    public static ThreadLocal<List<Job>> getJobs() {
        return jobs;
    }

    public static void setJobs(ThreadLocal<List<Job>> jobs) {
        TaskHandler.jobs = jobs;
    }
}
