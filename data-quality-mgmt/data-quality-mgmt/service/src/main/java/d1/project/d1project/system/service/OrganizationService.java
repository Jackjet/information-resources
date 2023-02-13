package d1.project.d1project.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.d1project.common.Constants;
import d1.project.d1project.common.model.OperationLog;
import d1.project.d1project.common.service.IOperationLogService;
import d1.project.d1project.common.service.IOrganizationService;
import d1.project.d1project.common.utils.BaseUtils;
import d1.project.d1project.system.dao.OrganizationDao;
import d1.project.d1project.system.dao.WebAdminUserDao;
import d1.project.d1project.system.entity.OrganizationEntity;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import d1.project.d1project.system.mapper.OrganizationMapper;
import d1.project.d1project.system.model.OrganizationTree;
import d1.project.d1project.system.model.vm.OrganizationInsertVm;
import d1.project.d1project.system.model.vm.OrganizationUpdateSeqVm;
import d1.project.d1project.system.model.vm.OrganizationUpdateVm;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织机构
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:34
 */
@Service
public class OrganizationService implements IOrganizationService {

    private final OrganizationDao organizationDao;
    private final WebAdminUserDao webAdminUserDao;

    private final IOperationLogService iOperationLogService;
    private final OrganizationUserService organizationUserService;

    private final OrganizationMapper mapper;

    public OrganizationService(OrganizationDao organizationDao, IOperationLogService iOperationLogService, OrganizationUserService organizationUserService, WebAdminUserDao webAdminUserDao) {
        this.organizationDao = organizationDao;
        this.iOperationLogService = iOperationLogService;
        this.mapper = Mappers.getMapper(OrganizationMapper.class);
        this.organizationUserService = organizationUserService;
        this.webAdminUserDao = webAdminUserDao;
    }

