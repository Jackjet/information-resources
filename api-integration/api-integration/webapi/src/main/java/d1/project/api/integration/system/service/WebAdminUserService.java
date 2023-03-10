package d1.project.api.integration.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.pinyin.util.PinyinHelper;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.CacheManger;
import d1.framework.webapi.utils.SignInRetryLimitManager;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.service.EventBus;
import d1.project.api.integration.common.service.IWebAdminUserService;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.log.model.vm.UserLogRequestInsertVm;
import d1.project.api.integration.log.service.UserLogService;
import d1.project.api.integration.system.dao.WebAdminUserDao;
import d1.project.api.integration.system.entity.*;
import d1.project.api.integration.system.mapper.WebAdminUserMapper;
import d1.project.api.integration.system.model.MySignInResult;
import d1.project.api.integration.system.model.WebAdminUserExcelExport;
import d1.project.api.integration.system.model.WebAdminUserExcelImport;
import d1.project.api.integration.system.model.WebAdminUserVm;
import d1.project.api.integration.system.model.vm.*;
import net.dcrun.component.ehcache.IEhcacheService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 15:21
 */
@Service
public class WebAdminUserService implements IWebAdminUserService {
    private final static String authType = "webadmin";
    private final static String cannotDeleted = "admin";

    private final OrganizationUserService organizationUserService;
    private final OrganizationService organizationService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;
    private final WebAdminUserDao webAdminUserDao;
    private final UserLogService userLogService;

    private final WebAdminUserMapper mapper;

    public WebAdminUserService(WebAdminUserDao webAdminUserDao, UserLogService userLogService, OrganizationUserService organizationUserService, OrganizationService organizationService, RoleUserService roleUserService, RoleService roleService) {
        this.webAdminUserDao = webAdminUserDao;
        this.mapper = Mappers.getMapper(WebAdminUserMapper.class);
        this.userLogService = userLogService;
        this.organizationUserService = organizationUserService;
        this.organizationService = organizationService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
    }

    /**
     * ??????
     *
     * @param model ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(WebAdminUserInsertVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity webAdminUser = mapper.dtoFormatIntoInsertEntity(model);

        String roleId = model.getRoleId();
        String organizationId = model.getOrganizationId();

        webAdminUser.initInsert();
        TokenManager.getInstance().updateCreateIdAndTime(request, webAdminUser);
        /*????????????????????????*/
        if (!StringUtils.isEmpty(organizationId)) {
            OrganizationEntity organization = organizationService.find(organizationId).orElseThrow(() -> new DoValidException("???????????????"));
            organizationUserService.insert(new OrganizationUserEntity(webAdminUser.getId(), organizationId), request);
            webAdminUser.setOrganizationName(organization.getName());
        }

