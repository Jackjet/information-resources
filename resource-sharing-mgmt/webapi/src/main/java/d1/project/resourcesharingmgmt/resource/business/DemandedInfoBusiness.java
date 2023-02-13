package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.entity.DemandedInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.*;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfoAnalysisVm;
import d1.project.resourcesharingmgmt.resource.service.DemandedInfoService;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.model.WebAdminUserVm;
import d1.project.resourcesharingmgmt.system.service.IWebAdminUserService;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 需求
 *
 * @author JungYoung
 */
@Service
public class DemandedInfoBusiness {
    private final DemandedInfoService demandedInfoService;
    private final OrganizationService organizationService;
    private final IWebAdminUserService iWebAdminUserService;
    private final IOperationLogService iOperationLogService;


    public DemandedInfoBusiness(DemandedInfoService demandedInfoService, OrganizationService organizationService, IWebAdminUserService iWebAdminUserService, IOperationLogService iOperationLogService) {
        this.demandedInfoService = demandedInfoService;
        this.organizationService = organizationService;
        this.iWebAdminUserService = iWebAdminUserService;
        this.iOperationLogService = iOperationLogService;
    }

    public DemandedInfoVm find(String id) throws DoValidException {
        DemandedInfoEntity demandedInfoEntity = demandedInfoService.find(id).orElseThrow(() -> new DoValidException("需求不存在"));
        DemandedInfoVm demandedInfoVm = new DemandedInfoVm();
        BeanUtils.copyProperties(demandedInfoEntity, demandedInfoVm);
        WebAdminUserVm webAdminUserVm = iWebAdminUserService.find(demandedInfoEntity.getCreateById());
        demandedInfoVm.setCreateByName(webAdminUserVm.getName());
        return demandedInfoVm;
    }

    /**
     * 查询当前登录人需要受理的需求
     *
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    public Page<DemandedInfoEntity> findAllByAcceptDeptId(DemandedInfoFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);

        if (!webAdminUserVm.getString("roleId").contains("admin")) {
            model.setAcceptDeptId(webAdminUserVm.getString("organizationId"));
        }
//        model.setStatus("0");
        return demandedInfoService.findAll(model);
    }

    /**
     * 查询用户所有需求
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Page<DemandedInfoEntity> findAll(DemandedInfoFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        model.setCreateById(user.getString("id"));
        return demandedInfoService.findAll(model);
    }

    /**
     * 新增
     */
    public void insert(DemandedInfoInsertVm model, HttpServletRequest request) throws Exception {
        if(demandedInfoService.existsByTitleAndAcceptDeptId(model.getTitle(), model.getAcceptDeptId())){
            throw new DoValidException(Constants.DEMANDED_IS_EXIST);
        }

        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        DemandedInfoEntity demandedInfoEntity = new DemandedInfoEntity();
        BeanUtils.copyProperties(model, demandedInfoEntity);
        demandedInfoEntity.setCreateDeptId(user.getString("organizationId"));
        demandedInfoEntity.setCreateDeptName(user.getString("organizationName"));
        demandedInfoService.insert(demandedInfoEntity, request);
        iOperationLogService.insert(new OperationLog("需求管理_需求", "新增", "新增需求" + demandedInfoEntity.getTitle(), JSON.toJSONString(demandedInfoEntity), 1), request);
    }

