package com.itheima.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_WorkQueues2 {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 2.设置参数
        factory.setHost("192.168.2.110");
        factory.setPort(5672);
        factory.setVirtualHost("itcast");
        factory.setUsername("admin");
        factory.setPassword("admin");


        // 3.创建连接
        Connection connection = factory.newConnection();

        // 4.创建Channel
        Channel channel = connection.createChannel();

        // 5.创建队列Queue
        /**
         * String queue, boolean durable, boolean exclusive,
         * boolean autoDelete, Map<String, Object> arguments
         */
        channel.queueDeclare("work_queues", true, false, false, null);

        // 6.接收消息
        /**
         * String queue, boolean autoAck,
         * Consumer callback
         */
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                /*System.out.println("consumerTag:" + consumerTag);
                System.out.println("exchange:" + envelope.getExchange());
                System.out.println("routingKey:" + envelope.getRoutingKey());
                System.out.println("properties:" + properties);*/
                System.out.println("body:" + new String(body));
            }
        };
        channel.basicConsume("work_queues", true, consumer);
    }

}
