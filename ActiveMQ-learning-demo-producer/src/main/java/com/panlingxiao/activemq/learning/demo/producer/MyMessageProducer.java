package com.panlingxiao.activemq.learning.demo.producer;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by panlingxiao on 2016/7/13.
 */
public class MyMessageProducer {

    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Object connectionFactory = initialContext.lookup("jndi.properties");

    }
}
