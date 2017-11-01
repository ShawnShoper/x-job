package org.shoper.log.util.queue;


import org.shoper.log.core.serialize.Log;

import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by ShawnShoper on 2017/4/17.
 */
public class LogQueue {
    public final static LinkedTransferQueue<Log> logQueue = new LinkedTransferQueue();
}
