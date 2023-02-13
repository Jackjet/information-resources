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
import d1.project.resourcesharingmgmt.resource.dao.CorrectionDao;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewExEntity;
import d1.project.resourcesharingmgmt.resource.entity.CorrectionEntity;
import d1.project.resourcesharingmgmt.resource.entity.DemandedInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoUpdateVm;
import d1.project.resourcesharingmgmt.resource.model.correction.CorrectionFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.correction.CorrectionInsertVm;
import d1.project.resourcesharingmgmt.resource.model.correction.CorrectionUpdateVm;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 纠错
 *
 * @author zhengyang
 * @date 2021-11-05 16:17
 */
@Service
public class CorrectionService {
    private final CorrectionDao correctionDao;
    private final IOperationLogService operationLogService;
    private final OrganizationService organizationService;
    private final ArchBusiUviewExService archBusiUviewExService;

    public CorrectionService(CorrectionDao correctionDao, IOperationLogService operationLogService, OrganizationService organizationService, ArchBusiUviewExService archBusiUviewExService) {
        this.correctionDao = correctionDao;
        this.operationLogService = operationLogService;
        this.organizationService = organizationService;
        this.archBusiUviewExService = archBusiUviewExService;
    }

    public void insert(CorrectionInsertVm model, HttpServletRequest request) throws DoValidException {
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.findByUviewId(model.getUviewId()).orElseThrow(() -> new DoValidException("信息资源目录不存在"));
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        String userId = user.getString("id");
        String organizationId = user.getString("organizationId");

        if(correctionDao.existsByUviewIdAndCreateById(model.getUviewId(), userId)){
            throw new DoValidException("该资源目录您已提交纠错信息，不能重复提交");
        }

        OrganizationEntity userOrg = organizationService.find(organizationId).orElseThrow(() -> new DoValidException("当前用户部门不存在"));
        OrganizationEntity correctionOrg = organizationService.find(archBusiUviewExEntity.getProvOrgId()).orElseThrow(() -> new DoValidException("资源目录机构不存在"));

        CorrectionEntity entity = new CorrectionEntity();
        BeanUtils.copyProperties(model, entity);
        entity.setId(BaseUtils.generate32Id());
        entity.setUviewNm(archBusiUviewExEntity.getUviewNm());
        entity.setUviewNo(archBusiUviewExEntity.getUviewNo());
        entity.setCorrectionOrgId(organizationId);
        entity.setCorrectionOrgName(userOrg.getName());
        entity.setOrgId(correctionOrg.getId());
        entity.setOrgName(correctionOrg.getName());
        entity.setStatus(0);
        TokenManager.getInstance().updateCreateIdAndTime(request, entity);

        correctionDao.save(entity);

        operationLogService.insert(new OperationLog("纠错管理_新增纠错", "新增纠错", "新增纠错" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 审核
     *
     * @param model
     * @param request
     * @throws DoValidException
     */
    public void update(CorrectionUpdateVm model, HttpServletRequest request) throws DoValidException {
        CorrectionEntity entity = correctionDao.findById(model.getId()).orElseThrow(() -> new DoValidException("纠错不存在"));
        entity.setStatus(model.getStatus());
        entity.setReject(model.getReject());
        TokenManager.getInstance().updateUpdateIdAndTime(request, entity);

        correctionDao.save(entity);
        operationLogService.insert(new OperationLog("纠错管理_审核", "审核", "审核" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<CorrectionEntity> find(String id) {
        return correctionDao.findById(id);
    }


    /**
     * 查询所有
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<CorrectionEntity> findAll(CorrectionFindAllVm model) throws Exception {
        SpecificationBuilder<CorrectionEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<CorrectionEntity> specification = builder.init(paramObject)
                .sContain("orgId", "orgId")
                .sEqual("correctionOrgId", "correctionOrgId")
                .sEqual("status", "status")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return correctionDao.findAll(specification, builder.getPageable());
    }

    /**
     * 删除需求
     *
     * @param id
     * @throws DoValidException
     */
    public void delete(String id) throws DoValidException {
        correctionDao.deleteById(id);
    }
}
