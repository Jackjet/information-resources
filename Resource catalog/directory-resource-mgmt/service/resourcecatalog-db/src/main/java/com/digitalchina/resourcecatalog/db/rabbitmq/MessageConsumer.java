//package com.digitalchina.resourcecatalog.db.rabbitmq;
//
//import com.digitalchina.resourcecatalog.db.util.AesUtils;
//import com.rabbitmq.client.Channel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Component;
//
///**
// * 消费者
// *
// * @author baokd
// **/
////@Component
//public class MessageConsumer {
//    private Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
//
//    @RabbitListener(queues = "002745c2e9694523a938aa1315b7b0d7")
//    @RabbitHandler
//    public void onMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
//        logger.debug("consumer receive message------->:{}", message);
//        try {
//            String aesEncrypt = new String(message.getBody());
//            String decryptStr = AesUtils.aesDecryptBySalt(aesEncrypt, "uxhoP2G/1oMG2fNyNiUf9E84qdKyWe4QJW3jrTksbD/S/eU1IHo3rIR6JY93m90jgfKWyhIbPzSNrWewEIkYpw==");
//            logger.debug("consumer receive message------->:{}", decryptStr);
//            // 采用手动应答模式, 手动确认应答更为安全稳定
////            channel.basicAck(tag, true);
//        } catch (Exception e) {
//            // 异常情况 :根据需要去： 重发/ 丢弃
//            // 重发一定次数后， 丢弃， 日志告警
//            e.printStackTrace();
//            channel.basicNack(tag, false, false);
//        }
//
//    }
//
//}
