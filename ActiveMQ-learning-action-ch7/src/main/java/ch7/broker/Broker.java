package ch7.broker;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用Java代码配置Broker实例,默认情况下都是通过配置ActiveMQ.xml来对Broker进行配置。
 * 下面的代码等价于
 * <pre>
 *     <broker xmlns="http://activemq.apache.org/schema/core" brokerName="myBroker" dataDirectory="${activemq.base}/data">
 *          <transportConnectors>
 *              <transportConnector name="openwire" uri="tcp://localhost:61616" />
 *          </transportConnectors>
 *
 *       <plugins>
 *          <simpleAuthenticationPlugin>
 *              <users>
 *                  <authenticationUser username="admin" password="password" groups="admins,publishers,consumers"/>
 *                  <authenticationUser username="publisher" password="password" groups="publishers,consumers"/>
 *                  <authenticationUser username="consumer" password="password" groups="consumers"/>
 *                  <authenticationUser username="guest" password="password" groups="guests"/>
 *              </users>
 *          </simpleAuthenticationPlugin>
 *      </plugins>
 *
 *      </broker>
 * </pre>
 * 运行官方例子的案例:
 * mvn exec:java -Dexec.mainClass=org.apache.activemq.book.ch7.broker.Broker -Dlog4j.configuration=file:src/main/java/log4j.properties
 */
public class Broker {

    public static void main(String[] args) throws Exception {
        /*
         * BrokerService用于管理ActiveMQ中的Broker对象的整个生命周期
         * 其有一组Transport Connector 和 network Connector 组成
         */
        BrokerService broker = new BrokerService();
        broker.setBrokerName("myBroker");
        broker.setDataDirectory("data/");

        SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();

        List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
        users.add(new AuthenticationUser("admin", "password", "admins,publishers,consumers"));
        users.add(new AuthenticationUser("publisher", "password", "publishers,consumers"));
        users.add(new AuthenticationUser("consumer", "password", "consumers"));
        users.add(new AuthenticationUser("guest", "password", "guests"));
        authentication.setUsers(users);

        //添加认证的插件，必须在添加 Transport Connector 之前添加 Plugin。
        broker.setPlugins(new BrokerPlugin[]{authentication});

		/*JaasAuthenticationPlugin jaas = new JaasAuthenticationPlugin();
        jaas.setConfiguration("src/main/resources/org/apache/activemq/book/ch5/login.config");
		broker.setPlugins(new BrokerPlugin[]{jaas});*/

        //添加 Transport Connector
        broker.addConnector("tcp://localhost:61616");


        //设置其是否启用对JMX的支持
        broker.setUseJmx(true);

        //修改Broker在监控中的名字
        ManagementContext managementContext = new ManagementContext();
        managementContext.setBrokerName("my-borker");
        managementContext.setConnectorPort(2011);
        broker.setManagementContext(managementContext);



        broker.start();

        System.out.println();
        System.out.println("Press any key to stop the broker");
        System.out.println();

        System.in.read();
    }

}
