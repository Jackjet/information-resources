package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.business.ArchBusiUviewExBusiness;
import d1.project.resourcesharingmgmt.resource.dao.AssetFileExDao;
import d1.project.resourcesharingmgmt.resource.entity.AssetFileExEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExUpdateVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetFileExFindAllVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 信息资源目录与云数据关联表
 *
 * @author zhengyang
 */
@Service
public class AssetFileExService {
    private final AssetFileExDao assetFileExDao;
    private final IOperationLogService iOperationLogService;
    private final ArchBusiUviewExBusiness archBusiUviewExBusiness;

    public AssetFileExService(AssetFileExDao assetFileExDao, IOperationLogService iOperationLogService, ArchBusiUviewExBusiness archBusiUviewExBusiness) {
        this.assetFileExDao = assetFileExDao;
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
        AssetFileExEntity assetFileExEntity = assetFileExDao.findById(id).orElseThrow(() -> new DoValidException("此资源目录下无该云数据信息"));
        assetFileExDao.delete(assetFileExEntity);
        if (assetFileExDao.countByUviewId(assetFileExEntity.getUviewId()) == 0) {
            ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
            archBusiUviewExUpdateVm.setId(assetFileExEntity.getUviewId());
            archBusiUviewExUpdateVm.setFileHookStatus("0");
            archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);
        }

        iOperationLogService.insert(new OperationLog("资源管理_云文件_删除", "云文件删除", "云文件删除：" + assetFileExEntity.getId(), JSON.toJSONString(assetFileExEntity), 1), request);
    }

    /**
     * 删除云数据
     *
     * @param ids
     * @param request
     * @throws DoValidException
     */
    public void deleteAll(Collection<String> ids, HttpServletRequest request) throws DoValidException {
        List<AssetFileExEntity> assetFileExEntitys = assetFileExDao.findAllById(ids);
        assetFileExDao.deleteAll(assetFileExEntitys);
        if (assetFileExDao.countByUviewId(assetFileExEntitys.get(0).getUviewId()) == 0) {
            ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
            archBusiUviewExUpdateVm.setId(assetFileExEntitys.get(0).getUviewId());
            archBusiUviewExUpdateVm.setFileHookStatus("0");
            archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);
        }

        iOperationLogService.insert(new OperationLog("资源管理_云文件_批量删除", "云文件批量删除", "云文件批量删除", JSON.toJSONString(assetFileExEntitys), 1), request);
    }

    /**
     * 查询资源目录下所有云数据信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<AssetFileExEntity> findAll(AssetFileExFindAllVm model) throws Exception {
        SpecificationBuilder<AssetFileExEntity> builder = new SpecificationBuilder<>();
        Specification<AssetFileExEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("uviewId", "uviewId")
                .sContain("name", "name")
                .sEqual("fileType", "fileType")
                .dOrder("createTime")
                .build();
        return assetFileExDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<AssetFileExEntity> find(String id) {
        return assetFileExDao.findById(id);
    }

    /**
     * 统计挂接数
     *
     * @param orgId orgId
     */
    public long countByOrgId(String orgId) {
        return assetFileExDao.countByOrgId(orgId);
    }

    public long count(){
        return assetFileExDao.count();
    }
}
