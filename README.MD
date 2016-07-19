# ActiveMQ-learning
个人对ActiveMQ消息中间件学习总结

### ActiveMQ-learning-guide-example 为官方的example的例子
在${ActiveMQ_HOME}/examples/openwire/java


## JMS体系结构
JMS 1.1 规范统一了两套不同的API，使用一套API来处理两个不同的消息域。

### JMS Client
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
JMS的对于消息机制规范的定义借鉴了已经存在的消息机制的实现，它定了两种消息机制(在JMS规范中，也被称为domain)。
两种发送消息的方式,分别为点对点(Point-to-Point)和发布订阅(Publish/Subscribe)

##### POINT-TO-POINT DOMAIN
P2P消息域使用队列作为消息的目的地。

##### PUBLISH/SUBSCRIBE DOMAIN


A copy of each message on a given sdestination is delivered to all topic subscribers using the pub/sub domain.


#### Administered Object

#### Destination

Topic/Queue


#### JMS message internals



##### 1.ConnectionFactory

##### 2.Connection

##### 3.Session

##### 4.Destination

##### 5.Message 
