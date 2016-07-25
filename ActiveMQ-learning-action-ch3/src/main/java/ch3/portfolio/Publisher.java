package ch3.portfolio;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;

/**
 * JMS In Action 案例:模拟一个股票交易，实时获取股票动态信息
 * mvn exec:java -Dexec.mainClass=org.apache.activemq.book.ch3.portfolio.Producer  -Dexec.args="CSCO ORCL"
 */
public class Publisher {
	
    protected int MAX_DELTA_PERCENT = 1;
    protected Map<String, Double> LAST_PRICES = new Hashtable<String, Double>();
    protected static int count = 10;
    protected static int total;
    
    protected static String brokerURL = "tcp://localhost:61616";
    protected static transient ConnectionFactory factory;
    protected transient Connection connection;
    protected transient Session session;
    protected transient MessageProducer producer;
    
    public Publisher() throws JMSException {
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	connection = factory.createConnection();
    	try {
        connection.start();
    	} catch (JMSException jmse) {
    		connection.close();
    		throw jmse;
    	}
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(null);
    }

    /**
     * 关闭客户端与JMS Provider之间的TCP连接
     * @throws JMSException
     */
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }
    
    public static void main(String[] args) throws JMSException {
        Publisher publisher = new Publisher();

        while (total < 1000) {
            for (int i = 0; i < count; i++) {
                publisher.sendMessage(args);
            }
            total += count;
            System.out.println("Published '" + count + "' of '" + total + "' price messages");
            try {
              Thread.sleep(1000);
            } catch (InterruptedException x) {
            }
        }
        publisher.close();
    }


    /**
     * 发送消息的处理流程
     * @param stocks
     * @throws JMSException
     */
    protected void sendMessage(String[] stocks) throws JMSException {
        int idx = 0;

        while (true) {
            idx = (int)Math.round(stocks.length * Math.random());
            if (idx < stocks.length) {
                break;
            }
        }

        String stock = stocks[idx];
        //将股票的名字作为Topic的名字
        Destination destination = session.createTopic("STOCKS." + stock);
        //创建JMS消息
        Message message = createStockMessage(stock, session);

        System.out.println("Sending: " + ((ActiveMQMapMessage)message).getContentMap() + " on destination: " + destination);
        producer.send(destination, message);
    }


    /**
     * 创建JMS消息
     * @param stock
     * @param session
     * @return
     * @throws JMSException
     */
    protected Message createStockMessage(String stock, Session session) throws JMSException {
        //获取当前股票的最新价格
        Double value = LAST_PRICES.get(stock);
        //模拟出股票的价格
        if (value == null) {
            value = new Double(Math.random() * 100);
        }

        // lets mutate the value by some percentage
        double oldPrice = value.doubleValue();
        value = new Double(mutatePrice(oldPrice));
        LAST_PRICES.put(stock, value);
        double price = value.doubleValue();

        double offer = price * 1.001;

        boolean up = (price > oldPrice);

		MapMessage message = session.createMapMessage();
		message.setString("stock", stock);
		message.setDouble("price", price);
		message.setDouble("offer", offer);
		message.setBoolean("up", up);
		return message;
    }

    protected double mutatePrice(double price) {
        double percentChange = (2 * Math.random() * MAX_DELTA_PERCENT) - MAX_DELTA_PERCENT;

        return price * (100 + percentChange) / 100;
    }

}