    /**
     * 新增
     */
    public void insert(OrganizationInsertVm model, HttpServletRequest request) throws DoValidException {
        if (model.getName().contains("/")) {
            throw new DoValidException("组织机构名称不可使用:/");
        }
        /*映射类型与补齐信息*/
        OrganizationEntity organization = mapper.dtoFormatIntoInsertEntity(model);
        TokenManager.getInstance().updateCreateIdAndTime(request, organization);
        String id = BaseUtils.generate32Id();
        String parentId = organization.getParentId();
        organization.setId(id);
        /*寻找父级*/
        OrganizationEntity organization1 = organizationDao.findById(parentId).orElseThrow(() -> new DoValidException("父级不存在"));
        int i = organization1.getLevel() + 1;
        /*寻找父级最后一个序列默认是第一个*/
        OrganizationEntity organization2 = organizationDao.findFirstByParentIdOrderBySeqDesc(StringUtils.isEmpty(parentId) ? "" : parentId).orElse(null);
        organization.setSeq(0);
        if (organization2 != null) {
            organization.setSeq(organization2.getSeq() + 1);
        }
        organization.setLevel(i);
        organization.setLevelMsg(BaseUtils.toHanStr(i + 1) + "级");
        organization.setIdLink(organization1.getIdLink() + "_" + id);
        organization.setNameLink(organization1.getNameLink() + "/" + model.getName());
        if (organizationDao.existsByNameAndParentId(model.getName(), organization.getParentId())) {
            throw new DoValidException("请重新输入，该组织机构名称已存在");
        }

        organizationDao.save(organization);
        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "新增", "新增组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }


    /**
     * 删除
     */
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        if (Constants.CANNOT_DELETED_ORGANIZATION.equals(id)) {
            throw new DoValidException("初始化组织机构不可删除");
        }
        OrganizationEntity organization = find(id).orElseThrow(() -> new DoValidException("组织机构不存在"));
        List<String> collect = organizationDao.findByIdLinkLike(organization.getIdLink() + "%").stream().map(OrganizationEntity::getId).collect(Collectors.toList());
        if (organizationUserService.existsByOrganizationIdIn(collect)) {
            throw new DoValidException("请移除该组织机构下全部用户再执行删除");
        }

        organizationDao.deleteById(id);
        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "删除", "删除组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }

    /**
     * 删除
     */
    public void deleteById(String id) throws Exception {
        organizationDao.deleteById(id);
    }

    /**
     * 更新
     */
    public void update(OrganizationUpdateVm model, HttpServletRequest request) throws DoValidException {
        if (model.getName().contains("/")) {
            throw new DoValidException("组织机构名称不可使用:/");
        }
        String oldName;
        String oldNameLink;
        OrganizationEntity organization = organizationDao.findById(model.getId()).orElseThrow(() -> new DoValidException("组织机构不存在"));
        oldName = organization.getName();
        oldNameLink = organization.getNameLink();
        mapper.copyProperties(model, organization);
        TokenManager.getInstance().updateUpdateIdAndTime(request, organization);

        if (organizationDao.existsByNameAndParentIdAndIdNot(organization.getName(), organization.getParentId(), organization.getId())) {
            throw new DoValidException("请重新输入，该组织机构名称已存在");
        }

        organizationDao.save(organization);

        if (!oldName.equals(model.getName())) {
            List<OrganizationEntity> byNameLink = organizationDao.findByNameLinkLike(oldNameLink + "%").stream().peek(organizationEntity -> {
                organizationEntity.setNameLink(organizationEntity.getNameLink().replace(oldName, model.getName()));
            }).collect(Collectors.toList());
            organizationDao.saveAll(byNameLink);
        }

        if (!oldName.equals(organization.getName())) {
            List<WebAdminUserEntity> collect = webAdminUserDao.findByOrganizationNameLike(oldName + "%").stream().peek(webAdminUser -> {
                String[] split = webAdminUser.getOrganizationName().split(",");
                List<String> strings = new ArrayList<>();
                for (String s : split) {
                    if (s.equals(oldName)) {
                        strings.add(organization.getName());
                        continue;
                    }
                    strings.add(s);
                }
                webAdminUser.setOrganizationName(String.join(",", strings));
            }).collect(Collectors.toList());
            webAdminUserDao.saveAll(collect);
        }

        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "编辑", "编辑组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }

    /**
     * 序列更新
     *
     * @param model 模型
     */
    public void updateSeq(OrganizationUpdateSeqVm model, HttpServletRequest request) throws DoValidException {
        OrganizationEntity organization = organizationDao.findById(model.getId()).orElseThrow(() -> new DoValidException("组织机构不存在"));
        int seq = organization.getSeq();
        int i;
        /*动作:0->1上移->下移*/
        if (model.getAction() == 0) {
            i = seq - 1;
            if (i < 0) {
                return;
            }

            OrganizationEntity byParentIdAndSeq = organizationDao.findByParentIdAndSeq(organization.getParentId(), i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() + 1);
            organizationDao.save(byParentIdAndSeq);

        } else {
            i = seq + 1;
            OrganizationEntity byParentIdAndSeq = organizationDao.findByParentIdAndSeq(organization.getParentId(), i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() - 1);
            organizationDao.save(byParentIdAndSeq);

        }

        organization.setSeq(i);
        organizationDao.save(organization);

        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "编辑序列", "编辑序列组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }

    /**
     * 保存
     */
    public void save(OrganizationEntity entity) throws Exception {
        organizationDao.save(entity);
    }

    /**
     * 查询所有
     */
    public OrganizationTree findAll() {
        List<OrganizationEntity> seq = organizationDao.findAll(Sort.by(Sort.Direction.ASC, "seq"));
        return getOrganizationTreeVm(seq);
    }

    public List<OrganizationEntity> findAllList() {
        return organizationDao.findAll();
    }

    /**
     * 根据id查询组织结构树
     */
    public OrganizationTree findAllById(String id) {
        List<OrganizationEntity> byLevelAsc = organizationDao.findByIdLinkLikeOrderBySeqAsc(id);
        return getOrganizationTreeVm(byLevelAsc);
    }

    /**
     * 详情
     */
    public Optional<OrganizationEntity> find(String id) {
        return organizationDao.findById(id);
    }

    /**
     * 详情
     */
    public Optional<OrganizationEntity> findByName(String name) {
        return organizationDao.findByName(name);
    }

    /**
     * 详情
     */
    public Optional<OrganizationEntity> findByNameLink(String nameLink) {
        return organizationDao.findByNameLink(nameLink);
    }

    @Override
    public JSONArray getForest() {
        Map<String, List<OrganizationEntity>> map = getDataAndGroupByParentId();
        if(map == null) {
            return null;
        }

        JSONArray data = new JSONArray();

        //查询根节点
        data = (JSONArray) JSON.toJSON(getRoot(map));
        //构建森林
        getChildren(data, map);
        return data;
    }

    //=======================================================================================================
    /**
     * 根据组织结构列表获取属性结构
     *
     * @param byLevelAsc 组织结构列表
     */
    private OrganizationTree getOrganizationTreeVm(List<OrganizationEntity> byLevelAsc) {
        OrganizationTree organizationTreeVm = new OrganizationTree();
        for (OrganizationEntity organization : byLevelAsc) {
            if (StringUtils.isEmpty(organization.getParentId())) {
                organizationTreeVm = mapper.entityFormatIntoDtoTree(organization);
                recursion(byLevelAsc, organizationTreeVm);
            }
        }
        return organizationTreeVm;
    }

    /**
     * 递归
     *
     * @param organizationList    组织机构列表
     * @param organizationTreeVm1 组织机构树
     */
    private void recursion(List<OrganizationEntity> organizationList, OrganizationTree organizationTreeVm1) {
        for (OrganizationEntity organization1 : organizationList) {
            if (organizationTreeVm1.getId().equals(organization1.getParentId())) {
                List<OrganizationTree> children = organizationTreeVm1.getChildren();
                OrganizationTree organizationTreeVm2 = mapper.entityFormatIntoDtoTree(organization1);
                children.add(organizationTreeVm2);
                recursion(organizationList, organizationTreeVm2);
            }
        }
    }

    private Map<String, List<OrganizationEntity>> getDataAndGroupByParentId(){
        List<OrganizationEntity> data = organizationDao.findAll();
        if(data.size() > 0) {
            return data.stream().collect(Collectors.groupingBy(OrganizationEntity::getParentId));
        } else {
            return null;
        }
    }

    private List<OrganizationEntity> getRoot(Map<String, List<OrganizationEntity>> map){
        return map.get("");
    }

    private void getChildren(JSONArray array, Map<String, List<OrganizationEntity>> map) {
        for (Object obj : array) {
            JSONObject item = (JSONObject) obj;

            List<OrganizationEntity> children = map.get(item.get("id"));

            if (children != null) {
                item.put("children", JSON.toJSON(children));
                getChildren((JSONArray) item.get("children"), map);
            }
        }
    }
}
