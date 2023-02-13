package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.dao.ResourceUseLogDao;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseLogEntity;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseExcelExport;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseLogExcelExport;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseLogFindAllVm;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资源使用
 *
 * @author zhengyang
 */
@Service
public class ResourceUseLogService {
    private final ResourceUseLogDao resourceUseLogDao;
    private final IOperationLogService operationLogService;

    public ResourceUseLogService(ResourceUseLogDao resourceUseLogDao, IOperationLogService operationLogService) {
        this.resourceUseLogDao = resourceUseLogDao;
        this.operationLogService = operationLogService;
    }

    public void save(ResourceUseLogEntity entity) {
        resourceUseLogDao.save(entity);
    }

    /**
     * 查询资源使用
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<ResourceUseLogEntity> findAll(ResourceUseLogFindAllVm model) throws Exception {
        SpecificationBuilder<ResourceUseLogEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<ResourceUseLogEntity> specification = builder.init(paramObject)
                .sEqual("resourceId","resourceId")
                .sContain("resourceName", "resourceName")
                .sContain("createByName", "createByName")
                .sEqual("status", "status")
                .aOrder("createTime")
                .build();
        return resourceUseLogDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询资源使用
     *
     * @param model
     * @return
     * @throws Exception
     */
    public List<ResourceUseLogExcelExport> findExportList(ResourceUseLogFindAllVm model) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SpecificationBuilder<ResourceUseLogEntity> builder = new SpecificationBuilder<>();
        Specification<ResourceUseLogEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("resourceId","resourceId")
                .sContain("resourceName", "resourceName")
                .sContain("createByName", "createByName")
                .sEqual("status", "status")
                .aOrder("createTime")
                .build();
        List<ResourceUseLogEntity> listEntity =  resourceUseLogDao.findAll(specification);

        return listEntity.stream().map(e -> {
            ResourceUseLogExcelExport resource = new ResourceUseLogExcelExport();
            BeanUtils.copyProperties(e, resource);
            if(e.getCreateTime()!=null) {
                resource.setCreateTime(sdf.format(e.getCreateTime().getTime()));
            }
            return resource;
        }).collect(Collectors.toList());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<ResourceUseLogEntity> find(String id) {
        return resourceUseLogDao.findById(id);
    }

    /**
     * 删除资源使用
     *
     * @param id
     * @throws DoValidException
     */
    public void delete(String id) throws DoValidException {
        resourceUseLogDao.deleteById(id);
    }
}
