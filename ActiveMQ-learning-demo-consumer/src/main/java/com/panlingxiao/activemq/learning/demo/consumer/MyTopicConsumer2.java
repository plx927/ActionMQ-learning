package com.panlingxiao.activemq.learning.demo.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by panlingxiao on 2016/10/11.
 */
public class MyTopicConsumer2 {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("consumer","password","tcp://172.16.1.86:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination destination = session.createTopic("test-topic");
        MessageConsumer consumer = session.createConsumer(destination);
        while (true){
            MapMessage mapMessage = (MapMessage) consumer.receive();
            System.out.println(String.format("消费者2收到消费:id=%d,text=%s",mapMessage.getInt("id"),mapMessage.getString("text")));
        }
    }
}