    /**
     * 删除
     * @param id
     * @param request
     * @throws DoValidException
     */
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        DemandedInfoEntity demandedInfoEntity = demandedInfoService.find(id).orElseThrow(() -> new DoValidException("需求不存在"));
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        if(demandedInfoEntity.getCreateById().equals(user.getString("id"))){
            demandedInfoService.delete(demandedInfoEntity.getId());
        }else {
            throw new DoValidException(Constants.DEMANDED_IS_NOT_USER);
        }
        if(!"0".equals(demandedInfoEntity.getStatus())){
            throw new DoValidException(Constants.DEMANDED_IS_ACCEP);
        }
    }

    /**
     * 我的待办-需求申请
     * @param request
     * @return
     * @throws DoValidException
     */
    public DemandedInfoAnalysisVm findAnalysis(HttpServletRequest request) throws DoValidException {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        DemandedInfoAnalysisVm demandedInfoAnalysisVm = demandedInfoService.findAnalysis(user.getString("id"));
        return demandedInfoAnalysisVm;
    }

    public Page<DemandedInfoByProvOrgIdVm> findResourceUseInfoByProvOrgId(DemandedInfoFindByProvOrgIdVm model) throws Exception {
        JSONObject orgParam = new JSONObject();
        orgParam.put("id", model.getCreateDeptId());
        orgParam.put("page", model.getPage());
        orgParam.put("size", model.getSize());
        orgParam.put("level", 1);
        Page<OrganizationEntity> orgPage = organizationService.findAll(orgParam);
        List<DemandedInfoByProvOrgIdVm> list = new ArrayList<>();
        for (OrganizationEntity org : orgPage.getContent()) {
            JSONObject demandedParam = new JSONObject();
            if (!org.getId().equals("headquarters")) {
                demandedParam.put("createDeptId", org.getId());
            }
            demandedParam.put("startTime", model.getStartTime());
            demandedParam.put("endTime", model.getEndTime());
            List<DemandedInfoEntity> demandedInfoEntityList = demandedInfoService.findAllList(demandedParam);

            DemandedInfoByProvOrgIdVm orgVm = new DemandedInfoByProvOrgIdVm();
            orgVm.setCreateDeptId(org.getId());
            orgVm.setCreateDeptName(org.getName());
            orgVm.setDemandedNum(demandedInfoEntityList.size());
            orgVm.setUntreated(demandedInfoEntityList.stream().filter(i -> i.getStatus().equals("0")).collect(Collectors.toList()).size());
            orgVm.setProcessed(demandedInfoEntityList.stream().filter(i -> i.getStatus().equals("1")).collect(Collectors.toList()).size());
            orgVm.setReject(demandedInfoEntityList.stream().filter(i -> i.getStatus().equals("2")).collect(Collectors.toList()).size());

            list.add(orgVm);
        }

        return new PageImpl<>(list, orgPage.getPageable(), orgPage.getTotalElements());
    }

    public List<DemandedInfoByProvOrgIdExport> exportDemandedInfoCountByProvOrgId(DemandedInfoFindByProvOrgIdVm model) throws Exception {
        JSONObject orgParam = new JSONObject();
        orgParam.put("level", 1);
        List<OrganizationEntity> orgPage = organizationService.findAllList(orgParam);
        List<DemandedInfoByProvOrgIdExport> list = new ArrayList<>();
        for (OrganizationEntity org : orgPage) {
            JSONObject demandedParam = new JSONObject();
            demandedParam.put("createDeptId", org.getId());
            demandedParam.put("startTime", model.getStartTime());
            demandedParam.put("endTime", model.getEndTime());
            List<DemandedInfoEntity> demandedInfoEntityList = demandedInfoService.findAllList(demandedParam);

            DemandedInfoByProvOrgIdExport orgVm = new DemandedInfoByProvOrgIdExport();
            orgVm.setCreateDeptName(org.getName());
            orgVm.setDemandedNum(demandedInfoEntityList.size());
            orgVm.setUntreated(demandedInfoEntityList.stream().filter(i -> i.getStatus().equals("0")).collect(Collectors.toList()).size());
            orgVm.setProcessed(demandedInfoEntityList.stream().filter(i -> i.getStatus().equals("1")).collect(Collectors.toList()).size());
            orgVm.setReject(demandedInfoEntityList.stream().filter(i -> i.getStatus().equals("2")).collect(Collectors.toList()).size());

            list.add(orgVm);
        }

        return list;
    }
}
