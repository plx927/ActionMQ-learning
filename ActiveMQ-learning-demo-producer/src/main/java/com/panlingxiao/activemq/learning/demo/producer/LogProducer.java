package com.panlingxiao.activemq.learning.demo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * Created by panlingxiao on 2016/10/10.
 */
public class LogProducer {

    private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);

    public static void main(String[] args) throws JMSException, InterruptedException {
        long start = System.currentTimeMillis();
        for(int i = 0 ;i < 10000;i++) {
            logger.info("Hello World：{},测试日志", i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        Thread.sleep(1000);
        System.exit(0);
    }


}
