package org.shoper.log.util.appender;

import org.shoper.log.core.config.Constans;
import org.shoper.log.core.serialize.Log;
import org.shoper.log.util.config.LogPattern;
import org.shoper.log.util.config.LogProperties;
import org.shoper.log.util.constans.Tag;
import org.shoper.commons.core.DateUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ShawnShoper on 2017/4/19.
 * 控制台输出Appender
 */
public class ConsoleAppender extends Appender {
    PrintStream print = System.out;
    List<LogPattern> logPatterns;


    public ConsoleAppender(LogProperties logProperties, List<LogPattern> logPatterns) {
        super(logProperties);
        this.logPatterns = logPatterns;
    }

    @Override
    public void init() {

    }

    /**
     * 预准备输出语句
     *
     * @param log
     * @return
     */
    public Map<String, String> prepare(Log log) {
        Map<String, String> tag_value = new HashMap<>();
        if (!logPatterns.isEmpty())
            logPatterns.stream().forEach(e -> {
                String name = e.getName();
                String tmp = Constans.EMPTY;
                if (Tag.T.name.equals(name)) {
                    String time = DateUtil.dateToString(e.getPattern(), new Date(log.getTime()));
                    tmp = time;
                    tmp = format(tmp, e.getOffset(), e.getNeg());
//                    tmp = String.format(PERCENT + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset()) + "s", tmp);
                } else if (Tag.C.name.equals(name)) {
                    tmp = log.getBusiness().getContent();
                    tmp = format(tmp, e.getOffset(), e.getNeg());
//                    tmp = String.format(PERCENT + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset()) + "s", tmp);
                } else if (Tag.L.name.equals(name)) {
                    String tag_name = log.getBusiness().getLevel();
                    tmp = tag_name;
                    if (tmp.length() < 5) {
                        int length = tmp.length();
                        int i = Tag.L.offset - length;
                        String m = Constans.EMPTY;
                        for (int k = 0; k < i; k++)
                            m += Constans.SPACE;
                        tmp = m + tmp;
                    }
                    tmp = format(tmp, e.getOffset(), e.getNeg());
//                    tmp = String.format(PERCENT + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset()) + "s", tmp);
                } else if (Tag.P.name.equals(name)) {
                    tmp = String.valueOf(log.getPid());
                    tmp = format(tmp, e.getOffset(), e.getNeg());
//                    tmp = String.format(PERCENT + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset()) + "s", tmp);
                } else if (Tag.MN.name.equals(name)) {
                    //add line number block.
                    tmp = String.valueOf(log.getMethodName());
                    String ln = String.valueOf(log.getLineNumber());
                    if ((e.getOffset() - ln.length() - 1) < tmp.length()) {
                        tmp = tmp.substring(0, e.getOffset() - ln.length() - 3 - 1) + "...";
                    }
                    tmp += Constans.DASH + ln;
                    tmp = format(tmp, e.getOffset(), e.getNeg());
//                    tmp = String.format(PERCENT + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset()) + "s", tmp);
//                } else if (Tag.LN.name.equals(name)) {
                    //remove ln code block.
//                    tmp = String.format("%" + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset()) + "s", tmp);
                } else if (Tag.CN.name.equals(name)) {
                    tmp = String.valueOf(log.getClassName());
                    if (e.getOffset() < 0 || Math.abs(Tag.CN.offset) < tmp.length()) {
                        String[] split = tmp.split("\\.");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < split.length - 1; i++)
                            stringBuilder.append(split[i].charAt(1) + ".");
                        tmp = stringBuilder.toString() + split[split.length - 1];
//                        tmp = tmp.substring(0, tmp.length() + Math.abs(tag.getOffset()) - tmp.length());
                    }
                    tmp = format(tmp, e.getOffset(), e.getNeg());
//                    tmp = String.format(PERCENT + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset() + 11) + "s", tmp);
                }
                tag_value.put(Constans.PERCENT + (' ' == e.getNeg() ? "" : e.getNeg()) + (e.getOffset() == 0 ? "" : e.getOffset()) + (Objects.nonNull(e.getPattern()) ? "{" + e.getPattern() + "}" : "") + e.getName(), tmp);
            });
        return tag_value;
    }

    private String format(String origin, int offset, char neg) {
        return String.format(Constans.PERCENT + (' ' == neg ? "" : neg) + (offset == 0 ? "" : offset) + "s", origin);
    }

    private AtomicBoolean over = new AtomicBoolean(true);

    public void write(Log log) throws IOException {
        over.compareAndSet(true, false);
        try {
            Map<String, String> prepare = prepare(log);
            String partten = logProperties.getPartten();
            String out = partten;
            for (String k : prepare.keySet()) {
                String s = prepare.get(k);
                out = out.replace(k, s);
            }
            print.write(out.getBytes());
            print.write(Constans.NEWLINE.getBytes());
            print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            over.compareAndSet(false, true);
        }


    }

    @Override
    public boolean canDestory() {
        return over.get();
    }

    @Override
    public void destroy() {
        if (Objects.isNull(print))
            print.close();

    }
}