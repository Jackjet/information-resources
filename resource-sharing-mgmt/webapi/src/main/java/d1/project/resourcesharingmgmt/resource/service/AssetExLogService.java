package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.dao.AssetExLogDao;
import d1.project.resourcesharingmgmt.resource.entity.AssetExLogEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 资源挂接审批日志
 *
 * @author zhengyang
 */
@Service
public class AssetExLogService {
    private final AssetExLogDao assetExLogDao;
    private final IOperationLogService iOperationLogService;

    public AssetExLogService(AssetExLogDao assetExLogDao, IOperationLogService iOperationLogService) {
        this.assetExLogDao = assetExLogDao;
        this.iOperationLogService = iOperationLogService;
    }

    /**
     * 新增云数据
     *
     * @param entity
     */
    public void save(AssetExLogEntity entity, HttpServletRequest request) throws DoValidException {
        assetExLogDao.save(entity);
        iOperationLogService.insert(new OperationLog("资源使用_资源挂接申请_审批", "资源挂接申请审批日志", "日志新增：" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 查询资源目录下所有云数据信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<AssetExLogEntity> findAll(AssetExLogFindAllVm model) throws Exception {
        SpecificationBuilder<AssetExLogEntity> builder = new SpecificationBuilder<>();
        Specification<AssetExLogEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("assetExId", "assetExId")
                .aOrder("createTime")
                .build();
        return assetExLogDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<AssetExLogEntity> find(String id) {
        return assetExLogDao.findById(id);
    }

}
