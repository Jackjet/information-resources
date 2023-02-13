package d1.project.api.integration.application.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.application.dao.NormalConsumerDao;
import d1.project.api.integration.application.entity.NormalConsumer;
import d1.project.api.integration.common.service.INormalConsumerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author libin
 */
@Service
public class NormalConsumerService implements INormalConsumerService {
    private final NormalConsumerDao normalConsumerDao;

    public NormalConsumerService(NormalConsumerDao normalConsumerDao) {
        this.normalConsumerDao = normalConsumerDao;
    }

    @Override
    public void save(NormalConsumer normalConsumer){
        normalConsumerDao.save(normalConsumer);
    }

    @Override
    public NormalConsumer findByAppid(String appid){
        return normalConsumerDao.findFirstByAppid(appid);
    }

    public void delete(NormalConsumer consumer){
        normalConsumerDao.delete(consumer);
    }

    public List<NormalConsumer> findAllByAppid(List<String> appids){
        return normalConsumerDao.findAllByAppidIn(appids);
    }

    public NormalConsumer findByCounsumerId(String consumerId) throws DoValidException {
        return normalConsumerDao.findFirstByConsumerId(consumerId);
    }
}
