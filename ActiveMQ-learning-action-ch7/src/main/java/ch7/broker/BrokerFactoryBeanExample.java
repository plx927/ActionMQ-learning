package ch7.broker;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * Created by panlingxiao on 2016/7/27.
 */
public class BrokerFactoryBeanExample {

    public static void main(String[] args) {
        System.setProperty("activemq.base", System.getProperty("user.dir"));
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("activemq-simple2.xml");
    }
}
