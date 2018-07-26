package com.liqihua.demo3;

import com.liqihua.config.MQConfig;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 多个队列的消费者接收消息
 * 手动答应消息
 */
public class RecvC2 {
	//交换机名称
	private final static String EXCHANGE_NAME = "exA";


	public void toRecv() {
		try {
			/* 
	         * 打开连接、创建频道
	         */
	        ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(MQConfig.HOST);
			factory.setPort(MQConfig.PORT);
			factory.setUsername(MQConfig.USERNAME);
			factory.setPassword(MQConfig.PASSWORD);
	        Connection connection = factory.newConnection();
	        final Channel channel = connection.createChannel();
	        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	        //声明转发器和类型
			channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
			// 创建一个非持久的、唯一的且自动删除的队列
			String queueName = channel.queueDeclare().getQueue();
			// 为转发器指定队列，设置binding
			channel.queueBind(queueName, EXCHANGE_NAME, "");

	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");  
	        /*
	         * 创建队列消费者  
	         */
	        Consumer consumer = new DefaultConsumer(channel) {
	    		@Override
	    		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	    			String message = new String(body, "UTF-8");
	    			System.out.println("--- RecvC2 [x] Received '" + message + "'");
	    			System.out.println("--- RecvC2 [x] Done '");
	    			channel.basicAck(envelope.getDeliveryTag(), false);
	    		}
	    	};

	        channel.basicConsume(queueName, false, consumer);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

	
}
