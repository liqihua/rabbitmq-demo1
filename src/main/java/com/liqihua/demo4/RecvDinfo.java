package com.liqihua.demo4;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 多个消费者平均分接消息
 * 手动答应消息
 */
public class RecvDinfo {
	//交换机名称
	private final static String EXCHANGE_NAME = "exB";
	//路由键值
	public static final String ROUTE_KEY_INFO = "route_key_info";
	public static final String ROUTE_KEY_WARN = "route_key_warn";
	public static final String ROUTE_KEY_ERROR = "route_key_error";


	public void toRecv() {
		try {
			/* 
	         * 打开连接、创建频道
	         */
	        ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost("119.23.130.114");
	        factory.setPort(9222);
	        factory.setUsername("name1");
	        factory.setPassword("123"); 
	        Connection connection = factory.newConnection();
	        final Channel channel = connection.createChannel();
	        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	        //声明转发器和类型
			//channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
			channel.exchangeDeclare(EXCHANGE_NAME,"direct");

			// 创建一个非持久的、唯一的且自动删除的队列
			String queueName = channel.queueDeclare().getQueue();
			// 为转发器指定队列，设置binding，并且指定接收某键值的路由消息
			//channel.queueBind(queueName, EXCHANGE_NAME, "");
			channel.queueBind(queueName, EXCHANGE_NAME, ROUTE_KEY_INFO);

	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	        /*
	         * 创建队列消费者  
	         */
	        Consumer consumer = new DefaultConsumer(channel) {
	    		@Override
	    		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	    			String message = new String(body, "UTF-8");
	    			System.out.println("--- RecvD info [x] Received '" + message + "'");
	    			System.out.println("--- RecvD info  [x] Done '");
	    			channel.basicAck(envelope.getDeliveryTag(), false);
	    		}
	    	};
	        channel.basicConsume(queueName, false, consumer);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

	
}
