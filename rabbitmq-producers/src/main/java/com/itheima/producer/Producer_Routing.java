package com.itheima.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_Routing {

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

        // 5.创建交换机
        /*
        String exchange, BuiltinExchangeType type, boolean durable,
        boolean autoDelete, Map<String, Object> arguments
        */
        String exchangeName = "test_direct";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, null);

        // 6.创建队列Queue
        /**
         * String queue, boolean durable, boolean exclusive,
         * boolean autoDelete, Map<String, Object> arguments
         */
        String queueName1 = "test_direct_queue1";
        String queueName2 = "test_direct_queue2";
        channel.queueDeclare(queueName1, true, false, false, null);
        channel.queueDeclare(queueName2, true, false, false, null);

        // 7.队列绑定交换机
        channel.queueBind(queueName1, exchangeName, "error");

        channel.queueBind(queueName2, exchangeName, "info");
        channel.queueBind(queueName2, exchangeName, "warning");
        channel.queueBind(queueName2, exchangeName, "error");

        // 8.发送消息
        String message = "张三调用了findAll()方法，日志等级为：error";
        channel.basicPublish(exchangeName, "error", null, message.getBytes());

        // 9.关闭连接
        channel.close();
        connection.close();
    }

}
