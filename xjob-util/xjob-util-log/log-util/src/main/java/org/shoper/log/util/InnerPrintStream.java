package org.shoper.log.util;

import java.io.*;

public class InnerPrintStream extends PrintStream {
    private InnerOutPutStream out;

    public String getMessage() {
        return out.getMessage().toString();
    }

    public InnerPrintStream(OutputStream out) {
        super(out);
        this.out = (InnerOutPutStream) out;
    }

    public InnerPrintStream(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
        this.out = (InnerOutPutStream) out;
    }

    public InnerPrintStream(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
        this.out = (InnerOutPutStream) out;
    }

    public InnerPrintStream(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public InnerPrintStream(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public InnerPrintStream(File file) throws FileNotFoundException {
        super(file);
    }

    public InnerPrintStream(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    @Override
    public void close() {
        super.close();
    }
}
