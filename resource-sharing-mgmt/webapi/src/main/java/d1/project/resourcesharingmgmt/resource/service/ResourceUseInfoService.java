package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.dao.ResourceUseInfoDao;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseExcelExport;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfoAnalysisVm;
import d1.project.resourcesharingmgmt.resource.model.Screen.SupportBusinessCountVm;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资源使用
 *
 * @author zhengyang
 */
@Service
public class ResourceUseInfoService {
    private final ResourceUseInfoDao resourceUseInfoDao;
    private final IOperationLogService operationLogService;

    public ResourceUseInfoService(ResourceUseInfoDao resourceUseInfoDao, IOperationLogService operationLogService) {
        this.resourceUseInfoDao = resourceUseInfoDao;
        this.operationLogService = operationLogService;
    }

    public void save(ResourceUseInfoEntity entity) {
        resourceUseInfoDao.save(entity);
    }

    public void update(JSONObject params, HttpServletRequest request) throws DoValidException {
        ResourceUseInfoEntity resourceUseInfoEntity = resourceUseInfoDao.findById(params.getString("id")).orElseThrow(() -> new DoValidException("资源使用信息不存在"));
        resourceUseInfoEntity.setSupportBusiness(params.getString("supportBusiness"));
        resourceUseInfoEntity.setVerifyMethod("Key-Auth");
        resourceUseInfoEntity.setPerMinute(params.getInteger("perMinute"));
        resourceUseInfoEntity.setEveryHour(params.getInteger("everyHour"));
        resourceUseInfoEntity.setEveryDay(params.getInteger("everyDay"));
        resourceUseInfoEntity.setSingleSize(params.getInteger("singleSize"));
        resourceUseInfoEntity.setAuditDesc(params.getString("auditDesc"));
        resourceUseInfoEntity.setStatus(params.getString("status"));
        resourceUseInfoEntity.setRejectDetail(params.getString("rejectDetail"));
        resourceUseInfoEntity.setMsg(params.getString("msg"));
        if ("2".equals(params.getString("status"))) {
            if ("1".equals(resourceUseInfoEntity.getType())) {
                resourceUseInfoEntity.setContainer(params.getString("container"));
                resourceUseInfoEntity.setRouteInfo(params.getString("routeInfo"));
                resourceUseInfoEntity.setKongApiId(params.getString("kongApiId"));
            }
        }
        if ("3".equals(resourceUseInfoEntity.getType())) {
            resourceUseInfoEntity.setIp(params.getString("ip"));
            resourceUseInfoEntity.setPort(params.getString("port"));
            resourceUseInfoEntity.setUsername(params.getString("username"));
            resourceUseInfoEntity.setPassword(params.getString("password"));
            resourceUseInfoEntity.setDatabaseName(params.getString("databaseName"));
            resourceUseInfoEntity.setTableName(params.getString("tableName"));
            resourceUseInfoEntity.setDataDetail(params.getString("dataDetail"));
        }
        TokenManager.getInstance().updateUpdateIdAndTime(request, resourceUseInfoEntity);
        resourceUseInfoDao.save(resourceUseInfoEntity);
        operationLogService.insert(new OperationLog("资源使用管理_资源使用审核", "审核", "审核资源使用" + resourceUseInfoEntity.getUviewNm(), JSON.toJSONString(resourceUseInfoEntity), 1), request);
    }

    /**
     * 查询资源使用
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<ResourceUseInfoEntity> findAll(ResourceUseInfoFindAllVm model) throws Exception {
        SpecificationBuilder<ResourceUseInfoEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<ResourceUseInfoEntity> specification = builder.init(paramObject)
                .sContain("resourceName", "resourceName")
                .sContain("uviewNm", "uviewNm")
                .sContain("uviewNo", "uviewNo")
                .sEqual("provOrgId", "provOrgId")
                .sEqual("provOrgCode", "provOrgCode")
                .sContain("provOrgName", "provOrgName")
                .sEqual("createDeptId", "createDeptId")
                .sContain("createDeptName", "createDeptName")
                .sEqual("status", "status")
                .sEqual("type", "type")
                .sContain("sourceApiName", "sourceApiName")
                .sEqual("createById", "createById")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return resourceUseInfoDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询资源使用
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<ResourceUseInfoEntity> findAll(JSONObject model) throws Exception {
        SpecificationBuilder<ResourceUseInfoEntity> builder = new SpecificationBuilder<>();
        Specification<ResourceUseInfoEntity> specification = builder.init(model)
                .sContain("uviewNm", "uviewNm")
                .sContain("uviewNo", "uviewNo")
                .sEqual("provOrgCode", "provOrgCode")
                .sEqual("createDeptId", "createDeptId")
                .sContain("createDeptName", "createDeptName")
                .sEqual("status", "status")
                .sEqual("type", "type")
                .sContain("sourceApiName", "sourceApiName")
                .sEqual("createById", "createById")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return resourceUseInfoDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询资源使用
     *
     * @param model
     * @return
     * @throws Exception
     */
    public List<ResourceUseExcelExport> findExportList(JSONObject model) throws Exception {
        SpecificationBuilder<ResourceUseInfoEntity> builder = new SpecificationBuilder<>();
        Specification<ResourceUseInfoEntity> specification = builder.init(model)
                .sContain("uviewNm", "uviewNm")
                .sContain("uviewNo", "uviewNo")
                .sEqual("provOrgCode", "provOrgCode")
                .sEqual("createDeptId", "createDeptId")
                .sContain("createDeptName", "createDeptName")
                .sEqual("status", "status")
                .sEqual("type", "type")
                .sContain("sourceApiName", "sourceApiName")
                .sEqual("createById", "createById")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        List<ResourceUseInfoEntity> listEntity = resourceUseInfoDao.findAll(specification);

        return listEntity.stream().map(e -> {
            ResourceUseExcelExport resource = new ResourceUseExcelExport();
            BeanUtils.copyProperties(e, resource);
            if (e.getCreateTime() != null) {
                resource.setCreateTime(e.getCreateTime().getTime());
            }
            switch (e.getType()) {
                case "1":
                    resource.setType("接口");
                    break;
                case "2":
                    resource.setType("文件");
                    break;
                case "3":
                    resource.setType("数据库");
                    break;
                default:
                    resource.setType("未知");
                    break;
            }
            return resource;
        }).collect(Collectors.toList());
    }

