package org.x.job.scheduler.executors;

import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import org.x.job.scheduler.ExecutorsContainer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Store executors.
 */
public class ExecutorContainerHandler {
    private static Logger LOGGER = LogFactory.getLogger(ExecutorsContainer.class);

    private static ExecutorsContainer executorsContainer = new ExecutorsContainer();

    /**
     * push executors
     *
     * @param executors
     */
    public static void push(Executor... executors) {
        if (Objects.isNull(executors)) return;
        if (LOGGER.isInfoEnable())
            LOGGER.info("Received executor,size is %s", executors.length);
        if (Objects.isNull(executors) || executors.length == 0) return;
        for (Executor executor : executors) {
            if (executorsContainer.containsKey(executor.getServiceId())) {
                Optional<Executor> first = executorsContainer.get(executor.getServiceId()).stream().filter(executor::equals).findFirst();
                if (!first.isPresent()) {
                    List<Executor> put = executorsContainer.get(executor.getServiceId());
                    put.add(executor);
                }
            } else {
                executorsContainer.put(executor.getServiceId(), Arrays.asList(executor));
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
     *
     * @param executorInstances all server list
     */
    public static void push(Map<String, List<Executor>> executorInstances) {
        if (Objects.isNull(executorInstances)) return;


        // Step1.Statistics of lost server lists
        List<Executor> oldExecutors = new ArrayList<>();
        executorsContainer.values().forEach(oldExecutors::addAll);
        List<Executor> newExecutor = new ArrayList<>();
        executorInstances.values().forEach(newExecutor::addAll);
        //  fetch exists executors.
        List<Executor> existExecutors = oldExecutors.stream().filter(newExecutor::contains).collect(Collectors.toList());

        existExecutors.stream().forEach(e -> push(e, true));
        //  fetch non-exists executors and revert status which in the old container
        //  there is disconnect or be shutdown executors
        oldExecutors.stream().filter(e -> !newExecutor.contains(e)).forEach(e -> push(e, false));

        newExecutor.stream().filter(e -> !oldExecutors.contains(e)).forEach(e -> push(e, true));
    }

    /**
     * push discovery executor.if flag is false,set status to DOWN
     * else UP
     *
     * @param executor
     * @param flag
     */
    private static void push(Executor executor, boolean flag) {
        //check container has exist the executor's serviceId of the key
        if (!executorsContainer.containsKey(executor.getServiceId()))
            executorsContainer.put(executor.getServiceId(), new ArrayList<>());
        else {
            List<Executor> executors = executorsContainer.get(executor.getServiceId());
            Optional<Executor> first = executors.stream().filter(executor::equals).findFirst();
            //if flag is false,set the status is DOWN
            if (!flag) {
                if (first.isPresent()) {
                    Executor exe = first.get();
                    if (exe.getStatus() == Status.DOWN)
                        exe.setStatus(Status.DOWN);
                }
            } else {
                //else if executor has been not exist,to add to container else set status is UP
//                if (Objects.isNull(executors)) executors = new ArrayList<>();
                if (!first.isPresent()) executors.add(executor);
                else first.get().setStatus(Status.UP);
            }
        }
    }
}
