/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.panlingxiao.activemq.learning.openwire;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;

/**
 * 官方例子
 */
public class Publisher {

    public static void main(String []args) throws JMSException {

        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");

        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));

        String destination = arg(args, 0, "event");

        int messages = 10000;
        int size = 256;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for( int i=0; i < size; i ++) {
            body += DATA.charAt(i%DATA.length());
        }

        /*
         * 创建JMS规范中所定义的ConnectionFactory,通过ConnectionFactory来创建客户端与JMS 提供商之间的连接
         */
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);

        /*
         * 通过ConnectionFactory创建Connection
         * 在默认情况下，ActiveMQ并没有指定连接时需要用户名和密码来进行连接
         * 如果希望使用用户标识，在需要对ActiveMQ的安全机制进行配置
         */
        Connection connection = factory.createConnection();
        //Connection connection = factory.createConnection(user, password);

        //连接必须开启
        connection.start();

        /*
         * JMS的Session是建立在Connection之上的，需要通过Connection来创建Session，并且设置Session为确认机制
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /*
         * Destination为消息发送的目的地
         * 在JMS规范中，Destination有两个子接口，分别为Queue和Topic
         * 这里创建的消息发送目的地为Topic，并且指定了Topic的名字
         */
        Destination dest = new ActiveMQTopic(destination);

        // 通过Session创建指定目的地的消息生产者(Producer)，通过Producer来发送消息
        // 通过MessageProducer来创建和发送消息

        // 也可以通过创建指定目的地的消息消费者（Consumer),通过Consumer来接受和处理消息
        MessageProducer producer = session.createProducer(dest);


        /*
         * 设置消息生产者发送消息的发送模式，为非持久化方式
         * JMS Provider 会尽可能地将消息进行发送,但是它不会将消息进行存储
         */
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


        for( int i=1; i <= messages; i ++) {
            //通过Session来创建文本消息
            TextMessage msg = session.createTextMessage(body);
            msg.setIntProperty("id", i);
            //使用消息的发送者来发送消息
            producer.send(msg);
            if( (i % 1000) == 0) {
                System.out.println(String.format("Sent %d messages", i));
            }
        }

        producer.send(session.createTextMessage("SHUTDOWN"));
        connection.close();

    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

}