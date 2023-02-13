package d1.project.resource.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resource.api.dao.SourceApiDao;
import d1.project.resource.api.dao.SourceApiViewDao;
import d1.project.resource.api.entity.SourceApi;
import d1.project.resource.api.model.SourceApiView;
import d1.project.resource.common.Constants;
import d1.project.resource.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class SourceApiService {
    private final SourceApiDao sourceApiDao;
    private final SourceApiViewDao sourceApiViewDao;

    public SourceApiService(SourceApiDao sourceApiDao, SourceApiViewDao sourceApiViewDao) {
        this.sourceApiDao = sourceApiDao;
        this.sourceApiViewDao = sourceApiViewDao;
    }

    public SourceApi findById(String id) throws Exception {
        Optional<SourceApi> tempInfo = sourceApiDao.findById(id);
        if (!tempInfo.isPresent()) {
            throw new Exception(Constants.DATA_NULL);
        }
        return tempInfo.get();
    }

    public SourceApiView findSourceApiViewById(String id) throws DoValidException {
        return sourceApiViewDao.findById(id).orElseThrow(() -> new DoValidException("源API信息不存在"));
    }

    public Page<SourceApiView> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<SourceApiView> builder = new SpecificationBuilder<>();
        Specification<SourceApiView> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tagName", "tagName")
                .sContain("tagValue", "tagValue")
                .sEqual("groupId", "groupId")
                .sNotIn("id","idsNot")
                .dOrder("updateTime").build();
        return sourceApiViewDao.findAll(specification, builder.getPageable());
    }

    public Page<SourceApi> findForOtherSys(JSONObject params) throws Exception {
        SpecificationBuilder<SourceApi> builder = new SpecificationBuilder<>();
        Specification<SourceApi> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tagName", "tagName")
                .sContain("tagValue", "tagValue")
                .sEqual("groupId", "groupId")
                .dOrder("updateTime").build();
        return sourceApiDao.findAll(specification, builder.getPageable());
    }

    public List<SourceApiView> findAllForExcel(JSONObject params) throws Exception {
        SpecificationBuilder<SourceApiView> builder = new SpecificationBuilder<>();
        Specification<SourceApiView> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tagName", "tagName")
                .sContain("tagValue", "tagValue")
                .sEqual("groupId", "groupId")
                .dOrder("updateTime").build();
        return sourceApiViewDao.findAll(specification);
    }

    public void insert(HttpServletRequest request, JSONObject params) throws Exception {
        SourceApi sourceApi = JSON.toJavaObject(params, SourceApi.class);
        if (this.sourceApiDao.existsByName(sourceApi.getName())) {
            throw new DoValidException(sourceApi.getName() + "已存在");
        }
        sourceApi.setId(BaseUtils.generate32Id());
        TokenManager.updateCreateIdAndTime(request, sourceApi);
        TokenManager.updateUpdateIdAndTime(request, sourceApi);
        this.sourceApiDao.save(sourceApi);
    }

    public void insertDesign(HttpServletRequest request, SourceApi sourceApi) throws DoValidException {
        TokenManager.updateCreateIdAndTime(request, sourceApi);
        TokenManager.updateUpdateIdAndTime(request, sourceApi);
        this.sourceApiDao.save(sourceApi);
    }

    public void update(HttpServletRequest request, SourceApi sourceApi) throws Exception {
        if (existsByNameAndIdNot(sourceApi.getName(), sourceApi.getId())) {
            throw new Exception(Constants.API_NAME_REPEAT);
        }
        SourceApi data = this.sourceApiDao.findById(sourceApi.getId()).orElseThrow(() -> new DoValidException("API不存在"));
        BeanUtils.copyProperties(sourceApi, data);
        TokenManager.updateUpdateIdAndTime(request, data);
        this.sourceApiDao.save(data);
    }

    public void deleteById(String id) throws Exception {
        SourceApi data = this.sourceApiDao.findById(id).orElseThrow(() -> new DoValidException("源API不存在"));
        this.sourceApiDao.delete(data);
    }

    public void batchSave(List<SourceApi> list) {
        this.sourceApiDao.saveAll(list);
    }

    public List<SourceApi> findAllByNameIn(List<String> names) {
        return sourceApiDao.findAllByNameIn(names);
    }

    public Boolean existsByName(String name) {
        return this.sourceApiDao.existsByName(name);
    }

    public Boolean existsByNameAndIdNot(String name, String id) {
        return this.sourceApiDao.existsByNameAndIdNot(name, id);
    }

    public boolean existsAllByGroupId(String groupId) {
        return sourceApiDao.existsAllByGroupId(groupId);
    }

}
