package d1.project.resourcesharingmgmt.resource.business;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.model.DataAggregationVm;
import d1.project.resourcesharingmgmt.resource.service.ArchBusiUviewExService;
import d1.project.resourcesharingmgmt.resource.service.DemandedInfoService;
import d1.project.resourcesharingmgmt.resource.service.ResourceUseInfoService;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import org.springframework.stereotype.Service;

/**
 * 数据汇聚
 *
 * @author JungYoung
 */
@Service
public class DataAggregationBusiness {
    private final ArchBusiUviewExService archBusiUviewExService;
    private final DemandedInfoService demandedInfoService;
    private final ResourceUseInfoService resourceUseInfoService;
    private final OrganizationService organizationService;

    public DataAggregationBusiness(ArchBusiUviewExService archBusiUviewExService, DemandedInfoService demandedInfoService, ResourceUseInfoService resourceUseInfoService, OrganizationService organizationService) {
        this.archBusiUviewExService = archBusiUviewExService;
        this.demandedInfoService = demandedInfoService;
        this.resourceUseInfoService = resourceUseInfoService;
        this.organizationService = organizationService;
    }

    public DataAggregationVm find() throws DoValidException {
        DataAggregationVm dataAggregationVm = new DataAggregationVm();
        dataAggregationVm.setOrg(organizationService.countByParentId("headquarters"));
        dataAggregationVm.setUview(archBusiUviewExService.countByStatus("0"));
        dataAggregationVm.setDemanded(demandedInfoService.count());
        dataAggregationVm.setResourceUse(resourceUseInfoService.count());
        return dataAggregationVm;
    }
}
