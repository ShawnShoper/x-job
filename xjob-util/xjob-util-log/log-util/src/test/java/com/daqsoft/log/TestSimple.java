package com.daqsoft.log;

import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import org.shoper.log.util.annotation.LogModel;
import org.shoper.log.util.config.LogProperties;

import java.util.concurrent.TimeUnit;

/**
 * Created by ShawnShoper on 2017/4/17.
 */
@LogModel("测试class")
public class TestSimple {
    private static Logger logger = LogFactory.getLogger(TestSimple.class);

    // static org.slf4j.Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {
//      logger.info("asd");
        LogProperties logProperties = new LogProperties();
        logProperties.setApplication("测试");
        logProperties.setHost("127.0.0.1");
        logProperties.setPort(8900);
        logProperties.setPartten("%-5{yyyy-MM-dd HH:mm:ss.ssss}t %-5l %6p %30mn %5ln %5cn %5c");
        LogFactory.setLogConfig(logProperties);
//      logger.info("测试t111e");
        new TestSimple().te();
    }

    @LogModel("测试method")
    public void te() {
        logger.info("测试te");
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
