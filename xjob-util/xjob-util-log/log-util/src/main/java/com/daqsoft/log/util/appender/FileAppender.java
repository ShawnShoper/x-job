package com.daqsoft.log.util.appender;

import com.daqsoft.commons.core.DateUtil;
import com.daqsoft.commons.core.StringUtil;
import com.daqsoft.log.core.config.Constans;
import com.daqsoft.log.core.serialize.Business;
import com.daqsoft.log.core.serialize.Log;
import com.daqsoft.log.util.config.FileProperties;
import com.daqsoft.log.util.config.LogPattern;
import com.daqsoft.log.util.config.LogProperties;
import com.daqsoft.log.util.constans.FileCap;
import scala.collection.immutable.Stream;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ShawnShoper on 2017/4/20.
 * 输出日志到File
 */
public class FileAppender extends Appender {
    //文件配置
    FileProperties fileProperties;
    //输出流
    private OutputStream outputStream;
    //时间切割
    String rollingPattern;
    //日志文件名
    String fileName;
    //日志存放路径
    String fileDir;
    int rolling;
    int fileSize;
    int maxFileSize;
    int segmentCount;
    List<LogPattern> logPatterns;

    public FileAppender(final LogProperties logProperties, final List<LogPattern> logPatterns) {
        super(logProperties);
        this.fileProperties = logProperties.getFileProperties();
        this.logPatterns = logPatterns;
    }

    @Override
    public void init() {
        fileName = fileProperties.getFileName();
        fileDir = this.fileProperties.getFileDir();
        File file = new File(fileDir);
        if (!file.exists()) if (!file.mkdirs())
            throw new RuntimeException("Can not create dir ['" + fileDir + "'] ,maybe your current user no permission or has being used");
        if (Objects.nonNull(fileProperties.getRolling())) {
            rollingPattern = fileProperties.getRolling().getPattern();
        }
        if (StringUtil.nonEmpty(fileProperties.getFileSize())) {
            //解析file size
            final String sizeReg = "(\\d+)\\s?(MB|KB|GB)";
            Pattern pattern = Pattern.compile(sizeReg);
            Matcher matcher = pattern.matcher(fileProperties.getFileSize());
            if (matcher.find()) {
                int cap = Integer.valueOf(matcher.group(1));
                String unit = matcher.group(2);
                FileCap fileUnit = FileCap.valueOf(unit);
                maxFileSize = cap * fileUnit.size;
            }
        }
//        String fileName = fileProperties.getFileDir() + File.separator + fileProperties.getFileName();
    }


    /**
     * 检查输出流是否需要重定向
     *
     * @return 是否重定向
     */
    private synchronized boolean plantOutputStream(int size) throws FileNotFoundException {
        boolean change = false;
        String pattern = DateUtil.timeToString(fileProperties.getRolling().getPattern(), System.currentTimeMillis());
        if (StringUtil.nonEmpty(rollingPattern)) {
            //去除'-'占位符,计算文件有效期
            int nowRolling = Integer.valueOf(pattern.replace("-", ""));
            if (nowRolling > rolling) {
                rolling = nowRolling;
                change = true;
                segmentCount = 0;
            }
        }
        if (Objects.nonNull(outputStream))
            //文件长度将超过或达到设置上限.分割日志文件
            if (maxFileSize > 0 && ((fileSize + size) > maxFileSize)) {
                change = true;
                segmentCount = 0;
            }

        if (change) {
            try {
                if (Objects.nonNull(outputStream)) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fileSize = size;
                File file;
                do {
                    ++segmentCount;
                    file = new File(fileDir + File.separator + fileName + (StringUtil.nonEmpty(String.valueOf(pattern)) ? "-" + pattern : "") + (segmentCount > 0 ? "-" + segmentCount : ""));
                } while (file.exists());
                outputStream = new FileOutputStream(file, true);

            }
        } else {
            fileSize += size;
        }
        return change;
    }

    private AtomicBoolean over = new AtomicBoolean(true);

    @Override
    public boolean canDestory() {
        return over.get();
    }

    @Override
    public void write(Log log) throws IOException {
        over.compareAndSet(true, false);
        try {
            byte[] data = parseLog(log, logProperties).getBytes();
            plantOutputStream(data.length);
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            over.compareAndSet(false, true);
        }
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(outputStream))
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 把对应的log实体序列化为相应的字符串
     *
     * @param log
     * @return
     */
    protected static String parseLog(Log log, LogProperties logProperties) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constans.MAJORVERSION).append(Constans.PLACEHOLDER)
                .append(log.getTime()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getApplication()) ? Constans.EMPTY : log.getApplication()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getClassName()) ? Constans.EMPTY : log.getClassName()).append(Constans.PLACEHOLDER)
                .append(log.getLineNumber()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getMethodName()) ? Constans.EMPTY : log.getMethodName()).append(Constans.PLACEHOLDER)
                .append(log.getContentType()).append(Constans.PLACEHOLDER)
                .append(log.getPid()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(logProperties.getHost()) ? Constans.EMPTY : logProperties.getHost()).append(Constans.PLACEHOLDER)
                .append(logProperties.getPort()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getChannel()) ? Constans.EMPTY : log.getChannel()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getBusiness().getVia()) ? Constans.EMPTY : log.getBusiness().getVia()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getBusiness().getLevel()) ? Constans.EMPTY : log.getBusiness().getLevel()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getBusiness().getModel()) ? Constans.EMPTY : log.getBusiness().getModel()).append(Constans.PLACEHOLDER)
                .append(Objects.isNull(log.getBusiness().getContent()) ? Constans.EMPTY : log.getBusiness().getContent().replace(Constans.NEWLINE, Constans.PLACEHOLDER2));
        return stringBuilder.toString() + Constans.NEWLINE;
    }

    /**
     * 解析log字符串,生成对应的Log实体
     *
     * @param logStr
     * @return
     */
    public static Log UnParseLog(String logStr) {
        String[] logs = logStr.split(Constans.PLACEHOLDER);
        Log log = new Log();
        if (Constans.MAJORVERSION.equals(logs[0])) {
            log.setTime(Objects.nonNull(logs[1]) ? Long.valueOf(logs[1]) : 0);
            log.setApplication(StringUtil.isEmpty(logs[2]) ? null : logs[2]);
            log.setClassName(StringUtil.isEmpty(logs[3]) ? null : logs[3]);
            log.setLineNumber(Objects.nonNull(logs[4]) ? Integer.valueOf(logs[4]) : 0);
            log.setMethodName(StringUtil.isEmpty(logs[5]) ? null : logs[5]);
            log.setContentType(StringUtil.isEmpty(logs[6]) ? null : logs[6]);
            log.setPid(StringUtil.isEmpty(logs[7]) ? 0 : Integer.valueOf(logs[7]));
            log.setHost(StringUtil.isEmpty(logs[8]) ? null : logs[8]);
            log.setPort(Integer.valueOf(logs[9]));
            log.setChannel(StringUtil.isEmpty(logs[10]) ? null : logs[10]);
            Business business = new Business();
            business.setVia(StringUtil.isEmpty(logs[11]) ? null : logs[11]);
            business.setLevel(StringUtil.isEmpty(logs[12]) ? null : logs[12]);
            business.setModel(StringUtil.isEmpty(logs[13]) ? null : logs[13]);
            String content = StringUtil.isEmpty(logs[14]) ? null : logs[14];
            business.setContent(content.replace(Constans.PLACEHOLDER2, Constans.NEWLINE));
            log.setBusiness(business);
        }
        return log;
    }
}
