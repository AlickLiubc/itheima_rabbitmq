package com.itheima;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

    // 1.注入RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testHelloWorld() {
        // 2.发送消息
        rabbitTemplate.convertAndSend("spring_queue", "hello world spring...");
    }

    /**
     * 发送fanout消息
     */
    @Test
    public void testFanout() {
        // 2.发送消息
        rabbitTemplate.convertAndSend("spring_fanout_exchange", "", "spring fanout...");
    }

    /**
     * 发送topic消息
     */
    @Test
    public void testTopic() {
        // 发送消息
        rabbitTemplate.convertAndSend("spring_topic_exchange", "heima.heihei.hahha", "spring topic...");
    }

    /**
     * 确认模式
     * 步骤：
     *      1. 确认模式开启：publisher-confirms="true"
     *      2. 在rabbitTemplate定义ConfirmCallBack回调函数
     */
    @Test
    public void testConfirm() {
        // 2.定义回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关的配置信息
             * @param b exchange交换机 是否成功收到了消息 true-成功，false-失败
             * @param s 失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("confirm方法被执行了。。。");

                if (b) {
                    // 接收成功
                    System.out.println("接收成功消息：" + s);
                } else {
                    // 接收失败
                    System.out.println("接收失败消息：" + s);
                    // 做一些处理，再次发送消息
                }
            }
        });

        // 3.发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "message confirm...");
    }

}
