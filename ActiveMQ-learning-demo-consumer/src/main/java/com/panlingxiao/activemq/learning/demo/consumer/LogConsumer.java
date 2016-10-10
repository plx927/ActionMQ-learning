package com.panlingxiao.activemq.learning.demo.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import javax.jms.*;

/**
 * Created by panlingxiao on 2016/10/10.
 */
public class LogConsumer {

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.1.86:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic("logTopic");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new LogMessageListener());
        System.out.println("日志消费者启动成功");
    }


    public static class LogMessageListener implements MessageListener{
        @Override
        public void onMessage(Message message) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                LoggingEvent logginEvent = (LoggingEvent) objectMessage.getObject();
                Level level = logginEvent.getLevel();
                Object logMessage = logginEvent.getMessage();
                System.out.println(level+","+logMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }



}