    /**
     * 查询资源使用
     *
     * @param model
     * @return
     * @throws Exception
     */
    public List<ResourceUseInfoEntity> findAllList(JSONObject model) throws Exception {
        SpecificationBuilder<ResourceUseInfoEntity> builder = new SpecificationBuilder<>();
        Specification<ResourceUseInfoEntity> specification = builder.init(model)
                .sEqual("createDeptId", "createDeptId")
                .sEqual("status", "status")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return resourceUseInfoDao.findAll(specification);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<ResourceUseInfoEntity> find(String id) {
        return resourceUseInfoDao.findById(id);
    }

    public void insert(ResourceUseInfoEntity resourceUseInfoEntity) throws DoValidException {
        resourceUseInfoDao.save(resourceUseInfoEntity);
    }

    /**
     * 删除资源使用
     *
     * @param id
     * @throws DoValidException
     */
    public void delete(String id) throws DoValidException {
        resourceUseInfoDao.deleteById(id);
    }

    /**
     * 统计
     *
     * @return
     */
    public long count() {
        return resourceUseInfoDao.count();
    }

    /**
     * 我的待办-需求使用申请
     *
     * @param createById
     * @return
     */
    public ResourceUseInfoAnalysisVm findAnalysis(String createById) throws DoValidException {
        return resourceUseInfoDao.findAnalysis(createById);
    }

    /**
     * 判断用户是否已经申请了云接口
     *
     * @param uviewApiId
     * @param createById
     * @return
     */
    public boolean existsByUviewApiIdAndCreateById(String uviewApiId, String createById) {
        return resourceUseInfoDao.existsByUviewApiIdAndCreateById(uviewApiId, createById);
    }

    public ResourceUseInfoEntity findFirstByUviewApiIdAndCreateByIdOrderByCreateTimeDesc(String uviewApiId, String createById) {
        return resourceUseInfoDao.findFirstByUviewApiIdAndCreateByIdOrderByCreateTimeDesc(uviewApiId, createById);
    }

    /**
     * 判断用户是否已经申请了云文件
     *
     * @param fileId
     * @param createById
     * @return
     */
    public boolean existsByFileIdAndCreateById(String fileId, String createById) {
        return resourceUseInfoDao.existsByFileIdAndCreateById(fileId, createById);
    }

    public ResourceUseInfoEntity findFirstByFileIdAndCreateByIdOrderByCreateTimeDesc(String fileId, String createById) {
        return resourceUseInfoDao.findFirstByFileIdAndCreateByIdOrderByCreateTimeDesc(fileId, createById);
    }

    /**
     * 判断用户是否已经申请了云数据
     *
     * @param dataId
     * @param createById
     * @return
     */
    public boolean existsByDataIdAndCreateById(String dataId, String createById) {
        return resourceUseInfoDao.existsByDataIdAndCreateById(dataId, createById);
    }

    public ResourceUseInfoEntity findFirstByDataIdAndCreateByIdOrderByCreateTimeDesc(String dataId, String createById) {
        return resourceUseInfoDao.findFirstByDataIdAndCreateByIdOrderByCreateTimeDesc(dataId, createById);
    }

    public long countByOrgNumDistinct(){
        return resourceUseInfoDao.countByOrgNumDistinct();
    }

    public long countByStatus(String status){
        return resourceUseInfoDao.countByStatus(status);
    }

    public long countByType(String type){
        return resourceUseInfoDao.countByType(type);
    }

    public long countByMonth(String month){
        return resourceUseInfoDao.countByMonth(month);
    }

    public Optional<ResourceUseInfoEntity> findFirstByCreateDeptIdAndFileDownloadUri(String createDeptId, String fileDownloadUri){
        return resourceUseInfoDao.findFirstByCreateDeptIdAndFileDownloadUri(createDeptId, fileDownloadUri);
    }

    public long countByCreateDeptIdAndStatus(String createDeptId, String status){
        return resourceUseInfoDao.countByCreateDeptIdAndStatus(createDeptId, status);
    }

    public List<SupportBusinessCountVm> findSupportBusinessCount(){
        return resourceUseInfoDao.findSupportBusinessCount();
    }
}
