package com.daqsoft.log.util.queue;


import com.daqsoft.log.core.serialize.Log;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by ShawnShoper on 2017/4/17.
 */
public class LogQueue {
    public final static LinkedTransferQueue<Log> logQueue = new LinkedTransferQueue();
}
