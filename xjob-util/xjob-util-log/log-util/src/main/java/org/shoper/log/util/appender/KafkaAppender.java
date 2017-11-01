package org.shoper.log.util.appender;

import org.shoper.log.core.serialize.Log;
import org.shoper.log.util.config.LogProperties;

import java.io.IOException;

public class KafkaAppender extends Appender  {
    public KafkaAppender(LogProperties logProperties) {
        super(logProperties);
    }

    @Override
    public void init() {

    }

    @Override
    public void write(Log log) throws IOException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean canDestory() {
        return false;
    }
}
