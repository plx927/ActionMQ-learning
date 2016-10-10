package com.panlingxiao.activemq.learning.demo.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.NamingException;

/**
 * Created by panlingxiao on 2016/7/13.
 */
public class MyMessageProducer {

    public static final String URL = ActiveMQConnectionFactory.DEFAULT_BROKER_BIND_URL;
    public static void main(String[] args) throws NamingException, JMSException {
//        InitialContext initialContext = new InitialContext();
//        Object connectionFactory = initialContext.lookup("jndi.properties");

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.1.86:61616");

        Connection connection = connectionFactory.createConnection();

        connection.start();
        Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);

        //创建队列
        Queue queue = session.createQueue("test-queue");
        MessageProducer producer = session.createProducer(queue);

        for(int i = 0;i < 3;i++){
            TextMessage textMessage = session.createTextMessage("hello :" + i);
            producer.send(textMessage);
        }
        /*
         * 如果Session设置了事务，
         */
        //session.commit();
        System.out.println("发送消息成功");
        session.close();
        connection.close();
    }
}
