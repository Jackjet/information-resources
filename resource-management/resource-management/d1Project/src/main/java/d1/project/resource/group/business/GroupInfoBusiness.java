package d1.project.resource.group.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.resource.api.service.SourceApiService;
import d1.project.resource.common.Constants;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.group.entity.GroupInfo;
import d1.project.resource.group.mapper.GroupMapper;
import d1.project.resource.group.model.GroupTree;
import d1.project.resource.group.model.GroupVm;
import d1.project.resource.group.service.GroupInfoService;
import d1.project.resource.resourcemanage.service.ContainerInfoService;
import d1.project.resource.resourcemanage.service.DataSourceInfoService;
import d1.project.resource.tag.service.TagInfoService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author baozh
 */
@Service
public class GroupInfoBusiness {
    private final GroupInfoService groupInfoService;
    private final SourceApiService sourceApiService;
    private final ContainerInfoService containerInfoService;
    private final DataSourceInfoService dataSourceInfoService;
    private final TagInfoService tagInfoService;
    private final IOperationLogService iOperationLogService;

    private final GroupMapper mapper;

    public GroupInfoBusiness(GroupInfoService groupInfoService, SourceApiService sourceApiService, ContainerInfoService containerInfoService, DataSourceInfoService dataSourceInfoService, TagInfoService tagInfoService, IOperationLogService iOperationLogService) {
        this.groupInfoService = groupInfoService;
        this.sourceApiService = sourceApiService;
        this.containerInfoService = containerInfoService;
        this.dataSourceInfoService = dataSourceInfoService;
        this.tagInfoService = tagInfoService;
        this.iOperationLogService = iOperationLogService;
        this.mapper = Mappers.getMapper(GroupMapper.class);
    }

    /**
     * find group tree list
     *
     * @param type group type
     * @return group tree list
     */
    public List<GroupTree> findTreeList(String type) {
        List<GroupInfo> list = groupInfoService.findAllByTypeOrderByCreateTimeAsc(type);
        if (list == null || list.size() == 0) {
            return null;
        }
        List<GroupTree> returnList = new ArrayList<>();
        for (GroupInfo groupInfo : list) {
            String parentId = groupInfo.getParentId();
            if ("1".equals(parentId)) {
                String id = groupInfo.getId();
                GroupTree groupTree = new GroupTree();
                groupTree.setName(groupInfo.getName());
                groupTree.setLevel(0);
                groupTree.setId(groupInfo.getId());
                groupTree.setChild(getTree(id, list, 0));
                returnList.add(groupTree);
            }
        }
        return returnList;
    }

    /**
     * find group tree list
     *
     * @param type group type
     * @return group tree list
     */
    public JSONArray findGroupList(String type) {
        List<GroupInfo> list = groupInfoService.findAllByTypeOrderByCreateTimeAsc(type);
        if (list == null || list.size() == 0) {
            return null;
        }
        JSONArray returnList = new JSONArray();
        for (GroupInfo groupInfo : list) {
            String parentId = groupInfo.getParentId();
            if ("1".equals(parentId)) {
                String id = groupInfo.getId();
                JSONObject groupList = new JSONObject();
                groupList.put("label", groupInfo.getName());
                groupList.put("value", groupInfo.getId());
                JSONArray child = getList(id, list, 0);
                if (child.size() > 0) {
                    groupList.put("children", child);
                }
                returnList.add(groupList);
            }
        }
        return returnList;
    }

