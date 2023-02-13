package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.resource.dao.DictAssetCateDao;
import d1.project.resourcesharingmgmt.resource.entity.DictAssetCateEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 目录分类管理
 *
 * @author zhengyang
 */
@Service
public class DictAssetCateService {
    private final DictAssetCateDao dictAssetCateDao;

    public DictAssetCateService(DictAssetCateDao dictAssetCateDao) {
        this.dictAssetCateDao = dictAssetCateDao;
    }

    /**
     * 查询所有目录分类
     * @return
     * @throws Exception
     */
    public List<DictAssetCateEntity> findAll() throws Exception {
        SpecificationBuilder<DictAssetCateEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = new JSONObject();
        paramObject.put("status","1");
        Specification<DictAssetCateEntity> specification = builder.init(paramObject)
                .sEqual("status", "status")
                .aOrder("displaySn")
                .aOrder("typCd")
                .build();
       return dictAssetCateDao.findAll(specification);
    }

    /**
     * 根据父节点ID查询所有分类目录
     * @return
     * @throws Exception
     */
    public List<DictAssetCateEntity> findAllByParTypId(String parTypId) throws Exception {
        return dictAssetCateDao.findAllByParTypId(parTypId);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<DictAssetCateEntity> find(String id) {
        return dictAssetCateDao.findById(id);
    }

    /**
     * 根据id判断是否存在目录分类信息
     * @param typId
     * @return
     */
    public boolean existsByTypId(String typId){
        return dictAssetCateDao.existsByTypId(typId);
    }

    /**
     * 单条插入目录分类信息
     * @param dictAssetCateEntity
     */
    public void insert(DictAssetCateEntity dictAssetCateEntity){
        dictAssetCateDao.save(dictAssetCateEntity);
    }

    /**
     * 批量插入目录分类信息
     * @param list
     */
    public void insertAll(List<DictAssetCateEntity> list){
        dictAssetCateDao.saveAll(list);
    }

    /**
     * 删除分类
     * @param typId
     * @return
     */
    public void deleteByTypId(String typId){
        dictAssetCateDao.deleteByTypId(typId);
    }

    public void deleteBatchByTypId(List<String> ids){
        dictAssetCateDao.deleteBatchByTypId(ids);
    }
}
