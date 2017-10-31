package com.daqsoft.log.util.config;

/**
 * 日志模板
 * %t   时间
 * %l   日志登记
 * %p   pid
 * %mn  方法名
 * %ln  代码所在行号
 * %cn  类名
 * %-5[yyyy-MM-dd HH:mm:ss.ssss]t %-5l %6p %30mn %5ln %5cn %5c
 */
public class LogPattern {
    private String name;
    //偏移量
    private int offset;
    private String pattern;
    //'-'或者''
    private char neg;

    public String getName() {
        return name;
    }

    public LogPattern(String name, int offset, String pattern, char neg) {
        this.name = name;
        this.offset = offset;
        this.pattern = pattern;
        this.neg = neg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public char getNeg() {
        return neg;
    }

    public void setNeg(char neg) {
        this.neg = neg;
    }
}