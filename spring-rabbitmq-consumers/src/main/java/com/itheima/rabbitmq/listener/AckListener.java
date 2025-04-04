package com.itheima.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * Consumer ACK机制：
 * 1. 设置手动签收。acknowledge="manual"
 * 2. 让监听器类实现ChannelAwareMessageListener接口
 * 3. 如果这个消息成功处理，则调用Channel的basicAck()签收
 * 4. 如果这个消息处理失败，则调用Channel的basicNack()拒绝，broker重新发送给Consumer
 *
 *
 */
@Component
public class AckListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Thread.sleep(500);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 1. 接收转换消息
            System.out.println(new String(message.getBody()));

            // 2. 处理业务逻辑
            int i = 1 / 0;
            System.out.println("处理业务逻辑。。。");

            // 3. 手动签收
            channel.basicAck(deliveryTag, true);
        } catch (Exception ex) {
            // ex.printStackTrace();

            // 4.拒绝签收
            /*
            第3个参数：requeue: 重回队列
                     如果设置为true，则消息重新回到queue，broker会重新发送该消息。
            */
            channel.basicNack(deliveryTag, true, true);
        }

    }

//    @Override
//    public void onMessage(Message message) {
//        System.out.println(new String(message.getBody()));
//    }

}
