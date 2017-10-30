package org.x.job.commons.job.Exception;

import org.x.job.commons.exception.JobException;

/**
 * Job启动类型异常抛出该异常
 * @author Eightmonth
 */
public class StartupException extends JobException {
    static final long serialVersionUID = -1;

    public StartupException(String message) {
        super(message);
    }

    public StartupException(int code, String message) {
        super(code,message);
    }

    public StartupException(String message, Throwable cause) {
        super(message, cause);
    }

}