        /*????????????????????????*/
        if (StringUtils.isEmpty(roleId)) {
            roleId = "default";
        }
        RoleEntity role = roleService.find(roleId).orElseThrow(() -> new DoValidException("???????????????"));
        roleUserService.insert(new RoleUserEntity(role.getId(), role.getName(), webAdminUser), request);
        webAdminUser.setRoleName(role.getName());
        /*?????????????????????*/
        if (webAdminUserDao.existsByAccount(webAdminUser.getAccount())) {
            throw new DoValidException("????????????????????????????????????");
        }
        webAdminUserDao.save(webAdminUser);
        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUser), "????????????" + webAdminUser.getName(), "??????", 1), request);
    }


    @Transactional(rollbackFor = Exception.class)
    public void insertAll(List<WebAdminUserExcelImport> model, HttpServletRequest request) throws DoValidException {

        List<String> accountThrows = new ArrayList<>();
        List<String> nameThrows = new ArrayList<>();
        List<String> accountMatchesThrows = new ArrayList<>();
        List<String> organizationThrows = new ArrayList<>();
        List<String> roleThrows = new ArrayList<>();
        List<String> emailMatchesThrows = new ArrayList<>();
        List<String> phoneMatchesThrows = new ArrayList<>();
        /*??????????????????*/
        Map<String, String> organizationMap = organizationService.findAllList().stream().collect(Collectors.toMap(OrganizationEntity::getName, OrganizationEntity::getId, (s, s2) -> s));
        /*????????????*/
        Map<String, String> roleMap = roleService.findAllList().stream().collect(Collectors.toMap(RoleEntity::getName, RoleEntity::getId, (u, u2) -> u));

        for (WebAdminUserExcelImport webAdminUserExcelVm : model) {

            /*?????????????????????*/
            String account = webAdminUserExcelVm.getAccount();

            if (StringUtils.isEmpty(account)) {
                continue;
            }
            String name = webAdminUserExcelVm.getName();
            if (StringUtils.isEmpty(name)) {
                nameThrows.add(account);
                continue;
            }

            String organizationName = webAdminUserExcelVm.getOrganizationName();
            String roleName = webAdminUserExcelVm.getRoleName();
            String email = webAdminUserExcelVm.getEmail();
            String phone = webAdminUserExcelVm.getPhone();
            WebAdminUserEntity webAdminUser = mapper.dtoFormatIntoInsertEntity(webAdminUserExcelVm);
            webAdminUser.initInsert();
            webAdminUser.setId(BaseUtils.generate32Id());

            if (!StringUtils.isEmpty(account)) {
                if (!account.matches("^[\\w+]{1,20}$")) {
                    accountMatchesThrows.add(account);
                    continue;
                }
            }

            if (!StringUtils.isEmpty(phone)) {
                if (!phone.matches("^((13[0-9])|(14[5,6,7,9])|(15[^4])|(16[5,6])|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$")) {
                    phoneMatchesThrows.add(account);
                    continue;
                }
            }
            if (!StringUtils.isEmpty(organizationName)) {
                String organizationId = organizationMap.get(organizationName);
                if (StringUtils.isEmpty(organizationId)) {
                    organizationThrows.add(account);
                    continue;
                }
                organizationUserService.insert(new OrganizationUserEntity(webAdminUser.getId(), organizationId), request);
                webAdminUser.setOrganizationName(organizationName);
            }
            if (!StringUtils.isEmpty(roleName)) {
                String roleId = roleMap.get(roleName);
                if (StringUtils.isEmpty(roleId)) {
                    roleThrows.add(account);
                    continue;
                }
                roleUserService.insert(new RoleUserEntity(roleId, roleName, webAdminUser), request);
                webAdminUser.setRoleName(roleName);
            } else {
                roleUserService.insert(new RoleUserEntity("default", "????????????", webAdminUser), request);
                webAdminUser.setRoleName("????????????");
            }

            if (!StringUtils.isEmpty(email)) {
                if (!email.matches("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$")) {
                    emailMatchesThrows.add(account);
                    continue;
                }
            }

            TokenManager.getInstance().updateCreateIdAndTime(request, webAdminUser);

            if (webAdminUserDao.existsByAccount(account)) {
                accountThrows.add(account);
                continue;
            }
            webAdminUserDao.save(webAdminUser);

            userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUser), "????????????" + webAdminUser.getAccount(), "??????", 1), request);
        }
        if (!accountThrows.isEmpty() || !organizationThrows.isEmpty() || !roleThrows.isEmpty() || !accountMatchesThrows.isEmpty() || !emailMatchesThrows.isEmpty() || !phoneMatchesThrows.isEmpty() || !nameThrows.isEmpty()) {
            LinkedHashMap<String, String> throwsMap = new LinkedHashMap<>();

            throwsMap.put("????????????", String.join(",", accountThrows));
            throwsMap.put("?????????????????????", String.join(",", accountMatchesThrows));
            throwsMap.put("?????????????????????", String.join(",", nameThrows));
            throwsMap.put("????????????????????????", String.join(",", phoneMatchesThrows));
            throwsMap.put("?????????????????????", String.join(",", organizationThrows));
            throwsMap.put("?????????????????????", String.join(",", roleThrows));
            throwsMap.put("?????????????????????", String.join(",", emailMatchesThrows));


            throw new DoValidException(JSON.toJSONString(throwsMap));
        }

    }


    /**
     * ??????
     *
     * @param ids id??????
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<String> ids, HttpServletRequest request) throws DoValidException {
        if (ids.contains(cannotDeleted)) {
            throw new DoValidException("????????????????????????");
        }
        List<WebAdminUserEntity> allById = webAdminUserDao.findAllById(ids);
        List<String> collect = new ArrayList<>();
        List<String> userIds = new ArrayList<>();

        allById.forEach(webAdminUser -> {
            collect.add(webAdminUser.getName());
            userIds.add(webAdminUser.getId());
        });
        organizationUserService.deleteByUserIdIn(userIds);
        roleUserService.deleteByUserIdIn(userIds);
        EventBus.getInstance().fire(Constants.USER_DELETE_EVENT, userIds);
        webAdminUserDao.deleteAll(allById);

        userLogService.insert(new UserLogRequestInsertVm(JSONArray.toJSONString(allById), "????????????" + String.join(",", collect), "??????", 1), request);
    }

    /**
     * ??????
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        if (cannotDeleted.equals(id)) {
            throw new DoValidException("admin??????????????????");
        }
        Optional<WebAdminUserEntity> byId = webAdminUserDao.findById(id);
        if (byId.isPresent()) {
            WebAdminUserEntity webAdminUser = byId.get();
            organizationUserService.deleteByUserIdIn(Collections.singletonList(id));
            roleUserService.deleteByUserIdIn(Collections.singletonList(id));
            EventBus.getInstance().fire(Constants.USER_DELETE_EVENT, Collections.singletonList(id));
            webAdminUserDao.deleteAll(Collections.singletonList(webAdminUser));
            deleteWebAdminUserToken(webAdminUser);

            userLogService.insert(new UserLogRequestInsertVm(JSONArray.toJSONString(webAdminUser), "????????????" + webAdminUser.getAccount(), "??????", 1), request);

        }

    }

    private void deleteWebAdminUserToken(WebAdminUserEntity webAdminUser) {
        WebAdminUserVm webAdminUserVm = getWebAdminUserVm(webAdminUser);
        IEhcacheService cache = CacheManger.getInstance().getCache();
        cache.traversal((k, v) -> {
            if (k.contains("token/")) {
                JSONObject jsonObject = JSONObject.parseObject(v);
                if (jsonObject != null) {
                    Object id = jsonObject.get("id");
                    if (id != null && id.toString().equals(webAdminUserVm.getId())) {
                        cache.removeData(k);
                    }
                }

            }
        });
    }

    /**
     * ??????
     *
     * @param model ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(WebAdminUserUpdateVm model, HttpServletRequest request) throws DoValidException {
        if (cannotDeleted.equals(model.getId())) {
            throw new DoValidException("admin??????????????????");
        }
        WebAdminUserEntity webAdminUserEntity = findById(model.getId()).orElseThrow(() -> new DoValidException("???????????????"));
        mapper.copyProperties(model, webAdminUserEntity);
        /*?????????????????????*/
        TokenManager.getInstance().updateUpdateIdAndTime(request, webAdminUserEntity);

        String roleId = model.getRoleId();
        String organizationId = model.getOrganizationId();
        /*????????????????????????????????????*/
        if (!StringUtils.isEmpty(roleId)) {
            RoleEntity role = roleService.find(roleId).orElseThrow(() -> new DoValidException("???????????????"));
            roleUserService.deleteByUserId(model.getId());
            roleUserService.insert(new RoleUserEntity(role.getId(), role.getName(), webAdminUserEntity), request);
            webAdminUserEntity.setRoleName(role.getName());
        }
        /*??????????????????????????????????????????*/
        if (!StringUtils.isEmpty(organizationId)) {
            OrganizationEntity organization = organizationService.find(organizationId).orElseThrow(() -> new DoValidException("???????????????"));
            organizationUserService.deleteByUserId(model.getId());
            organizationUserService.insert(new OrganizationUserEntity(webAdminUserEntity.getId(), model.getOrganizationId()), request);
            webAdminUserEntity.setOrganizationName(organization.getName());
        }
        webAdminUserEntity.setPinyin(PinyinHelper.toPinyin(webAdminUserEntity.getName()));
        webAdminUserDao.save(webAdminUserEntity);

        /*????????????*/
        updateWebAdminUserToken(webAdminUserEntity);
        userLogService.insert(new UserLogRequestInsertVm(JSONArray.toJSONString(model), "????????????" + webAdminUserEntity.getName(), "??????", 1), request);
    }

    /**
     * ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(WebAdminUserUpdatePasswordVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity user = webAdminUserDao.findByIdAndPassword(model.getId(), model.getOldPassword()).orElseThrow(() -> new DoValidException("???????????????"));
        user.setPassword(model.getNewPassword());
        webAdminUserDao.save(user);

        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(user), "??????????????????" + user.getName(), "????????????", 1), request);
    }

    /**
     * ??????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePasswordReset(WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException {
        List<String> ids = model.getIds();
        if (ids.contains(cannotDeleted)) {
            throw new DoValidException("????????????????????????????????????");
        }
        List<WebAdminUserEntity> webAdminUsers = webAdminUserDao.findAllById(ids).stream().peek(webAdminUser -> webAdminUser.setPassword("e10adc3949ba59abbe56e057f20f883e")).collect(Collectors.toList());
        webAdminUserDao.saveAll(webAdminUsers);
        /*???????????????????????????????????????*/
        webAdminUsers.forEach(this::deleteWebAdminUserToken);
        List<String> collect = webAdminUsers.stream().map(WebAdminUserEntity::getAccount).collect(Collectors.toList());
        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUsers), "??????????????????" + String.join(",", collect), "????????????", 1), request);
    }

    /**
     * ??????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAllEnable(WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws DoValidException {
        List<String> ids = model.getIds();
        if (ids.contains(cannotDeleted)) {
            throw new DoValidException("??????????????????????????????/??????");
        }
        List<WebAdminUserEntity> webAdminUsers = webAdminUserDao.findAllById(ids).stream().peek(webAdminUser -> webAdminUser.setEnable(model.isEnable())).collect(Collectors.toList());
        webAdminUserDao.saveAll(webAdminUsers);
        List<String> collect = webAdminUsers.stream().map(WebAdminUserEntity::getAccount).collect(Collectors.toList());
        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUsers), model.isEnable() ? "????????????" : "????????????" + String.join(",", collect), "????????????", 1), request);
    }

    /**
     * ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEnable(WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws DoValidException {
        String id = model.getId();
        if (id.equals(cannotDeleted)) {
            throw new DoValidException("????????????????????????");
        }
        WebAdminUserEntity webAdminUserEntity = webAdminUserDao.findById(id).orElseThrow(() -> new DoValidException("???????????????"));
        if (webAdminUserEntity != null) {
            boolean enable = !webAdminUserEntity.isEnable();
            webAdminUserEntity.setEnable(enable);
            webAdminUserDao.save(webAdminUserEntity);
            if (!enable) {
                deleteWebAdminUserToken(webAdminUserEntity);
            }
            String account = webAdminUserEntity.getAccount();
            userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUserEntity), enable ? "????????????" + account : "????????????" + account, "????????????", 1), request);
        }

    }

    /**
     * ????????????token??????
     */
    private void updateWebAdminUserToken(WebAdminUserEntity user) {
        WebAdminUserVm webAdminUserVm = getWebAdminUserVm(user);
        IEhcacheService cache = CacheManger.getInstance().getCache();
        cache.traversal((k, v) -> {
            if (k.contains("token/")) {
                JSONObject jsonObject1 = JSONObject.parseObject(v);
                if (jsonObject1 != null) {
                    Object id = jsonObject1.get("id");
                    if (!StringUtils.isEmpty(id) && id.equals(webAdminUserVm.getId())) {
                        JSONObject.parseObject("");
                        JSONObject jsonObject = (JSONObject) JSON.toJSON(webAdminUserVm);
                        jsonObject.put("type", authType);
                        cache.setExpireString(k, jsonObject.toString(), Constants.TOKEN_EXPIRE_TIME);
                    }
                }

            }
        });
    }

    public Page<WebAdminUserVm> findAll(WebAdminUserFindAllVm model) throws Exception {
        SpecificationBuilder<WebAdminUserEntity> builder = new SpecificationBuilder<>();
        Specification<WebAdminUserEntity> specification = getSpecificationBuild(model, builder);
        Page<WebAdminUserEntity> all = webAdminUserDao.findAll(specification, builder.getPageable());
        List<WebAdminUserVm> webAdminUserVms = mapper.entityListFormatIntoDtoList(all.getContent());
        return new PageImpl<>(webAdminUserVms, all.getPageable(), all.getTotalElements());
    }

    public List<WebAdminUserExcelExport> findAllList(WebAdminUserFindAllVm model) throws Exception {
        SpecificationBuilder<WebAdminUserEntity> builder = new SpecificationBuilder<>();
        Specification<WebAdminUserEntity> specification = getSpecificationBuild(model, builder);
        List<WebAdminUserEntity> all = webAdminUserDao.findAll(specification);
        return mapper.entityListFormatIntoExcelDtoList(all);
    }


    private Specification<WebAdminUserEntity> getSpecificationBuild(WebAdminUserFindAllVm model, SpecificationBuilder<WebAdminUserEntity> builder) throws Exception {
        return builder.init((JSONObject) JSON.toJSON(model))
                .sContain("name", "name")
                .sContain("account", "account")
                .sContain("phone", "phone")
                .sContain("organizationName", "organizationName")
                .sEqual("sex", "sex")
                .dOrder("createTime").build();
    }

    public WebAdminUserVm find(String id) throws DoValidException {
        return getWebAdminUserVm(webAdminUserDao.findById(id).orElseThrow(() -> new DoValidException("???????????????")));
    }

    public Optional<WebAdminUserEntity> findById(String id) {
        return webAdminUserDao.findById(id);
    }

    /**
     * ??????
     *
     * @param username ??????
     * @param password ??????
     */
    public MySignInResult login(String username, String password, String x_index, String checkMoveId, HttpServletRequest request) throws DoValidException {
        /*??????????????????*/
        checkMove(x_index, checkMoveId);
        /*?????????????????????*/
        SignInRetryLimitManager instance = SignInRetryLimitManager.getInstance();
        if (instance.verifyIsLocked(username)) {
            throw new DoValidException("???????????????????????????????????????????????????");
        }
        WebAdminUserEntity user = webAdminUserDao.findByAccount(username).orElseThrow(() -> new DoValidException("????????????????????????"));
        if (!user.getPassword().equals(password)) {
            /*???????????????Pwd??????*/
            instance.signInWithWrongPwd(username);
            throw new DoValidException("????????????????????????");
        }

        if (!user.isEnable()) {
            throw new DoValidException("???????????????????????????????????????????????????");
        }

        instance.signInSuccess(username);
        /*???????????????->?????????*/
        WebAdminUserVm webAdminUserVm = getWebAdminUserVm(user);

        user.setLastSignInTime(Calendar.getInstance());
        /*????????????*/
        String token = TokenManager.getInstance().createToken(authType, (JSONObject) JSON.toJSON(webAdminUserVm), Constants.TOKEN_EXPIRE_TIME);
        MySignInResult signInResult = new MySignInResult(webAdminUserVm, token);

        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(signInResult), "??????" + user.getAccount() + "??????", "??????", 1), user, request);
        return signInResult;
    }

    /**
     * ????????????
     */
    private void checkMove(String x_index, String checkMoveId) throws DoValidException {
        IEhcacheService cache = CacheManger.getInstance().getCache();
        if (ObjectUtil.isNull(x_index) || ObjectUtil.isNull(checkMoveId)) {
            throw new DoValidException("????????????????????????");
        } else {
            // ??????readis??????????????????
            String x_index_orld = cache.getString(checkMoveId);
            if (ObjectUtil.isNull(x_index_orld)) {
                throw new DoValidException("????????????????????????");
            } else {
                Double dMoveLength = Double.valueOf(x_index_orld);
                Double xWidth = Double.valueOf(x_index);
                if (Math.abs(xWidth - dMoveLength) > 10) {
                    //???????????????
                    throw new DoValidException("???????????????????????????");
                } else {
                    //????????????
                    cache.removeData(checkMoveId);
                }
            }
        }
    }

    /**
     * ?????????????????????
     */
    private WebAdminUserVm getWebAdminUserVm(WebAdminUserEntity user) {
        WebAdminUserVm webAdminUserVm = mapper.entityFormatIntoDto(user);
        String id1 = user.getId();
        webAdminUserVm.setRoleId(roleUserService.findByUserId(id1).stream().map(RoleUserEntity::getRoleId).collect(Collectors.joining(",")));
        webAdminUserVm.setOrganizationId(organizationUserService.findByUserId(id1).stream().map(OrganizationUserEntity::getOrganizationId).collect(Collectors.joining(",")));
        return webAdminUserVm;
    }

    /**
     * ????????????
     */
    public void singOut(HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        userLogService.insert(new UserLogRequestInsertVm(userByToken.toJSONString(), "??????" + userByToken.getString("account") + "????????????", "????????????", 1), request);
        TokenManager.getInstance().removeToken(request);
    }


    @Override
    public List<WebAdminUserEntity> findAllByName(String name) {
        return this.webAdminUserDao.findAllByName(name);
    }
}
