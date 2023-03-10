package d1.project.resource.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resource.common.Constants;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.system.dao.RoleDao;
import d1.project.resource.system.dao.RoleMenuTreeDao;
import d1.project.resource.system.dao.WebAdminUserDao;
import d1.project.resource.system.entity.RoleEntity;
import d1.project.resource.system.entity.RoleUserEntity;
import d1.project.resource.system.entity.WebAdminUserEntity;
import d1.project.resource.system.mapper.RoleMapper;
import d1.project.resource.system.model.vm.RoleFindAllVm;
import d1.project.resource.system.model.vm.RoleInsertVm;
import d1.project.resource.system.model.vm.RoleUpdateVm;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:35
 */
@Service
public class RoleService {
    private final RoleMenuTreeDao roleMenuTreeDao;
    private final RoleDao roleDao;
    private final WebAdminUserDao webAdminUserDao;

    private final RoleUserService roleUserService;
    private final IOperationLogService iOperationLogService;

    private final RoleMapper mapper;

    public RoleService(RoleDao roleDao, RoleUserService roleUserService, WebAdminUserDao webAdminUserDao, IOperationLogService iOperationLogService, RoleMenuTreeDao roleMenuTreeDao) {
        this.roleDao = roleDao;
        this.mapper = Mappers.getMapper(RoleMapper.class);
        this.roleUserService = roleUserService;
        this.webAdminUserDao = webAdminUserDao;
        this.iOperationLogService = iOperationLogService;
        this.roleMenuTreeDao = roleMenuTreeDao;
    }

    /**
     * ??????
     *
     * @param model ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(RoleInsertVm model, HttpServletRequest request) throws DoValidException {

        RoleEntity role = mapper.dtoFormatIntoInsertEntity(model);
        TokenManager.getInstance().updateCreateIdAndTime(request, role);
        role.setId(BaseUtils.generate32Id());
        if (roleDao.existsByName(model.getName())) {
            throw new DoValidException("????????????????????????????????????????????????");
        }
        roleDao.save(role);
        iOperationLogService.insert(new OperationLog("????????????_??????", "??????", "????????????" + role.getName(), JSON.toJSONString(role), 1), request);
    }

    /**
     * ??????
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        if (Constants.CANNOT_DELETED_ROLE.contains(id)) {
            throw new DoValidException("????????????????????????");
        }
        RoleEntity role = roleDao.findById(id).orElseThrow(() -> new DoValidException("???????????????"));
        if (roleUserService.existsByRoleId(id)) {
            throw new DoValidException("????????????????????????????????????????????????");
        }
        roleDao.deleteById(id);
        roleUserService.deleteByRoleId(id);
        roleMenuTreeDao.deleteByRoleId(id);
        iOperationLogService.insert(new OperationLog("????????????_??????", "??????", "????????????" + role.getName(), JSON.toJSONString(role), 1), request);
    }

    /**
     * ??????
     *
     * @param model ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleUpdateVm model, HttpServletRequest request) throws DoValidException {
        String name = model.getName();
        String id = model.getId();

        if (roleDao.existsByNameAndIdNot(name, id)) {
            throw new DoValidException("????????????????????????????????????????????????");
        }

        RoleEntity role = roleDao.findById(id).orElseThrow(() -> new DoValidException("???????????????"));
        String oldName = role.getName();
        mapper.copyProperties(model, role);
        TokenManager.getInstance().updateUpdateIdAndTime(request, role);
        roleDao.save(role);
        /*???????????????????????????????????????????????????????????????*/
        List<RoleUserEntity> byRoleId = roleUserService.findByRoleId(id);
        List<RoleUserEntity> collect = byRoleId.stream().peek(roleUser -> {
            roleUser.setRoleName(name);
        }).collect(Collectors.toList());
        roleUserService.saveAll(collect);

        if (!oldName.equals(name)) {
            List<WebAdminUserEntity> collect1 = webAdminUserDao.findByRoleNameLike(oldName + "%").stream().peek(webAdminUser -> {
                String[] split = webAdminUser.getRoleName().split(",");
                List<String> strings = new ArrayList<>();
                for (String s : split) {
                    if (s.equals(oldName)) {
                        strings.add(role.getName());
                        continue;
                    }
                    strings.add(s);
                }
                webAdminUser.setRoleName(String.join(",", strings));
            }).collect(Collectors.toList());
            webAdminUserDao.saveAll(collect1);
        }

        iOperationLogService.insert(new OperationLog("????????????_??????", "??????", "????????????" + model.getName(), JSON.toJSONString(model), 1), request);
    }

    /**
     * ????????????
     *
     * @param model ??????
     */
    public Page<RoleEntity> findAll(RoleFindAllVm model) throws Exception {
        SpecificationBuilder<RoleEntity> builder = new SpecificationBuilder<>();
        Specification<RoleEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return roleDao.findAll(specification, builder.getPageable());
    }

    public List<RoleEntity> findAllList() {
        return roleDao.findAll();
    }

    /**
     * ??????
     *
     * @param id id
     */
    public Optional<RoleEntity> find(String id) {
        return roleDao.findById(id);
    }

}
