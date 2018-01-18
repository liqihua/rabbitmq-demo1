package com.liqihua.demo2;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 多个消费者平均分接消息
 * 手动答应消息
 */
public class RecvB {
	//队列名称  
    private final static String QUEUE_NAME = "queueA";
    
    private String label = null;
    
    
    
	
	public RecvB(String label) {
		super();
		this.label = label;
	}



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
	    		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	    			String message = new String(body, "UTF-8");
	    			System.out.println(label+" [x] Received '" + message + "'");
					doWork(message);
	    			System.out.println(label+" [x] Done '");
	    			/*
	    			 * 答应消息表示已成功接收消息并已将消息业务处理完
	    			 * deliveryTag:该消息的index
					 * multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
					 * basicAck​(long deliveryTag,boolean multiple)
	    			 */
	    			channel.basicAck(envelope.getDeliveryTag(), false);
	    		}
	    	};
	    	/*
	         * 指定消费队列——关闭自动答应，使用手动答应，方式消息丢失
	         * queue：队列名称
	         * autoAck：为了确保消息一定被消费者处理，rabbitMQ提供了消息确认功能，就是在消费者处理完任务之后，就给服务器一个回馈，服务器就会将该消息删除，如果消费者超时不回馈，那么服务器将就将该消息重新发送给其他消费者，默认开启
	         * 	开启：消费者收到消息后不等待处理立刻自动应答消息
	         *  关闭：消费者收到消息后不会自动应答消息，需要代码调用用channel.ack()、channel.nack()、channel.basicReject()等函数才会进行消息应答
	         * channel.basicConsume​(String queue, boolean autoAck, Consumer callback)
	         */ 
	        channel.basicConsume(QUEUE_NAME, false, consumer);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/** 
     * 每个点耗时1s 
     * @param task 
     * @throws InterruptedException 
     */  
    private static void doWork(String task) {  
        for (char ch : task.toCharArray())  
        {  
            if (ch == '.') {
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }  
    }
	
}
