package d1.project.api.integration.kong.service;

import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.kong.dao.DefaultConsumerDao;
import d1.project.api.integration.kong.entity.DefaultConsumer;
import org.springframework.stereotype.Service;

/**
 * @author libin
 */
@Service
public class DefaultConsumerService {
    private final DefaultConsumerDao defaultConsumerDao;

    public DefaultConsumerService(DefaultConsumerDao defaultConsumerDao) {
        this.defaultConsumerDao = defaultConsumerDao;
    }

    /**
     * 初始化默认用户
     * @param container 容器，ip:port
     * @return
     */
    public DefaultConsumer initConsumer(String container){
        DefaultConsumer consumer = new DefaultConsumer();

        String id = BaseUtils.generate32Id();
        consumer.setId(id);
        consumer.setUsername(id);
        consumer.setKey(id);
        consumer.setGroups(id);
        consumer.setContainer(container);

        return consumer;
    }

    /**
     * 保存用户
     * @param consumer  用户实体
     */
    public void save(DefaultConsumer consumer){
        defaultConsumerDao.save(consumer);
    }


    /**
     * 查询默认用户
     * @param container 容器，ip:port
     * @return
     */
    public DefaultConsumer findByContainer(String container){
        return defaultConsumerDao.findFirstByContainer(container);
    }
}
