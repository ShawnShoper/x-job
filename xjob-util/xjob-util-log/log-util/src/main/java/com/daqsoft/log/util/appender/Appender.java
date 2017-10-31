package com.daqsoft.log.util.appender;

import com.daqsoft.log.core.serialize.Log;
import com.daqsoft.log.util.config.LogProperties;

import java.io.IOException;

/**
 * Created by ShawnShoper on 2017/4/19.
 * 输出接口
 */
public abstract class Appender {
    LogProperties logProperties;

    /**
     * 用于初始化
     */
    public abstract void init();

    public Appender(LogProperties logProperties) {
        this.logProperties = logProperties;
    }

    /**
     * 日志输出接口
     *
     * @param log
     * @throws IOException
     */
    public abstract void write(Log log) throws IOException;

    /**
     * Appender销毁接口
     */
    public abstract void destroy();

    /**
     * 确认是否可销毁
     */
    public abstract boolean canDestory();
}
