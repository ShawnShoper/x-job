package org.xjob.scheduler.task.Exception;

/**
 * Job启动类型异常抛出该异常
 * @author Eightmonth
 */
public class StartupException extends RuntimeException {
    static final long serialVersionUID = -1;

    public StartupException() {
        super();
    }

    public StartupException(String message) {
        super(message);
    }

    public StartupException(String message, Throwable cause) {
        super(message, cause);
    }

    public StartupException(Throwable cause) {
        super(cause);
    }

    protected StartupException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
