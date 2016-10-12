package com.panlingxiao.activemq.learning.demo.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by panlingxiao on 2016/10/11.
 */
public class MyTopicProducer {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "password", "tcp://172.16.1.86:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageProducer producer = session.createProducer(null);

        try {
            for (int i = 0; i < 100000; i++) {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setInt("id", i);
                mapMessage.setString("text", "hello:" + i);
                producer.send(topic, mapMessage);
                System.out.printf("发送消息[id:%d,text:%s]到主题\n", mapMessage.getInt("id"), mapMessage.getString("text"));
//                if(i > 0 && (i % 100000) == 0){
//                    session.commit();
//                }
            }
           // session.commit();

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            producer.close();
            session.close();
            connection.close();
        }
    }
}

