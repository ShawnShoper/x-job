package org.shoper.log.util;


import org.shoper.log.core.config.Target;
import org.shoper.log.util.appender.Appender;
import org.shoper.log.util.appender.ConsoleAppender;
import org.shoper.log.util.appender.FileAppender;
import org.shoper.log.util.appender.KafkaAppender;
import org.shoper.log.util.config.LogPattern;
import org.shoper.log.util.config.LogProperties;
import org.shoper.log.util.constans.Tag;
import org.ho.yaml.Yaml;
import org.shoper.commons.core.StringUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ShawnShoper on 2017/4/17.
 * 日志工厂
 */
public class LogFactory {
    //日志配置
    private static LogProperties logProperties;
    private static LogProcessor logProcessor;
    public final static String PERCENT = "%";

    public static LogProperties getLogProperties() {
        return logProperties;
    }

    private static void init() {
        try {
            LogFactory.logProperties = Yaml.loadType(LogProperties.class.getResourceAsStream("/log.yml"), LogProperties.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        if (Objects.isNull(logProperties)) {
//            logProperties.setApplication(logProperties.getApplication());
//            logProperties.setHost(logProperties.getHost());
//            logProperties.setPort(logProperties.getPort());
//            logProperties.setPartten("%-23{yyyy-MM-dd HH:mm:ss.sss}t%-5l%6p%30mn%-5ln%cn%c");
//        }
        List<LogPattern> logPatterns = new ArrayList<>();
        String partten = LogFactory.logProperties.getPartten();
        String[] log_partten = partten.split(PERCENT);
        Pattern reg_pattern = Pattern.compile("(-)?(\\d*?)(\\{.*?\\})?([a-z]+)", Pattern.CASE_INSENSITIVE);
        //遍历pattern
        Arrays.stream(log_partten).map(String::trim).filter(StringUtil::nonEmpty).map(e -> {
            Matcher matcher = reg_pattern.matcher(e);
            if (matcher.find()) {
                String neg = matcher.group(1);
                if (Objects.nonNull(neg) && !"-".equals(neg))
                    throw new RuntimeException(String.format("Log express neg %s not support", neg));
                String offset = matcher.group(2);
                String pattern = matcher.group(3);
                if (Objects.nonNull(pattern)) {
                    pattern = pattern.substring(1, pattern.length() - 1);
                }
                String name = matcher.group(4);
                if (Objects.isNull(name))
                    throw new RuntimeException(String.format("Log express tag %s not support", name));
                Tag tag;
                try {
                    tag = Tag.valueOf(name.toUpperCase());
                } catch (IllegalArgumentException e1) {
                    throw new RuntimeException(String.format("Log express tag %s not support", name));
                }
                return new LogPattern(tag.getName(), offset.isEmpty() ? 0 : Integer.valueOf(offset), pattern, (Objects.isNull(neg) ? ' ' : '-'));
            } else {
                return null;
            }
        }).forEach(logPatterns::add);
        List<Appender> appenders = new ArrayList<>(logProperties.getTargets().length);
        for (Target target : logProperties.getTargets()) {
            if (target == Target.File)
                appenders.add(new FileAppender(logProperties, logPatterns));
            else if (target == Target.Sout)
                appenders.add(new ConsoleAppender(logProperties, logPatterns));
            else if(target == Target.Kafka)
                appenders.add(new KafkaAppender(logProperties));
            else
                throw new RuntimeException("Target " + target + " not support yet");

        }
        logProcessor = new LogProcessor(appenders);
        //程序结束时,避免还有余量的任务未持久化.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logProcessor.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    public static void setLogConfig(LogProperties logProperties) {
        LogFactory.logProperties = logProperties;
    }


    public static Logger getLogger(Class<?> clazz) {
        if (Objects.isNull(logProperties))
            synchronized (LogProperties.class) {
                if (Objects.isNull(logProcessor))
                    init();
            }
        return new Logger(clazz, logProcessor,logProperties);
    }

    private LogFactory() {

    }
}
