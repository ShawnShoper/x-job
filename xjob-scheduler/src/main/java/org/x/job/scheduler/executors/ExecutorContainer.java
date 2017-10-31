package org.x.job.scheduler.executors;

import java.util.*;

/**
 * Store executors.
 */
public class ExecutorContainer {
    private static Map<String, List<Executor>> executorsContainer = new HashMap<>();
    /**
     * put the executors
     * @param executors
     */
    public static void push(Executor... executors) {
        if (Objects.isNull(executors) || executors.length == 0) return;
        for (Executor executor : executors) {
            if (executorsContainer.containsKey(executor.getServiceId())) {
                Optional<Executor> first = executorsContainer.get(executor.getServiceId()).stream().filter(executor::equals).findFirst();
                if (!first.isPresent()) {
                    List<Executor> put = executorsContainer.get(executor.getServiceId());
                    put.add(executor);
                }
            }else{
                executorsContainer.put(executor.getServiceId(),Arrays.asList(executor));
            }
        }
    }

    /**
     * push all server list<br/>
     * and
     * Statistics changed server list<br/>
     * to
     * change the server status<br/>
     * then
     * modify the center store info data<br/>
     * @param executorInstances all server list
     */
    public static void push(Map<String, List<Executor>> executorInstances) {
        // Step1.Statistics of lost server lists
    }
}
