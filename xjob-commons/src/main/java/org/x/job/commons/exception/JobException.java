package org.x.job.commons.exception;

/**
 * 定义的一个X-job Exception父类<br/>
 * 该项目下其他需要单独剔出来的异常继承于他
 */
public class JobException extends Exception {
    private int code;

    public JobException(String message) {
        super(message);
    }

    public JobException(int code, String message) {
        super(message);
        this.code = code;
    }

    public JobException(String message, Throwable cause) {
        super(message, cause);
    }
}
