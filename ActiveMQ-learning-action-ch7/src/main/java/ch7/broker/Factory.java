package ch7.broker;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

/**
 * <pre>
 * In many applications, you’ll want to be able to initialize the broker
 * using the same configuration files used to configure standalone instances of the ActiveMQ broker.
 * For that purpose ActiveMQ provides the utility org.apache.activemq.broker.BrokerFactory class.
 * </pre>
 *
 *
 *
 */
public class Factory {

    public static void main(String[] args) throws Exception {
        System.setProperty("activemq.base", System.getProperty("user.dir"));

        /*
         * BrokerFactory通过使用ActiveMQ的URI来创建ActiveMQ的Broker
         * BrokerFactory通过根据URI来选择合适的BrokerFactoryHandler，从而完成BrokerService的创建
         * 使用最多的BrokerFactoryHandler是XBeanBrokerFactory,我们只需通过配置对应的URI即可。
         */
        String str = "xbean:classpath:activemq-simple.xml";


        //URI scheme for simple broker configuration performed completely via the configuration URI.
        //str = "broker:(tcp://localhost:61616)";

        BrokerService broker = BrokerFactory.createBroker(new URI(str));
        broker.start();

        System.out.println();
        System.out.println("Press any key to stop the broker");
        System.out.println();

        System.in.read();
    }

}
