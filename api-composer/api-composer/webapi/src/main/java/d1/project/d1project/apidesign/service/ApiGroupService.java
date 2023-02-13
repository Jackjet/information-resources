package d1.project.d1project.apidesign.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.apidesign.dao.ApiDesignDao;
import d1.project.d1project.apidesign.dao.ApiGroupDao;
import d1.project.d1project.apidesign.entity.ApiGroup;
import d1.project.d1project.apidesign.model.ApiGroupInsertVm;
import d1.project.d1project.apidesign.model.ApiGroupTree;
import d1.project.d1project.apidesign.model.ApiGroupUpdateVm;
import d1.project.d1project.common.Constants;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengyang
 */
@Service
public class ApiGroupService {
    private final ApiGroupDao apiGroupDao;
    private final ApiDesignDao apiDesignDao;
    private final WebAdminUserService webAdminUserService;

    public ApiGroupService(ApiGroupDao apiGroupDao, ApiDesignDao apiDesignDao, WebAdminUserService webAdminUserService) {
        this.apiGroupDao = apiGroupDao;
        this.apiDesignDao = apiDesignDao;
        this.webAdminUserService = webAdminUserService;
    }

    public List<ApiGroupTree> findTreeList(String name) throws Exception {
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(name)) {
            json.put("name", name);
        }
        List<ApiGroup> list = findAll(json);
        List<ApiGroupTree> returnList = new ArrayList<>();
        returnList = getTree("", list, 0);
        return returnList;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(ApiGroupInsertVm model, HttpServletRequest request) throws DoValidException {
        if (apiGroupDao.existsAllByNameAndParentId(model.getName(), model.getParentId())) {
            throw new DoValidException(Constants.DATA_NAME_REPEAT);
        }
        ApiGroup apiGroup = new ApiGroup();
        BeanUtils.copyProperties(model, apiGroup);
        if (StringUtils.isEmpty(model.getParentId())) {
            apiGroup.setParentId("1");
        }
        apiGroup.setId(BaseUtils.generate32Id());
        webAdminUserService.updateCreateIdAndTime(request, apiGroup);
        apiGroupDao.save(apiGroup);
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws DoValidException {
        ApiGroup apiGroup = apiGroupDao.findById(id).orElseThrow(() -> new DoValidException("任务组不存在"));
        //查询分组下面是否有分组
        if (apiGroupDao.existsAllByParentId(id)) {
            throw new DoValidException(Constants.DATA_NEXT_HAS);
        }
        //查询分组下面是否有任务
        if (apiDesignDao.existsAllByGroupId(id)) {
            throw new DoValidException(Constants.API_GROUP_HAS);
        }
        apiGroupDao.deleteById(apiGroup.getId());
    }

    /**
     * 更新
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(ApiGroupUpdateVm model, HttpServletRequest request) throws DoValidException {
        ApiGroup apiGroup = apiGroupDao.findById(model.getId()).orElseThrow(() -> new DoValidException("任务不存在"));
        if (apiGroupDao.existsAllByNameAndParentIdAndIdNot(model.getName(), apiGroup.getParentId(), model.getId())) {
            throw new DoValidException(Constants.DATA_NAME_REPEAT);
        }
        BeanUtils.copyProperties(model, apiGroup);
        webAdminUserService.updateUpdateIdAndTime(request, apiGroup);
        apiGroupDao.save(apiGroup);
    }

    /**
     * 查询所有
     *
     * @param model 模型
     */
    private List<ApiGroup> findAll(JSONObject model) throws Exception {
        SpecificationBuilder<ApiGroup> builder = new SpecificationBuilder<>();
        Specification<ApiGroup> specification = builder.init(model)
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return apiGroupDao.findAll(specification);
    }

    private List<ApiGroupTree> getTree(String startPoint, List<ApiGroup> list, int level) {
        List<ApiGroupTree> treeList = new ArrayList<>();
        boolean isRoot = false;
        for (ApiGroup apiGroup : list) {
            String parentId = apiGroup.getParentId();
            String name = apiGroup.getName();
            String id = apiGroup.getId();
            if ("1".equals(parentId) && level == 0 || startPoint.equals(parentId)) {
                ApiGroupTree apiGroupTree = new ApiGroupTree();
                apiGroupTree.setName(name);
                apiGroupTree.setLevel(level);
                apiGroupTree.setId(id);
                apiGroupTree.setChild(getTree(id, list, level + 1));
                treeList.add(apiGroupTree);
            }
        }
        return treeList;
    }
}
