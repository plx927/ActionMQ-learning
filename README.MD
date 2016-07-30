# ActiveMQ-learning
个人对ActiveMQ消息中间件学习总结

## ActiveMQ-learning-guide-example 为官方的example的例子
在${ActiveMQ_HOME}/examples/openwire/java


## JMS体系结构
JMS 1.1 规范统一了两套不同的API，使用一套API来处理两个不同的消息域。

#### JMS Client
由传Java语言所编写的应用，用于发送和接受消息。

####  JMS Producer
创建并且发送消息的JMS消息的客户端应用。JMS使用MessageProducer来发送消息到目的地。

####  JMS Consumer
接受和处理JMS消息的客户端应用。JMS 客户端使用MessageConsumer从目的地读取消息进行消费。
MessageConsumer可以通过receive方法来进行同步消费，也可以通过提供一个MessageListener的实现来实现异步消费。
一旦消费被发送的目的地，那么MessageListener.onMessage()方法就会得到调用。
对于MessageConsumer，没有提供方法来设置Destination，因此只有在创建MessageConsumer的时候就直接指定。
Session.createMessageConsumer(destination);

#### JMS Provider
实现JMS接口的消息中间件，完全由Java语言所编写。
#### JMS Message
JMS消息是JMS规范中最为重要的概念，它就是被JMS客户端所发送和接收的对象。

#### JMS Domain
JMS的消息机制规范借鉴了已经存在的消息机制的实现，它定义了两种消息机制(在JMS规范中，也被称为domain)；这两种发送消息的方式,分别为点对点(Point-to-Point)和发布订阅(Publish/Subscribe)

##### POINT-TO-POINT DOMAIN
P2P消息域使用队列作为消息的目的地。通过队列，消息可以被同步/异步发送或者接受。每一个在队列中被接受的消息只能被发送一次，并且只能被发送给一个消费者。
P2P消息传送机制与通过邮件服务器发送私人邮件非常类似。消费者可以通过同步的方式从队列中获取消息(通过MessageConsumer.receive()完成)，或者通过注册一
个MessageListener实现类以异步的方式从队列中接受消息。**队列会存储所有的消息直到它被消息者消费或者消息过期。**
多个消费者可以注册到同一个队列中，但是队列中的每一条消息只能被一个消费者，具体取决于消息者对消息的确认机制。
JMS的提供者确保了消息只会被发送一次，并且只有一个消费着可以消费到消息。就这一点而言，JMS提供者将消费分散以轮询的方式将消息发送给多个消费者。

##### PUBLISH/SUBSCRIBE DOMAIN
PS消息域使用Topic作为消息的目的地。发布者发布消息到Topic，订阅者通过向Topic中进行注册，从Topic获取消息。任何发送到Topic中的消息都会自动发送给所有订阅者。
订阅者和消息之间是多对一的关系。与P2P类似的地方在于，订阅者也可以通过同步(MessageConsumer.receive())或者异步地获取消息(通过MessageListener)。
与P2P不同的是，队列会存储消息，而Topic在默认情况下不会存储消息，如果希望让Topic存储消息，那么则需要使用持久化订阅。如果使用持久化订阅，当订阅者与JMS Provider断开连接，
那么JMS Provider将会负责为订阅者存储消息，一旦重新建立连接，持久化订阅者将会从JMS Provider接受到未过期的消息。持久化订阅允许订阅者在断开连接的情况下不丢失消息。

> MESSAGE DURABILITY VS MESSAGE PERSISTENCE
在JMS中，message durability 和 message persistence 是非常容易混淆的两个概念。尽管它们看起来非常相似，但是它们在语义还是存在不同，并且它么各自都自己的特定的目的。
Message Durability 可以通过 publish/subscribe 的方式来实现。当客户端连接上 JMS Provider，它可以使用持久化订阅或者非持久化订阅。

