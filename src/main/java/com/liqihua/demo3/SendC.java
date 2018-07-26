package com.liqihua.demo3;

import com.liqihua.config.MQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * fanout（分发）交换机：一个消息发送到多个队列
 */
public class SendC {
	//交换机名称
	private final static String EXCHANGE_NAME = "exA";
	
    public static void send() {
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
	        Channel channel = connection.createChannel();
	        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			//声明转发器和类型，fanout：不处理路由键，像广播
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			String message = "message - abc "+System.currentTimeMillis();
			// 往转发器上发送消息
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			//关闭频道和连接
	        channel.close();
	        connection.close();
    	}catch(Exception e) {
			e.printStackTrace();
		}
    }
	
	
	
}
