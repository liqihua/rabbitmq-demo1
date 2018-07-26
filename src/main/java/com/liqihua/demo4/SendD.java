package com.liqihua.demo4;

import com.liqihua.config.MQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 发布/订阅，附带路由键值：一个消息发送到多个队列：一对多（一个消息只能被多个接收者消费）
 */
public class SendD {
	//交换机名称
	private static final String EXCHANGE_NAME = "exB";
	//路由键值
	public static final String ROUTE_KEY_INFO = "route_key_info";
	public static final String ROUTE_KEY_WARN = "route_key_warn";
	public static final String ROUTE_KEY_ERROR = "route_key_error";


	
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
			//声明转发器和类型，direct：处理路由键，队列路由完全匹配
			//channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			channel.exchangeDeclare(EXCHANGE_NAME, "direct");
			System.out.println(" [x] Sent message ... ");
			// 往转发器上发送消息，并且附带路由键值（相当于消息类型）
			//channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_INFO, null, ("i am info msg "+System.currentTimeMillis()).getBytes());
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_INFO, null, ("i am info msg "+System.currentTimeMillis()).getBytes());
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_INFO, null, ("i am info msg "+System.currentTimeMillis()).getBytes());
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_INFO, null, ("i am info msg "+System.currentTimeMillis()).getBytes());

			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_WARN, null, ("i am warn msg "+System.currentTimeMillis()).getBytes());
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_WARN, null, ("i am warn msg "+System.currentTimeMillis()).getBytes());
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_WARN, null, ("i am warn msg "+System.currentTimeMillis()).getBytes());

			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_ERROR, null, ("i am error msg "+System.currentTimeMillis()).getBytes());
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_ERROR, null, ("i am error msg "+System.currentTimeMillis()).getBytes());

			System.out.println(" [x] Sent finished .");
			//关闭频道和连接
	        channel.close();
	        connection.close();
    	}catch(Exception e) {
			e.printStackTrace();
		}
    }
	
	
	
}
