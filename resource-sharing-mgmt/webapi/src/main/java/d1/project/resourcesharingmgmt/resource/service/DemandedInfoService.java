package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.DemandedInfoDao;
import d1.project.resourcesharingmgmt.resource.entity.DemandedInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoUpdateVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfoAnalysisVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 需求管理
 *
 * @author zhengyang
 */
@Service
public class DemandedInfoService {
    private final DemandedInfoDao demandedInfoDao;
    private final IOperationLogService operationLogService;

    public DemandedInfoService(DemandedInfoDao demandedInfoDao, IOperationLogService operationLogService) {
        this.demandedInfoDao = demandedInfoDao;
        this.operationLogService = operationLogService;
    }

    /**
     * 新增
     */
    public void insert(DemandedInfoEntity demandedInfoEntity, HttpServletRequest request) throws DoValidException {
        demandedInfoEntity.setId(BaseUtils.generate32Id());
        demandedInfoEntity.setStatus("0");
        TokenManager.getInstance().updateCreateIdAndTime(request, demandedInfoEntity);
        demandedInfoDao.save(demandedInfoEntity);
    }

    /**
     * 审核
     *
     * @param model
     * @param request
     * @throws DoValidException
     */
    public void update(DemandedInfoUpdateVm model, HttpServletRequest request) throws DoValidException {
        DemandedInfoEntity demandedInfoEntity = demandedInfoDao.findById(model.getId()).orElseThrow(() -> new DoValidException("需求申请不存在"));
        demandedInfoEntity.setStatus(model.getStatus());
        demandedInfoEntity.setOpinion(model.getOpinion());
        TokenManager.getInstance().updateUpdateIdAndTime(request, demandedInfoEntity);
        demandedInfoDao.save(demandedInfoEntity);
        operationLogService.insert(new OperationLog("需求管理_需求审核", "审核", "审核需求申请" + demandedInfoEntity.getTitle(), JSON.toJSONString(demandedInfoEntity), 1), request);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<DemandedInfoEntity> find(String id) {
        return demandedInfoDao.findById(id);
    }


    /**
     * 查询所有
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<DemandedInfoEntity> findAll(DemandedInfoFindAllVm model) throws Exception {
        SpecificationBuilder<DemandedInfoEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<DemandedInfoEntity> specification = builder.init(paramObject)
                .sContain("title", "title")
                .sEqual("acceptDeptId", "acceptDeptId")
                .sEqual("acceptDept", "acceptDept")
                .sContain("createDeptName", "createDeptName")
                .sEqual("status", "status")
                .sEqual("requestType", "requestType")
                .sEqual("createById", "createById")
                .sEqual("createDeptId", "createDeptId")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return demandedInfoDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询所有
     *
     * @param model
     * @return
     * @throws Exception
     */
    public List<DemandedInfoEntity> findAllList(JSONObject model) throws Exception {
        SpecificationBuilder<DemandedInfoEntity> builder = new SpecificationBuilder<>();
        Specification<DemandedInfoEntity> specification = builder.init(model)
                .sEqual("createDeptId", "createDeptId")
                .sEqual("status", "status")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return demandedInfoDao.findAll(specification);
    }

    /**
     * 删除需求
     *
     * @param id
     * @throws DoValidException
     */
    public void delete(String id) throws DoValidException {
        demandedInfoDao.deleteById(id);
    }

    /**
     * 统计
     *
     * @return
     */
    public long count() {
        return demandedInfoDao.count();
    }

    /**
     * 我的待办-需求申请
     *
     * @param createById
     * @return
     */
    public DemandedInfoAnalysisVm findAnalysis(String createById) throws DoValidException {
        return demandedInfoDao.findAnalysis(createById);
    }

    public boolean existsByTitleAndAcceptDeptId(String title, String acceptDeptId){
        return demandedInfoDao.existsByTitleAndAcceptDeptId(title, acceptDeptId);
    }

    public Integer countByStatus(String status) {
        return demandedInfoDao.countByStatus(status);
    }

    public long countByReason(String reason){
        return demandedInfoDao.countByReason(reason);
    }

    public long countByOrgNumDistinct(){
        return demandedInfoDao.countByOrgNumDistinct();
    }

    public long countByRequestType(String requestType){
        return demandedInfoDao.countByRequestType(requestType);
    }

    public long countByCreateDeptId(String createDeptId){
        return demandedInfoDao.countByCreateDeptId(createDeptId);
    }
}