*Durable subscription : 当订阅者对Topic使用持久化订阅，其告诉 JMS Provider 当订阅者与 JMS Provider 断开连接时，JMS Provider 应该去保存订阅状态。  
如果持久化订阅者与JMS Provider断开连接时，JMS Provider 将会存储所有的消息直到订阅者再一次与其建立连接或者订阅者显示地取消对主题的订阅。*

*Nondurable subscription : 当订阅者对Topic使用非持久订阅，其告诉 JMS Provider 当订阅者与 JMS Provider 断开连接时，无需保存订阅状态。
当客户端与 JMS Provider 断开连接，JMS Provider 在断开连接的这段时间不会为其保存任何消息*
 
Message persistence 是一个与消息域无关的概念。Message persistence是一个服务质量属性，它被用于标识当JMS Provider发生宕机时，JMS 应用能否处理丢失的消息。这个属性
的指定是通过消息消费者的setDeliveryMode方法来进行设置。参考javax.jms.DeliveryMode。

#### Administered Object
Administered Object 包含了特定的 JMS 提供者的配置信息，并且其应该被 JMS 管理者所创建。**Administrator Object 被用于JMS客户端。**
它的作用是是向客户端屏蔽掉特定的 JMS 提供者的详细信息，并且提取了 JMS 提供者的管理任务。通常情况下，对于 Administrator Object 的查找是通过
JNDI的方式来完成，但是这并不是强制要求的。在 JMS 规范中定义了两种类型的 Administrator Object:ConnectionFactory 和 Destination。

##### ConnectionFactory
JMS 客户端使用 ConnectionFactory 来创建与 JMS Provider 之间的连接。这个连接通常是客户端与 JMS Provider 之间的一个TCP连接，因此对于连接的创建，开销是巨大的。
对于实现者来说，应该尽可能使用连接池来对连接进行管理。JMS 中的 Connection 与 JDBC中的 Connection 非常类似；JMS 客户端通过 Connection 来创建 javax.jms.Session对象
来完成与 JMS Provider 之间的交互。

##### Destination

Topic/Queue



## 5.JMS 消息持久化
JMS规范支持两种类型的消息发送:持久化和非持久化。当一个消息以持久化的方式发送时，那么 JMS Provider 必须将其记录存储，对于非持久化的方式，JMS Provider 必须尽可能地完成消息的发送，但是它不会去将消息进行记录存储。
ActiveMQ支持对于这两种类型的消息发送都支持，并且可以配置来支持消息恢复。

持久化消息使用的场景:被用于当你发送完消息到Broker之后，即使在这段时间内Consumer没有被运行，但是在之后依旧可以被消费。一旦消费者消费了消息并且对其进行了确认，那么Broker就会将消息进行删除。
非持久化消息使用的场景:被用于发送通知或者是实时数据。当我们的应用对于性能的要求比较高并且可以允许消息的丢失的情况下，我们应该选择非持久化的消息发送。

### 5.1 消息是如何在ActiveMQ中存储的


### 5.2 KahaDB 消息存储

#### 5.2.1 KahaDB存储内部结构


#### 5.2.2 KahaDB 存储目录结构
*db log files*:存储具体的消息
*db.data*:存储消息的索引信息
*db.redo*:用于恢复BTree索引
*archive directory*:当开启归档属性时才生效。

##### 5.2.3 配置KahaDB消息存储




### 5.6 在Broker中缓存消息提供给Consumer消费
尽管消息持久化的一个最重要目的是为了让消息能够存活的时间更长一些；除此之外，有很多情况下我们希望在当Consumer与Broker断开连接的情况下，消息依然可以最终被Consumer所消费。
实时地发送价格信息到一个交易平台是一个使用MQ很好的应用场景，但是这些实时数据只有在很短的一段时间内才有意义，如果过了这段时间后，其就失去意义。因此在这样的场景下去对消息进行持久化是没有意义的。

#### 5.6.1 如何将消息缓存提供给Consumer工作


















A copy of each message on a given sdestination is delivered to all topic subscribers using the pub/sub domain.





#### JMS message internals



##### 1.ConnectionFactory

##### 2.Connection

##### 3.Session

##### 4.Destination

##### 5.Message 
