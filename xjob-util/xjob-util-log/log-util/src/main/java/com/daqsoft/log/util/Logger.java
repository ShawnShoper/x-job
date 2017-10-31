package com.daqsoft.log.util;

import com.daqsoft.log.core.config.Constans;
import com.daqsoft.log.util.config.LogProperties;
import com.daqsoft.log.util.constans.LogLevel;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Objects;

public class Logger {
    private Class<?> clazz;
    private LogProcessor logProcessor;

    public Logger(Class<?> clazz, final LogProcessor logProcessor, LogProperties logProperties) {
        this.clazz = clazz;
        this.logProcessor = logProcessor;
        this.logProperties = logProperties;
    }

    private LogProperties logProperties;

    @PreDestroy
    public void destroy() {
    }

    public void info(String channel, LogInfo logMsg) {
        log(channel, logMsg, Constans.INFO);
    }

    public LogInfo format(String formatter, Object... obj) {
        LogInfo logInfo = new LogInfo();
        if (Objects.nonNull(obj) && obj.length > 0) {
            Object[] ts = null;
            if (obj[obj.length - 1] instanceof Throwable) {
                ts = Arrays.copyOf(obj, obj.length - 1, Object[].class);
                logInfo.setThrowable((Throwable) obj[obj.length - 1]);
            }
            logInfo.setMsg(String.format(formatter, Objects.nonNull(ts) ? ts : obj));
        } else {
            logInfo.setMsg(formatter);
        }

        return logInfo;
    }

    public void info(String formatter, Object... obj) {
        info(null, format(formatter, obj));
    }

    public void info(String channel, String formatter, Object... obj) {
        info(channel, format(formatter, obj));
    }

    public void warn(LogInfo logMsg) {
        log(null, logMsg, Constans.WARN);
    }

    public void warn(String formatter, Object... obj) {
        warn(format(formatter, obj));
    }

    private void debug(LogInfo logMsg) {
        log(null, logMsg, Constans.DEBUG);
    }

    public void debug(String formatter, Object... obj) {
        debug(format(formatter, obj));
    }

    public void error(LogInfo logMsg) {
        log(null, logMsg, Constans.ERROR);
    }

    public void error(String formatter, Object... obj) {
        error(format(formatter, obj));
    }

    public boolean isDebugEnable() {
        return isLevelEnable(LogLevel.Debug);
    }

    public boolean isInfoEnable() {
        return isLevelEnable(LogLevel.Info);
    }

    public boolean isWarnEnable() {
        return isLevelEnable(LogLevel.Warn);
    }

    public boolean isErrorEnable() {
        return isLevelEnable(LogLevel.Error);
    }

    public boolean isFatalEnable() {
        return isLevelEnable(LogLevel.Fatal);
    }

    /**
     * 判断当然日志级别是否被允许输出
     *
     * @param level
     * @return
     */
    private final boolean isLevelEnable(LogLevel level) {
        return level.compare(logProperties.getLogLevel()) >= 0;
    }

    /**
     * @author ShawnShoper
     * @date 2016/12/20 0020 11:27
     * LogUtil构造，构造中开启线程进行维护
     */
    public Logger(LogProperties logProperties) {
        this.logProperties = logProperties;
    }

    /**
     * @author ShawnShoper
     * @date 2016/12/20 0020 16:35
     * 记录日志
     */
    public void log(String channel, LogInfo logMsg, String logLevel) {
        logProcessor.processor(channel, logMsg, logLevel, clazz);
    }

}