package com.daqsoft.log.util;

import com.daqsoft.commons.core.StringUtil;
import com.daqsoft.log.core.config.Constans;

import java.io.IOException;
import java.io.OutputStream;

public class InnerOutPutStream extends OutputStream {
    public StringBuilder message = new StringBuilder();

    public StringBuilder getMessage() {
        return message;
    }

    @Override
    public void write(int b) throws IOException {
        throw new RuntimeException("This method not support now");
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        String m = new String(b, off, len);
        if (StringUtil.nonEmpty(m))
            message.append(m + Constans.NEWLINE);
    }
}