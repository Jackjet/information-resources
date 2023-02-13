//package com.digitalchina.resourcecatalog.db.rabbitmq;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.Resource;
//
///**
// * @author baokd
// */
//@Configuration
//public class RabbitConfig {
//
//    Logger log = LoggerFactory.getLogger(RabbitConfig.class);
//  //给业务系统提供的用户同步exchange
//    public static final String USER_THIRE_EXCHAGE = "user.direct";
//
//    /**
//     * 增加q
//     **/
//    public static final String QUEUE_ADD = "add";
//
//    /**
//     * 删除q
//     **/
//    public static final String QUEUE_DELETE = "delete";
//    
//    //组织机构
//    public static final Integer ORG_SELECT = 1;
//    public static final Integer ORG_UPDATE = 3;
//    public static final Integer ORG_DELETE = 4;
//    //目录分类
//    public static final Integer DICT_SELECT = 5;
//    public static final Integer DICT_UPDATE = 7;
//    public static final Integer DICT_DELETE = 8;
//    //资源目录-共享
//    public static final Integer CATA_SELECT_SHARE = 9;
//    public static final Integer CATA_UPDATE_SHARE = 11;
//    public static final Integer CATA_DELETE_SHARE = 12;
//    //资源目录-开放
//    public static final Integer CATA_SELECT = 13;
//    public static final Integer CATA_UPDATE = 15;
//    public static final Integer CATA_DELETE = 16;
//    
//    
//    @Resource
//    private RabbitTemplate rabbitTemplate;
//
//    /**
//     * 如果mq是json格式可以启动 string的话就不需要了
//     */
////    @Bean
//    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
//        return rabbitTemplate;
//    }
//
//    //    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    /**
//     * 同步此q
//     * 有四个参数
//     * 1.队列名
//     * 2.durable 持久化消息队列，重启不删除，默认true
//     * 3.auto-delete 没有使用时候自动删除 默认false
//     * 4.exclusive 该消息队列只在当前connection生效，默认false
//     *
//     * @return
//     */
////    @Bean
//    public Queue userSyncQueue() {
//        return new Queue("USER_SYNC_QUEUE");
//    }
//
//    /**
//     * 用户交换机
//     *
//     * @param
//     * @return
//     **/
//    @Bean
//    public DirectExchange userThirdDirect() {
//        return new DirectExchange(USER_THIRE_EXCHAGE, true, false);
//    }
//
//}
//
