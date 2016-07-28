package ch7.broker;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * Created by panlingxiao on 2016/7/27.
 * 通过读取Spring配置文件创建ActiveMQ的Broker
 */
public class SpringConfig {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("pure-spring.xml");
    }
}
