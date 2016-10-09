package ch14.jmx;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;


/**
 * 通过JMX API 获取Broker的信息
 * 运行前先启动activemq start xbean=.../activemq-jmx.xml
 * 指定启动的配置文件
 */
public class Stats {

	public static void main(String[] args) throws Exception {


		JMXServiceURL url = new JMXServiceURL(
				"service:jmx:rmi:///jndi/rmi://localhost:2011/jmxrmi");
		//建立与Broker的MBean Server的连接，这里使用的url地址为ActiveMQ中启动日志输出的url
		JMXConnector connector = JMXConnectorFactory.connect(url, null);
		connector.connect();

		MBeanServerConnection connection = connector.getMBeanServerConnection();

		/*
		 * 通过连接获取MBean所代表的Broker
		 * MBean通过名字来引用，其格式如下:
		 * <jmx domain name>:brokerName=<name of the broker>,type=Broker
		 */
		ObjectName name = new ObjectName("my-broker:type=Broker,brokerName=localhost");

		BrokerViewMBean mbean = (BrokerViewMBean) MBeanServerInvocationHandler
				.newProxyInstance(connection, name, BrokerViewMBean.class, true);

		System.out.println("Statistics for broker " + mbean.getBrokerId() + " - " + mbean.getBrokerName());
		System.out.println("\n-----------------\n");
		//获取总的消息数量
		System.out.println("Total message count: " + mbean.getTotalMessageCount() + "\n");
		//获取总的消费者数量
		System.out.println("Total number of consumers: " + mbean.getTotalConsumerCount());
		//查询队列的数量
		System.out.println("Total number of Queues: " + mbean.getQueues().length);

		ObjectName[] topics = mbean.getTopics();
		System.out.println("Total number of Topic: "+topics.length);


		System.out.println("----------------队列信息-------------");
		for (ObjectName queueName : mbean.getQueues()) {
			System.out.println(queueName);
			QueueViewMBean queueMbean = (QueueViewMBean) MBeanServerInvocationHandler
					.newProxyInstance(connection, queueName,
							QueueViewMBean.class, true);
			System.out.println("\n-----------------\n");
			System.out.println("Statistics for queue " + queueMbean.getName());
			System.out.println("Size: " + queueMbean.getQueueSize());
			System.out.println("Number of consumers: " + queueMbean.getConsumerCount());
		}


		System.out.println("----------------主题信息-------------");
		for(ObjectName topicName : topics){
			System.out.println(topicName);
			TopicViewMBean topicViewMBean = MBeanServerInvocationHandler.newProxyInstance(connection, topicName, TopicViewMBean.class, true);
			System.out.println("Statistics for topic " + topicViewMBean.getName());
			System.out.println("Size: " + topicViewMBean.getMaxMessageSize());
			System.out.println("\n-----------------\n");
		}
	}

}