    /**
     * insert group info
     *
     * @param insertVm insertVm
     * @param request  request info for get user info
     * @throws Exception throw repeat error
     */
    public void insertGroup(GroupVm insertVm, HttpServletRequest request) throws Exception {
        String name = insertVm.getName();
        String parentId = insertVm.getParentId();
        if (StringUtils.isEmpty(parentId)) {
            parentId = "1";
            insertVm.setParentId(parentId);
        }
        String type = insertVm.getType();
        if (groupInfoService.existsAllByTypeAndName(type, name)) {
            throw new Exception(Constants.DATA_NAME_REPEAT);
        }
        GroupInfo groupInfo = new GroupInfo();
        mapper.groupInsert(insertVm, groupInfo);
        JSONObject userVm = BaseUtils.getUserInfo(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        groupInfo.setUpdateById(userId);
        groupInfo.setCreateById(userName);
        groupInfo.setUpdateTime(Calendar.getInstance());
        groupInfo.setCreateTime(Calendar.getInstance());
        groupInfo.setId(BaseUtils.generate32Id());
        groupInfoService.insert(groupInfo);
        iOperationLogService.insert(new OperationLog("分组管理", "添加分组", "添加分组", "添加分组:" + name, 1), request);
    }

    /**
     * insert group info
     *
     * @param updateVm updateVm
     * @param request  request info for get user info
     * @throws Exception throw repeat error
     */
    public void updateGroup(GroupVm updateVm, HttpServletRequest request) throws Exception {
        String name = updateVm.getName();
        String id = updateVm.getId();
        GroupInfo groupInfo = groupInfoService.findById(id);
        if (groupInfo == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        String parentId = groupInfo.getParentId();
        if (groupInfoService.existsAllByParentIdAndNameAndIdNot(parentId, name, id)) {
            throw new Exception(Constants.DATA_NAME_REPEAT);
        }
        mapper.groupUpdate(updateVm, groupInfo);
        groupInfo.setUpdateTime(Calendar.getInstance());
        JSONObject userVm = BaseUtils.getUserInfo(request);
        String userId = userVm.getString("id");
        groupInfo.setUpdateById(userId);
        groupInfoService.insert(groupInfo);
        iOperationLogService.insert(new OperationLog("分组管理", "编辑分组", "编辑分组", "编辑分组:" + name, 1), request);
    }

    /**
     * delete group
     *
     * @param id      group id
     * @param request request info
     * @throws Exception throw null/has child exception
     */
    public void deleteGroupById(String id, HttpServletRequest request) throws Exception {
        List<GroupInfo> list = groupInfoService.findAllByParentId(id);
        if (list.size() > 0) {
            throw new Exception(Constants.GROUP_HAS_CHILD);
        }
        GroupInfo groupInfo = groupInfoService.findById(id);
        if (groupInfo == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        String type = groupInfo.getType();
        boolean isTrue;
        switch (type) {
            case "0":
                isTrue = sourceApiService.existsAllByGroupId(id);
                break;
            case "1":
                isTrue = containerInfoService.existsAllByGroupId(id);
                break;
            case "2":
                isTrue = dataSourceInfoService.existsAllByGroupId(id);
                break;
            case "3":
                isTrue = tagInfoService.existsAllByGroupId(id);
                break;
            default:
                throw new Exception(Constants.GROUP_TYPE_ERROR);
        }
        if (isTrue) {
            throw new Exception(Constants.GROUP_HAS_RESOURCE);
        }
        groupInfoService.delete(id);
        iOperationLogService.insert(new OperationLog("分组管理", "删除分组", "删除分组", "删除分组:" + groupInfo.getName(), 1), request);
    }


    private List<GroupTree> getTree(String startPoint, List<GroupInfo> list, int level) {
        List<GroupTree> treeList = new ArrayList<>();
        for (GroupInfo groupInfo : list) {
            String parentId = groupInfo.getParentId();
            String name = groupInfo.getName();
            String id = groupInfo.getId();
            if (startPoint.equals(parentId)) {
                GroupTree groupTree = new GroupTree();
                groupTree.setName(name);
                groupTree.setLevel(level);
                groupTree.setId(id);
                groupTree.setChild(getTree(id, list, level + 1));
                treeList.add(groupTree);
            }
        }
        return treeList;
    }

    private JSONArray getList(String startPoint, List<GroupInfo> list, int level) {
        JSONArray treeList = new JSONArray();
        for (GroupInfo groupInfo : list) {
            String parentId = groupInfo.getParentId();
            String name = groupInfo.getName();
            String id = groupInfo.getId();
            if (startPoint.equals(parentId)) {
                JSONObject groupList = new JSONObject();
                groupList.put("label", name);
                groupList.put("value", groupInfo.getId());
                JSONArray child = getList(id, list, level + 1);
                if (child.size() > 0) {
                    groupList.put("children", child);
                }
                treeList.add(groupList);
            }
        }
        return treeList;
    }
}
