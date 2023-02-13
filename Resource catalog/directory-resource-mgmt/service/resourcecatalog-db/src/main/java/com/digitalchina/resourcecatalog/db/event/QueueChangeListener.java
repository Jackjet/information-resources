//package com.digitalchina.resourcecatalog.db.event;
//
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
////import com.digitalchina.resourcecatalog.db.rabbitmq.RabbitConfig;
//
///**
// * 队列动态增加删除绑定
// *
// * @author baokd
// **/
//@Component
//public class QueueChangeListener implements ApplicationListener<QueueChangeEvent> {
//
//    @Autowired
//    private DirectExchange userThirdDirect;
//
//    @Autowired
//    private ConnectionFactory connectionFactory;
//
//    @Override
//    public void onApplicationEvent(QueueChangeEvent queueChangeEvent) {
//        String source = (String) queueChangeEvent.getSource();
//        String[] strings = source.split(",");
////        if (RabbitConfig.QUEUE_ADD.equals(strings[0])) {
////            //增加
////            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
////            rabbitAdmin.declareQueue(new Queue(strings[1]));
////
////            rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(strings[1])).to(userThirdDirect).with(strings[1]));
////        } else if (RabbitConfig.QUEUE_DELETE.equals(strings[0])) {
////            //删除
////            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
////            rabbitAdmin.deleteQueue(strings[1]);
////        }
//
//    }
//}
