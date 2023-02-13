package d1.project.resource.tag.business;

import com.alibaba.fastjson.JSONObject;
import d1.project.resource.common.Constants;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.tag.entity.TagInfo;
import d1.project.resource.tag.mapper.TagMapper;
import d1.project.resource.tag.model.TagVm;
import d1.project.resource.tag.service.TagInfoService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

/**
 * @author baozh
 */
@Service
public class TagInfoBusiness {
    private final TagInfoService tagInfoService;
    private final TagMapper mapper;
    private final IOperationLogService iOperationLogService;

    public TagInfoBusiness(TagInfoService tagInfoService, IOperationLogService iOperationLogService) {
        this.tagInfoService = tagInfoService;
        this.iOperationLogService = iOperationLogService;
        this.mapper = Mappers.getMapper(TagMapper.class);
    }

    public Page<TagInfo> findTags(JSONObject params) throws Exception {
        return tagInfoService.findAll(params);
    }

    public List<TagInfo> findTagsList(JSONObject params) throws Exception {
        return tagInfoService.findList(params);
    }

    /**
     * find tag detail
     *
     * @param id tag id
     * @return tag detail
     * @throws Exception throw null exception
     */
    public TagInfo findTagById(String id) throws Exception {
        TagInfo tagInfo = tagInfoService.findById(id);
        if (tagInfo == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        return tagInfo;
    }

    /**
     * insert tag
     *
     * @param tagVm   insertVm
     * @param request request info
     * @throws Exception throw tag repeat Exception
     */
    public void insertTag(TagVm tagVm, HttpServletRequest request) throws Exception {
        String tagName = tagVm.getName();
        if (tagInfoService.existsAllByName(tagName)) {
            throw new Exception(Constants.TAG_REPEAT_ERROR);
        }
        TagInfo tagInfo = new TagInfo();
        mapper.tagInsert(tagVm, tagInfo);
        tagInfo.setCreateTime(Calendar.getInstance());
        tagInfo.setUpdateTime(Calendar.getInstance());
        JSONObject userVm = BaseUtils.getUserInfo(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        tagInfo.setCreateById(userId);
        tagInfo.setUpdateById(userId);
        tagInfo.setCreateName(userName);
        tagInfo.setUpdateName(userName);
        tagInfo.setId(BaseUtils.generate32Id());
        tagInfoService.insert(tagInfo);
        iOperationLogService.insert(new OperationLog("标签管理", "添加标签", "添加标签", "添加标签:" + tagName, 1), request);
    }

    /**
     * update tag
     *
     * @param tagVm   updateVm
     * @param request request info
     * @throws Exception throw tag repeat Exception
     */
    public void updateTag(TagVm tagVm, HttpServletRequest request) throws Exception {
        String tagName = tagVm.getName();
        String id = tagVm.getId();
        if (tagInfoService.existsAllByNameAndIdNot(tagName, id)) {
            throw new Exception(Constants.TAG_REPEAT_ERROR);
        }
        TagInfo tagInfo = tagInfoService.findById(id);
        mapper.tagUpdate(tagVm, tagInfo);
        tagInfo.setUpdateTime(Calendar.getInstance());
        JSONObject userVm = BaseUtils.getUserInfo(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        tagInfo.setUpdateById(userId);
        tagInfo.setUpdateName(userName);
        tagInfoService.insert(tagInfo);
        iOperationLogService.insert(new OperationLog("标签管理", "编辑标签", "编辑标签", "编辑标签:" + tagName, 1), request);
    }

    /**
     * delete tag info
     *
     * @param id      data id
     * @param request request info
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        tagInfoService.deleteById(id);
        iOperationLogService.insert(new OperationLog("标签管理", "删除标签", "删除标签", "删除标签:" + id, 1), request);
    }
}
