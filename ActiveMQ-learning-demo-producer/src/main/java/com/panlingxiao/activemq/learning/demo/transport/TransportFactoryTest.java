package com.panlingxiao.activemq.learning.demo.transport;

import org.apache.activemq.transport.TransportFactory;

import java.net.URI;

/**
 * Created by panlingxiao on 2016/10/11.
 * 测试Activemq中的TransportFactory
 */
public class TransportFactoryTest {

    public static void main(String[] args) throws Exception{
        URI location = new URI("tcp://172.16.1.86:61616");
        System.out.println(location.getScheme());
        TransportFactory.connect(location);
    }
}
