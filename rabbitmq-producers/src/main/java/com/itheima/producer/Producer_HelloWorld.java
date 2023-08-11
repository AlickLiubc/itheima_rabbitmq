package com.itheima.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_HelloWorld {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 2.设置参数
        factory.setHost("192.168.2.110");
        factory.setPort(5672);
        factory.setVirtualHost("itcast");
        factory.setUsername("admin");
        factory.setPassword("admin");

        // 3.创建连接 Connection
        Connection connection = factory.newConnection();

        // 4.创建Channel
        Channel channel = connection.createChannel();

        // 5.创建队列Queue
        /**
         * String queue, boolean durable, boolean exclusive,
         * boolean autoDelete, Map<String, Object> arguments
         */
        channel.queueDeclare("hello_world", true, false, false, null);

        // 6.发送消息
        /**
         * String exchange,
         * String routingKey,
         * BasicProperties props,
         * byte[] body
         */
        String message = "hello rabbitmq~~~";
        channel.basicPublish("", "hello_world", null, message.getBytes());

        // 7.释放资源
        channel.close();
        connection.close();
    }

}
