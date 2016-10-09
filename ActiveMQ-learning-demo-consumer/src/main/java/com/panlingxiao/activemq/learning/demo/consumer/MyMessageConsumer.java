package com.panlingxiao.activemq.learning.demo.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by panlingxiao on 2016/10/9.
 */
public class MyMessageConsumer {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("test-queue");
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("消费者收到了消息:"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
