package d1.project.resourcesharingmgmt.resource.business;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewStrExEntity;
import d1.project.resourcesharingmgmt.resource.service.ArchBusiUviewStrExService;
import org.springframework.stereotype.Service;

/**
 * 信息项
 * @author JungYoung
 */
@Service
public class ArchBusiUviewStrExBusiness {
    private final ArchBusiUviewStrExService archBusiUviewStrExService;

    public ArchBusiUviewStrExBusiness(ArchBusiUviewStrExService archBusiUviewStrExService) {
        this.archBusiUviewStrExService = archBusiUviewStrExService;
    }

    public ArchBusiUviewStrExEntity find(String id) throws DoValidException {
        ArchBusiUviewStrExEntity archBusiUviewStrExEntity = archBusiUviewStrExService.find(id).orElseThrow(() -> new DoValidException("信息项不存在"));
        return archBusiUviewStrExEntity;
    }
}
