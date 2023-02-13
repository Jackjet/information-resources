//package com.digitalchina.resourcecatalog.db.rabbitmq;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.digitalchina.resourcecatalog.db.domain.MqMessage;
//import com.digitalchina.resourcecatalog.db.service.MqMessageService;
//
//import javax.annotation.PostConstruct;
//import java.util.UUID;
//
///**
// * rabbitmq消息发送及回调
// *
// * @author baokd
// **/
//@Component
//public class MessageProducer {
//    private Logger logger = LoggerFactory.getLogger(MessageProducer.class);
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private MqMessageService mqMessageService;
//
//    /**
//     * 定制一些处理策略
//     * 定制化amqp模版
//     * <p>
//     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack yml需要配置 publisher-confirms: true
//     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack   yml需要配置 publisher-returns: true
//     * 1.如果消息没有到exchange,则confirm回调,ack=false
//     * 2.如果消息到达exchange,则confirm回调,ack=true
//     * 3.exchange到queue成功,则不回调return
//     * 4.exchange到queue失败,则回调return(需设置mandatory=true,否则不回调,消息就丢了)
//     */
//    @PostConstruct
//    public void setup() {
//
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (ack) {
//                logger.debug("消息发送到exchange成功,id: {}", correlationData.getId());
//            } else {
//                logger.error("消息发送到exchange失败, 原因: {}", cause);
//            }
//        });
//
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            String aesEncrypt = new String(message.getBody());
//            logger.error("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", aesEncrypt, replyCode, replyText, exchange, routingKey);
//            //存入数据库
//            MqMessage mqMessage = new MqMessage(message.getMessageProperties().getCorrelationId(), replyCode, exchange + "->" + routingKey + ":" + replyText, aesEncrypt);
//            mqMessageService.save(mqMessage);
//        });
//
//    }
//
//    public void sendMessage(String routeKey, Object message) {
//        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString().replaceAll("-", ""));
//        rabbitTemplate.convertAndSend(routeKey, message, correlationData);
//    }
//
//    public void sendMessage(String exchange, String routeKey, Object message) {
//        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString().replaceAll("-", ""));
//        rabbitTemplate.convertAndSend(exchange, routeKey, message, correlationData);
//    }
//}
