package com.liqihua.demo1;

import com.liqihua.config.MQConfig;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RecvA {
	//队列名称  
    private final static String QUEUE_NAME = "queueA";
	
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
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
         * exclusive：是否为当前连接的专用队列，在连接断开后，会自动删除该队列
         * autoDelete：是否自动删除。也就是说queue会清理自己。但是是在最后一个connection断开的时候
         * arguments：其他参数
         * channel.queueDeclare(queueName, durable, exclusive, autoDelete, arguments);
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");  
        /*
         * 创建队列消费者  
         */
        Consumer consumer = new DefaultConsumer(channel) {
    		@Override
    		public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
    			String message = new String(body, "UTF-8");
    			System.out.println(" [x] Received '" + message + "'");
    		}
    	};
        /*
         * 指定消费队列
         * queue：队列名称
         * autoAck：为了确保消息一定被消费者处理，rabbitMQ提供了消息确认功能，就是在消费者处理完任务之后，就给服务器一个回馈，服务器就会将该消息删除，如果消费者超时不回馈，那么服务器将就将该消息重新发送给其他消费者，默认开启
         * 	开启：消费者收到消息后不等待处理立刻自动应答消息
         *  关闭：消费者收到消息后不会自动应答消息，需要代码调用用channel.ack()、channel.nack()、channel.basicReject()等函数才会进行消息应答
         * channel.basicConsume​(String queue, boolean autoAck, Consumer callback)
         */
        channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}
