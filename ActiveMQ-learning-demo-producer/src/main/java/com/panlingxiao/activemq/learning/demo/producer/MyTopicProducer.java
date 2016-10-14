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
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageProducer producer = session.createProducer(null);
        long start = System.currentTimeMillis();
        long total = 100000000;
        System.out.println("DeliverMode:"+producer.getDeliveryMode());
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        try {
            for (int i = 0; i < total; i++) {
                //默认的消息为持久化消息，该条消息就会被MQ所存储
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setInt("id", i);
                mapMessage.setString("text", "hello:" + i);
                producer.send(topic, mapMessage);
                if(i > 0 && (i % 100) == 0){
                    session.commit();
                    System.out.println("提交事务成功，已发送发送："+(i)+"条消息");
                }
            }
            session.commit();
            long end = System.currentTimeMillis();
            System.out.println("发送"+total+"条消息，耗时"+(end - start)/1000+"秒");

        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            producer.close();
            session.close();
            connection.close();
        }
    }
}

