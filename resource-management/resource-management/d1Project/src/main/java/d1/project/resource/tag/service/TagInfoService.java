package d1.project.resource.tag.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resource.tag.dao.TagInfoDao;
import d1.project.resource.tag.entity.TagInfo;
import d1.project.resource.tag.model.TagReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 标签管理
 *
 * @author baozh
 */
@Service
public class TagInfoService {
    private final TagInfoDao tagInfoDao;

    public TagInfoService(TagInfoDao tagInfoDao) {
        this.tagInfoDao = tagInfoDao;
    }

    public Page<TagInfo> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<TagInfo> builder = new SpecificationBuilder<>();
        Specification<TagInfo> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("groupId", "groupId")
                .dOrder("updateTime").build();
        return tagInfoDao.findAll(specification, builder.getPageable());
    }

    public List<TagInfo> findList(JSONObject params) throws Exception {
        SpecificationBuilder<TagInfo> builder = new SpecificationBuilder<>();
        Specification<TagInfo> specification = builder.init(params)
                .sContain("name", "name")
                .dOrder("updateTime").build();
        return tagInfoDao.findAll(specification);
    }

    public List<TagReturn> findAllForList() {
        return tagInfoDao.getTagList();
    }

    public TagInfo findById(String id) {
        Optional<TagInfo> data = tagInfoDao.findById(id);
        return data.orElse(null);
    }

    public void insert(TagInfo tagInfo) {
        tagInfoDao.save(tagInfo);
    }

    public void deleteById(String id) {
        tagInfoDao.deleteById(id);
    }

    public boolean existsAllByName(String name) {
        return tagInfoDao.existsAllByName(name);
    }

    public boolean existsAllByNameAndIdNot(String name, String id) {
        return tagInfoDao.existsAllByNameAndIdNot(name, id);
    }

    public boolean existsAllByGroupId(String groupId) {
        return tagInfoDao.existsAllByGroupId(groupId);
    }
}
