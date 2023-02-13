package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.resource.dao.ArchBusiUviewStrExDao;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewStrExEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewStrExFindAllVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 信息项
 *
 * @author zhengyang
 */
@Service
public class ArchBusiUviewStrExService {
    private final ArchBusiUviewStrExDao archBusiUviewStrExDao;

    public ArchBusiUviewStrExService(ArchBusiUviewStrExDao archBusiUviewStrExDao) {
        this.archBusiUviewStrExDao = archBusiUviewStrExDao;
    }

    /**
     * 查询所有
     * @param model
     * @return
     * @throws Exception
     */
    public Page<ArchBusiUviewStrExEntity> findAll(ArchBusiUviewStrExFindAllVm model) throws Exception {
        SpecificationBuilder<ArchBusiUviewStrExEntity> builder = new SpecificationBuilder<>();
        Specification<ArchBusiUviewStrExEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("uviewId", "uviewId")
                .aOrder("sno")
                .build();
        return archBusiUviewStrExDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<ArchBusiUviewStrExEntity> find(String id) {
        return archBusiUviewStrExDao.findById(id);
    }

    /**
     * 根据信息项Id,判断是否存在资源目录信息项
     * @param UviewstrId
     * @return
     */
    public boolean existsByUviewstrId(String UviewstrId){
        return archBusiUviewStrExDao.existsByUviewstrId(UviewstrId);
    }

    /**
     *单条插入资源目录信息项
     * @param entity
     */
    public void insert(ArchBusiUviewStrExEntity entity){
        archBusiUviewStrExDao.save(entity);
    }

    /**
     *批量插入资源目录信息项
     * @param list
     */
    public void insertAll(List<ArchBusiUviewStrExEntity> list){
        archBusiUviewStrExDao.saveAll(list);
    }

    public void deleteByUviewId(String uviewId){
        archBusiUviewStrExDao.deleteByUviewId(uviewId);
    }
}
