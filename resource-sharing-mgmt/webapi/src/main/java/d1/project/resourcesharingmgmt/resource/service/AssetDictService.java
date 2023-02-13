package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.resource.dao.*;
import d1.project.resourcesharingmgmt.resource.entity.*;
import d1.project.resourcesharingmgmt.resource.model.AssetDict.AssetDictFindAllVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * 信息资源目录与云数据关联表
 *
 * @author zhengyang
 */
@Service
public class AssetDictService {
    private final AssetDictDao assetDictDao;
    private final IOperationLogService iOperationLogService;

    public AssetDictService(AssetDictDao assetDictDao, IOperationLogService iOperationLogService) {
        this.assetDictDao = assetDictDao;
        this.iOperationLogService = iOperationLogService;
    }

    /**
     * 新增字典
     *
     * @param entity
     */
    public void save(AssetDictEntity entity) throws DoValidException {
        assetDictDao.save(entity);
    }

    /**
     * 查询资源目录下所有云数据信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public List<AssetDictEntity> findAll(AssetDictFindAllVm model) throws Exception {
        SpecificationBuilder<AssetDictEntity> builder = new SpecificationBuilder<>();
        Specification<AssetDictEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("name", "name")
                .sEqual("value", "value")
                .sEqual("type", "type")
                .aOrder("sort")
                .build();
        return assetDictDao.findAll(specification);
    }

    public List<AssetDictEntity> findAll(){
        return assetDictDao.findAll();
    }

    public void deleteById(String id){
        assetDictDao.deleteById(id);
    }

    public void deleteBatch(List<String> ids){
        assetDictDao.deleteBatch(ids);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<AssetDictEntity> find(String id) {
        return assetDictDao.findById(id);
    }

    public boolean existsById(String id){
        return assetDictDao.existsById(id);
    }

    public boolean existsByValueAndType(String value,String type){
        return assetDictDao.existsByValueAndType(value, type);
    }
}
