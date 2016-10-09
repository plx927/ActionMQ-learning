package com.panlingxiao.activemq.learning.demo.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.NamingException;

/**
 * Created by panlingxiao on 2016/7/13.
 */
public class MyMessageProducer {

    public static void main(String[] args) throws NamingException, JMSException {
//        InitialContext initialContext = new InitialContext();
//        Object connectionFactory = initialContext.lookup("jndi.properties");

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_BIND_URL);

        Connection connection = connectionFactory.createConnection();

        connection.start();

        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //创建队列
        Queue queue = session.createQueue("test-queue");
        MessageProducer producer = session.createProducer(queue);

        for(int i = 0;i < 100;i++){
            TextMessage textMessage = session.createTextMessage("hello :" + i);
            producer.send(textMessage);
        }
        System.out.println("发送消息成功");
        session.close();
        connection.close();
    }
}
