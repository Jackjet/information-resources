//package com.digitalchina.resourcecatalog.db.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.digitalchina.resourcecatalog.db.dao.MqResourceMapper;
//import com.digitalchina.resourcecatalog.db.domain.MqResource;
//import com.digitalchina.resourcecatalog.db.event.QueueChangeEvent;
//import com.digitalchina.resourcecatalog.db.rabbitmq.MessageProducer;
//import com.digitalchina.resourcecatalog.db.rabbitmq.RabbitConfig;
//import com.digitalchina.resourcecatalog.db.service.MqResourceService;
//import com.digitalchina.resourcecatalog.db.util.AesUtils;
//import com.digitalchina.resourcecatalog.db.util.JsonMapper;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class MqResourceServiceImpl extends ServiceImpl<MqResourceMapper, MqResource> implements MqResourceService {
//
//    @Autowired
//    private ApplicationContext applicationContext;
//    
//	@Autowired
//	MessageProducer messageProducer;
//
//    @Override
//    @Transactional
//    public void insert(MqResource resource) {
//        String guid = UUID.randomUUID().toString().replaceAll("-", "");
//        resource.setClientId(guid);
//        String userSyncKey = AesUtils.aesEncrypt(resource.getName() + guid);
//        resource.setUserSyncKey(userSyncKey);
//        resource.setCreateTime(new Date());
//        this.baseMapper.insert(resource);
//        applicationContext.publishEvent(new QueueChangeEvent(RabbitConfig.QUEUE_ADD + "," + resource.getClientId()));
//    }
//
//    @Override
//    public void delete(Integer type, String clientId) {
//        MqResource resource = this.getOne(new QueryWrapper<MqResource>().eq(MqResource.TYPE, type).eq(MqResource.CLIENTID, clientId));
//        if (resource != null) {
//            applicationContext.publishEvent(new QueueChangeEvent(RabbitConfig.QUEUE_DELETE + "," + resource.getClientId()));
//            this.baseMapper.deleteById(resource.getId());
//        }
//    }
//
//    @Override
//	public void sendMessage(Integer type, Object message) {
//		List<MqResource> list=this.list(new QueryWrapper<MqResource>().eq(MqResource.TYPE, type));
//		if(list!=null && list.size()>0){
//			for (MqResource mqResource : list) {
//				messageProducer.sendMessage(RabbitConfig.USER_THIRE_EXCHAGE,mqResource.getClientId() , AesUtils.aecEncryptBySalt(JsonMapper.toJsonString(message), mqResource.getUserSyncKey()));
//			}
//		}else{
//			System.out.println("-----------------------没有查到序列----------------------"+type);
//		}
//    }
//}
