package d1.project.api.integration.common.service;

import d1.project.api.integration.application.entity.NormalConsumer;

public interface INormalConsumerService {
    void save(NormalConsumer normalConsumer);
    NormalConsumer findByAppid(String appid);
}
