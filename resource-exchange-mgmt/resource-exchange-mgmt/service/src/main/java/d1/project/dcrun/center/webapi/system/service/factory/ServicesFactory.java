package d1.project.dcrun.center.webapi.system.service.factory;

import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.common.util.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工厂模式，根据类型返回对应的对象
 *
 * @author Buter
 * @date 2020/5/10 19:35
 */
@Service
public class ServicesFactory {
    private final WebapiSystemService webapiSystemService;
    private final EmqSystemService emqSystemService;
    private final DcApiSystemService dcApiSystemService;
    private final DataCacheSystemService dataCacheSystemService;

    @Autowired
    public ServicesFactory(WebapiSystemService webapiSystemService, EmqSystemService emqSystemService, DcApiSystemService dcApiSystemService, DataCacheSystemService dataCacheSystemService) {
        this.webapiSystemService = webapiSystemService;
        this.emqSystemService = emqSystemService;
        this.dcApiSystemService = dcApiSystemService;
        this.dataCacheSystemService = dataCacheSystemService;
    }

    public AbstractSystemService getSystemService(String type) throws Exception {
        ServiceType serviceType = ServiceType.getServiceTypeByName(type);
        switch (serviceType) {
            case EMQ:
                return emqSystemService;
            case DCAPI:
                return dcApiSystemService;
            case WEBAPI:
                return webapiSystemService;
            case DATACACHE:
                return dataCacheSystemService;
            default:
                throw new ValidException("没找到对应的系统服务类型：" + type);
        }
    }
}
