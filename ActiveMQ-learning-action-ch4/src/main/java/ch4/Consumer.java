package ch4;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * SSL协议:
 * mvn exec:java
 * -Djavax.net.ssl.keyStore=${ACTIVEMQ_HOME}/conf/client.ks
 * -Djavax.net.ssl.keyStorePassword=password
 * -Djavax.net.ssl.trustStore=${ACTIVEMQ_HOME}/conf/client.ts
 * -Dexec.mainClass=org.apache.activemq.book.ch4.Consumer -Dexec.args="ssl://localhost:61617 CSCO ORCL"
 * <p/>
 *
 * <p/>
 * mvn exec:java
 * -Djavax.net.ssl.keyStore=E:\apache-activemq-5.11.1\conf\client.ts
 * -Djavax.net.ssl.keyStorePassword=password
 * -Djavax.net.ssl.trustStore=E:\apache-activemq-5.11.1\conf\client.ts
 * -Dexec.mainClass=org.apache.activemq.book.ch4.Publisher
 * -Dexec.args="ssl://localhost:61617 CSCO ORCL"
 *
 */
public class Consumer {

    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;

    public Consumer(String brokerURL) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws JMSException {
        if (args.length == 0) {
            System.err.println("Please define connection URI!");
            return;
        }

        //define connection URI
        Consumer consumer = new Consumer(args[0]);

        //extract topics from the rest of arguments
        String[] topics = new String[args.length - 1];
        System.arraycopy(args, 1, topics, 0, args.length - 1);
        for (String stock : topics) {
            Destination destination = consumer.getSession().createTopic("STOCKS." + stock);
            MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
            messageConsumer.setMessageListener(new Listener());
        }
    }

    public Session getSession() {
        return session;
    }

}
