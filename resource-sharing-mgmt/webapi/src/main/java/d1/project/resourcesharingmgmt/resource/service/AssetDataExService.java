package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.business.ArchBusiUviewExBusiness;
import d1.project.resourcesharingmgmt.resource.dao.AssetDataExDao;
import d1.project.resourcesharingmgmt.resource.entity.AssetDataExEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExUpdateVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 信息资源目录与云数据关联表
 *
 * @author zhengyang
 */
@Service
public class AssetDataExService {
    private final AssetDataExDao assetDataExDao;
    private final IOperationLogService iOperationLogService;
    private final ArchBusiUviewExBusiness archBusiUviewExBusiness;

    public AssetDataExService(AssetDataExDao assetDataExDao, IOperationLogService iOperationLogService, ArchBusiUviewExBusiness archBusiUviewExBusiness) {
        this.assetDataExDao = assetDataExDao;
        this.iOperationLogService = iOperationLogService;
        this.archBusiUviewExBusiness = archBusiUviewExBusiness;
    }

    /**
     * 删除云数据
     *
     * @param id
     * @param request
     * @throws DoValidException
     */
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        AssetDataExEntity assetDataExEntity = assetDataExDao.findById(id).orElseThrow(() -> new DoValidException("此资源目录下无该云数据信息"));
        assetDataExDao.delete(assetDataExEntity);
        if (assetDataExDao.countByUviewId(assetDataExEntity.getUviewId()) == 0) {
            ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
            archBusiUviewExUpdateVm.setId(assetDataExEntity.getUviewId());
            archBusiUviewExUpdateVm.setDataHookStatus("0");
            archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);
        }

        iOperationLogService.insert(new OperationLog("资源管理_云数据_删除", "云数据删除", "云数据删除：" + assetDataExEntity.getId(), JSON.toJSONString(assetDataExEntity), 1), request);
    }

    /**
     * 删除云数据
     *
     * @param ids
     * @param request
     * @throws DoValidException
     */
    public void deleteAll(Collection<String> ids, HttpServletRequest request) throws DoValidException {
        List<AssetDataExEntity> assetDataExEntitys = assetDataExDao.findAllById(ids);
        assetDataExDao.deleteAll(assetDataExEntitys);
        if (assetDataExDao.countByUviewId(assetDataExEntitys.get(0).getUviewId()) == 0) {
            ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
            archBusiUviewExUpdateVm.setId(assetDataExEntitys.get(0).getUviewId());
            archBusiUviewExUpdateVm.setDataHookStatus("0");
            archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);
        }

        iOperationLogService.insert(new OperationLog("资源管理_云数据_批量删除", "云数据批量删除", "云数据批量删除", JSON.toJSONString(assetDataExEntitys), 1), request);
    }

    /**
     * 查询资源目录下所有云数据信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<AssetDataExEntity> findAll(AssetDataExFindAllVm model) throws Exception {
        SpecificationBuilder<AssetDataExEntity> builder = new SpecificationBuilder<>();
        Specification<AssetDataExEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("uviewId", "uviewId")
                .dOrder("createTime")
                .build();
        return assetDataExDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<AssetDataExEntity> find(String id) {
        return assetDataExDao.findById(id);
    }

    /**
     * 查找云数据挂接信息
     *
     * @param id id
     */
    public AssetDataExEntity findByUviewId(String id) {
        return assetDataExDao.findFirstByUviewIdOrderByCreateTimeDesc(id);
    }

    /**
     * 统计挂接数
     *
     * @param orgId orgId
     */
    public long countByOrgId(String orgId) {
        return assetDataExDao.countByOrgId(orgId);
    }

    public long count(){
        return assetDataExDao.count();
    }
}
