package org.x.job.scheduler.executors;

/**
 * Executor's status
 */
public enum Status {
    UP("up"), DOWN("down");
    private String name;

    Status(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
