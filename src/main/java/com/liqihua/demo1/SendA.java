package com.liqihua.demo1;

import com.liqihua.config.MQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送到指定队列：点对点（一个消息只能被一个接收者消费一次）
 */
public class SendA {
	//队列名称  
    private final static String QUEUE_NAME = "queueA";
	
	
	public static void main(String[] args) throws IOException, TimeoutException {
		/* 
         * 打开连接、创建频道
         */  
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MQConfig.HOST);
        factory.setPort(MQConfig.PORT);
        factory.setUsername(MQConfig.USERNAME);
        factory.setPassword(MQConfig.PASSWORD);
        Connection connection = factory.newConnection();  
        Channel channel = connection.createChannel();  
        /*
         * 指定一个队列
         * queueName：队列名
         * durable：持久化，指的是队列持久化到数据库中。在之前的博文中也说过，如果RabbitMQ服务挂了怎么办，队列丢失了自然是不希望发生的。持久化设置为true的话，即使服务崩溃也不会丢失队列
         * exclusive：设置了排外为true的队列只可以在本次的连接中被访问，也就是说在当前连接创建多少个channel访问都没有关系，但是如果是一个新的连接来访问，对不起，不可以，下面是我尝试访问了一个排外的queue报的错。还有一个需要说一下的是，排外的queue在当前连接被断开的时候会自动消失（清除）无论是否设置了持久化
         * autoDelete：是否自动删除。也就是说queue会清理自己。但是是在最后一个connection断开的时候
         * arguments：其他参数
         * channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //发送的消息  
        String message = "hello world! "+System.currentTimeMillis();  
        /*
         * 往队列中发出一条消息
         * exchange：指定交换机，""表示使用默认交换机
         * routingKey：路由键，#匹配0个或多个单词，*匹配一个单词，在topic exchange做消息转发用
         * mandatory：true：如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，那么会调用basic.return方法将消息返还给生产者。false：出现上述情形broker会直接将消息扔掉
		 * immediate：true：如果exchange在将消息route到queue(s)时发现对应的queue上没有消费者，那么这条消息不会放入队列中。当与消息routeKey关联的所有queue(一个或多个)都没有消费者时，该消息会通过basic.return方法返还给生产者。
         * props：需要注意的是BasicProperties.deliveryMode，0:不持久化 1：持久化 这里指的是消息的持久化，配合channel(durable=true),queue(durable)可以实现，即使服务器宕机，消息仍然保留
         * body：消息字节
         * channel.basicPublish(exchange, routingKey, boolean, immediate, props, body);
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");  
        //关闭频道和连接  
        channel.close();  
        connection.close();

	}
	
	
	
}
