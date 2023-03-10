package d1.project.resourcesharingmgmt.system.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.pinyin.util.PinyinHelper;
import d1.framework.authz.configuration.AuthzConfig;
import d1.framework.authz.service.AuthzMyInfoService;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.CacheManger;
import d1.framework.webapi.utils.SignInRetryLimitManager;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.service.EventBus;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.log.model.vm.UserLogRequestInsertVm;
import d1.project.resourcesharingmgmt.log.service.UserLogService;
import d1.project.resourcesharingmgmt.system.dao.WebAdminUserDao;
import d1.project.resourcesharingmgmt.system.entity.*;
import d1.project.resourcesharingmgmt.system.mapper.WebAdminUserMapper;
import d1.project.resourcesharingmgmt.system.model.MySignInResult;
import d1.project.resourcesharingmgmt.system.model.WebAdminUserExcelBase;
import d1.project.resourcesharingmgmt.system.model.WebAdminUserExcelImport;
import d1.project.resourcesharingmgmt.system.model.WebAdminUserVm;
import d1.project.resourcesharingmgmt.system.model.vm.*;
import d1.project.resourcesharingmgmt.system.utils.ExcelTitleHandler;
import net.dcrun.component.ehcache.IEhcacheService;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 15:21
 */
public class WebAdminUserServiceImpl implements IWebAdminUserService {
    private final static String authType = "webadmin";
    private final static String cannotDeleted = "admin";

    private final OrganizationUserService organizationUserService;
    private final OrganizationService organizationService;
    private final RoleUserService roleUserService;
    private final RoleService roleService;
    private final WebAdminUserDao webAdminUserDao;
    private final UserLogService userLogService;
    private final BaseService baseService;
    private final SignInRetryLimitManager signInRetryLimitManager;
    private final SSOService ssoService;

    private boolean isShowImageCaptcha;
    private final WebAdminUserMapper mapper;

    @Autowired
    private AuthzConfig authzConfig;
    @Autowired
    private AuthzMyInfoService authzMyInfoService;

    public WebAdminUserServiceImpl(WebAdminUserDao webAdminUserDao,
                                   UserLogService userLogService,
                                   OrganizationUserService organizationUserService,
                                   OrganizationService organizationService,
                                   RoleUserService roleUserService,
                                   RoleService roleService,
                                   BaseService baseService,
                                   SSOService ssoService) {
        this.webAdminUserDao = webAdminUserDao;
        this.mapper = Mappers.getMapper(WebAdminUserMapper.class);
        this.userLogService = userLogService;
        this.organizationUserService = organizationUserService;
        this.organizationService = organizationService;
        this.roleUserService = roleUserService;
        this.roleService = roleService;
        this.signInRetryLimitManager = SignInRetryLimitManager.getInstance();
        this.baseService = baseService;
        this.ssoService = ssoService;
    }

    @Override
    public void setShowImageCaptcha(boolean isShowImageCaptcha) {
        this.isShowImageCaptcha = isShowImageCaptcha;
    }

    /**
     * ??????
     *
     * @param model ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebAdminUserEntity insert(WebAdminUserInsertVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity webAdminUser = mapper.dtoFormatIntoInsertEntity(model);

        String roleId = model.getRoleId();
        String organizationId = model.getOrganizationId();

        webAdminUser.initInsert();
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        if (userByToken != null) {
            TokenManager.getInstance().updateCreateIdAndTime(request, webAdminUser);
        }

        /*????????????????????????*/
        if (!StringUtils.isEmpty(organizationId)) {
            OrganizationEntity organization = organizationService.find(organizationId).orElseThrow(() -> new DoValidException("???????????????"));
            organizationUserService.insert(new OrganizationUserEntity(webAdminUser.getId(), organizationId), request);
            webAdminUser.setOrganizationName(organization.getNameLink());
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
        WebAdminUserEntity webAdminUserEntity = webAdminUserDao.save(webAdminUser);
        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUser), "????????????" + webAdminUser.getName(), "??????", 1), request);
        return webAdminUserEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertAll(List<WebAdminUserExcelImport> model, HttpServletRequest request) throws DoValidException {
        insertAll(model, request, null);
    }

    /**
     * ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertAll(List<WebAdminUserExcelImport> model, HttpServletRequest request, Function<WebAdminUserEntity, Boolean> accountFunction) throws DoValidException {
        List<String> accountThrows = new ArrayList<>();
        List<String> ssoAccountThrows = new ArrayList<>();
        List<String> nameThrows = new ArrayList<>();
        List<String> accountMatchesThrows = new ArrayList<>();
        List<String> organizationThrows = new ArrayList<>();
        List<String> roleThrows = new ArrayList<>();
        List<String> emailMatchesThrows = new ArrayList<>();
        List<String> phoneMatchesThrows = new ArrayList<>();
        List<String> idCardMatchesThrows = new ArrayList<>();
        /*??????????????????*/
        Map<String, String> organizationMap = organizationService.findAllList().stream().collect(Collectors.toMap(OrganizationEntity::getNameLink, OrganizationEntity::getId, (s, s2) -> s));
        /*????????????*/
        Map<String, String> roleMap = roleService.findAllList().stream().collect(Collectors.toMap(RoleEntity::getName, RoleEntity::getId, (u, u2) -> u));

        for (WebAdminUserExcelImport webAdminUserExcelVm : model) {
            WebAdminUserEntity webAdminUser = new WebAdminUserEntity();

            /*?????????????????????*/
            String account = webAdminUserExcelVm.getAccount();
            String name = webAdminUserExcelVm.getName();
            String organizationName = webAdminUserExcelVm.getOrganizationName();
            String roleName = webAdminUserExcelVm.getRoleName();
            String email = webAdminUserExcelVm.getEmail();
            String phone = webAdminUserExcelVm.getPhone();
            String sex = webAdminUserExcelVm.getSex();
            String idCard = webAdminUserExcelVm.getIdCard();

            if (webAdminUserDao.existsByAccount(account)) {
                webAdminUser = webAdminUserDao.findByAccount(account).get();
                webAdminUser.setName(name);
                webAdminUser.setPhone(phone);
                webAdminUser.setSex(sex.equals("???") ? 0 : 1);
                webAdminUser.setIdCard(idCard);
                webAdminUser.setOrganizationName(organizationName);
                webAdminUser.setRoleName(roleName);
                webAdminUser.setPinyin(PinyinHelper.toPinyin(name));
            } else {
                webAdminUser.initInsert();
                webAdminUser = mapper.dtoFormatIntoInsertEntity(webAdminUserExcelVm);
                webAdminUser.setId(BaseUtils.generate32Id());
                webAdminUser.setEnable(true);
                webAdminUser.setPinyin(PinyinHelper.toPinyin(name));
            }

            if (StringUtils.isEmpty(account)) {
                accountThrows.add("????????????");
                continue;
            }
            if (StringUtils.isEmpty(name)) {
                nameThrows.add(account + ":????????????");
                continue;
            }
            if (!StringUtils.isEmpty(account)) {
                if (!account.matches("^[\\w+]{1,20}$")) {
                    accountMatchesThrows.add(account);
                    continue;
                }
            }
            if (!StringUtils.isEmpty(phone)) {
                if (!phone.matches("^(((13[0-9])|(14[5,6,7,9])|(15[^4])|(16[5,6])|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8})|(0\\d{2,3}-\\d{7,8})$")) {
                    phoneMatchesThrows.add(account + ":" + phone);
                    continue;
                }
            }

            if (!StringUtils.isEmpty(idCard)) {
                if (!idCard.matches("^([1-6][1-9]|50)\\d{4}(18|19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")) {
                    idCardMatchesThrows.add(account + ":" + idCard);
                    continue;
                }
            }

            if (!StringUtils.isEmpty(email)) {
                if (!email.matches("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$")) {
                    emailMatchesThrows.add(account + ":" + email);
                    continue;
                }
            }

            if (!StringUtils.isEmpty(organizationName)) {
                String organizationId = organizationMap.get(organizationName);
                if (StringUtils.isEmpty(organizationId)) {
                    organizationThrows.add(account + ":" + organizationName);
                    continue;
                }
                //????????????????????????
                organizationUserService.deleteByUserId(webAdminUser.getId());
                OrganizationUserEntity model1 = new OrganizationUserEntity(webAdminUser.getId(), organizationId);
                organizationUserService.insert(model1, request);
                webAdminUser.setOrganizationName(organizationName);
            }
            if (!StringUtils.isEmpty(roleName)) {
                String roleId = roleMap.get(roleName);
                if (StringUtils.isEmpty(roleId)) {
                    roleThrows.add(account + ":" + roleName);
                    continue;
                }
                //????????????????????????
                roleUserService.deleteByUserId(webAdminUser.getId());
                roleUserService.insert(new RoleUserEntity(roleId, roleName, webAdminUser), request);
                webAdminUser.setRoleName(roleName);
            } else {
                roleUserService.insert(new RoleUserEntity("default", "????????????", webAdminUser), request);
                webAdminUser.setRoleName("????????????");
            }

            TokenManager.getInstance().updateCreateIdAndTime(request, webAdminUser);
            //????????????
            try {
                if (ssoService.existsFindByName(account.toLowerCase())) {
                    UserRepresentation ssoUser = ssoService.findByName(account.toLowerCase());
                    //??????SSO????????????????????????SSOID??????????????????
                    webAdminUser.setSsoId(ssoUser.getId());
                    ssoService.updateUser(webAdminUser);
                } else {
                    ssoService.createUser(webAdminUser);
                }
            } catch (Exception e) {
                ssoAccountThrows.add(account);
            }

            webAdminUserDao.save(webAdminUser);

            userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUser), "????????????" + webAdminUser.getAccount(), "??????", 1), request);
        }
        if (!accountThrows.isEmpty() || !organizationThrows.isEmpty() || !roleThrows.isEmpty() || !accountMatchesThrows.isEmpty() || !emailMatchesThrows.isEmpty() || !phoneMatchesThrows.isEmpty() || !idCardMatchesThrows.isEmpty() || !nameThrows.isEmpty() || !ssoAccountThrows.isEmpty()) {
            LinkedHashMap<String, String> throwsMap = new LinkedHashMap<>();

            if (!accountThrows.isEmpty()) {
                throwsMap.put("????????????", String.join(",", accountThrows));
            }
            if (!accountMatchesThrows.isEmpty()) {
                throwsMap.put("?????????????????????", String.join(",", accountMatchesThrows));
            }
            if (!nameThrows.isEmpty()) {
                throwsMap.put("?????????????????????", String.join(",", nameThrows));
            }
            if (!phoneMatchesThrows.isEmpty()) {
                throwsMap.put("????????????????????????", String.join(",", phoneMatchesThrows));
            }
            if (!idCardMatchesThrows.isEmpty()) {
                throwsMap.put("???????????????????????????", String.join(",", idCardMatchesThrows));
            }
            if (!organizationThrows.isEmpty()) {
                throwsMap.put("?????????????????????", String.join(",", organizationThrows));
            }
            if (!roleThrows.isEmpty()) {
                throwsMap.put("?????????????????????", String.join(",", roleThrows));
            }
            if (!emailMatchesThrows.isEmpty()) {
                throwsMap.put("?????????????????????", String.join(",", emailMatchesThrows));
            }

            if (!ssoAccountThrows.isEmpty()) {
                throwsMap.put("?????????????????????", String.join(",", ssoAccountThrows));
            }

            throw new DoValidException(JSON.toJSONString(throwsMap));
        }

    }


    /**
     * ??????
     *
     * @param ids id??????
     */
    @Override
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebAdminUserEntity delete(String id, HttpServletRequest request) throws DoValidException {
        if (cannotDeleted.equals(id)) {
            throw new DoValidException("admin??????????????????");
        }
        WebAdminUserEntity webAdminUser = webAdminUserDao.findById(id).orElseThrow(() -> new DoValidException("???????????????"));
        organizationUserService.deleteByUserIdIn(Collections.singletonList(id));
        roleUserService.deleteByUserIdIn(Collections.singletonList(id));
        EventBus.getInstance().fire(Constants.USER_DELETE_EVENT, Collections.singletonList(id));
        webAdminUserDao.deleteAll(Collections.singletonList(webAdminUser));
        deleteWebAdminUserToken(webAdminUser);

        userLogService.insert(new UserLogRequestInsertVm(JSONArray.toJSONString(webAdminUser), "????????????" + webAdminUser.getAccount(), "??????", 1), request);

        return webAdminUser;
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebAdminUserEntity update(WebAdminUserUpdateVm model, HttpServletRequest request) throws DoValidException {
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
            webAdminUserEntity.setOrganizationName(organization.getNameLink());
        }
        webAdminUserEntity.setPinyin(PinyinHelper.toPinyin(webAdminUserEntity.getName()));
        webAdminUserDao.save(webAdminUserEntity);

        /*????????????*/
        updateWebAdminUserToken(webAdminUserEntity);
        userLogService.insert(new UserLogRequestInsertVm(JSONArray.toJSONString(model), "????????????" + webAdminUserEntity.getName(), "??????", 1), request);
        return webAdminUserEntity;
    }

    public void save(WebAdminUserEntity user) {
        webAdminUserDao.save(user);
    }

    /**
     * ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebAdminUserEntity updatePassword(WebAdminUserUpdatePasswordVm model, HttpServletRequest request) throws DoValidException {
        WebAdminUserEntity user = webAdminUserDao.findByIdAndPassword(model.getId(), model.getOldPassword()).orElseThrow(() -> new DoValidException("???????????????"));
        user.setPassword(BaseUtils.getMD5String(model.getNewPassword()));
        webAdminUserDao.save(user);

        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(user), "??????????????????" + user.getName(), "????????????", 1), request);
        return user;
    }

    /**
     * ??????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WebAdminUserEntity> updatePasswordReset(WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException {
        List<String> ids = model.getIds();
        if (ids.contains(cannotDeleted)) {
            throw new DoValidException("????????????????????????????????????");
        }
        List<WebAdminUserEntity> webAdminUsers = webAdminUserDao.findAllById(ids).stream().peek(webAdminUser -> webAdminUser.setPassword("ada6d861e78a32f5eb3ffc402b7aa4a0")).collect(Collectors.toList());
        webAdminUserDao.saveAll(webAdminUsers);
        /*???????????????????????????????????????*/
        webAdminUsers.forEach(this::deleteWebAdminUserToken);
        List<String> collect = webAdminUsers.stream().map(WebAdminUserEntity::getAccount).collect(Collectors.toList());
        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUsers), "??????????????????" + String.join(",", collect), "????????????", 1), request);
        return webAdminUsers;
    }

    /**
     * ??????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WebAdminUserEntity> updateAllEnable(WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws Exception {
        List<String> ids = model.getIds();
        if (ids.contains(cannotDeleted)) {
            throw new DoValidException("??????????????????????????????/??????");
        }
        List<WebAdminUserEntity> webAdminUsers = webAdminUserDao.findAllById(ids).stream().peek(webAdminUser -> webAdminUser.setEnable(model.isEnable())).collect(Collectors.toList());
        webAdminUserDao.saveAll(webAdminUsers);
        List<String> collect = webAdminUsers.stream().map(WebAdminUserEntity::getAccount).collect(Collectors.toList());
        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUsers), model.isEnable() ? "????????????" : "????????????" + String.join(",", collect), "????????????", 1), request);

        return webAdminUsers;
    }

    /**
     * ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WebAdminUserEntity updateEnable(WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws Exception {
        String id = model.getId();
        if (id.equals(cannotDeleted)) {
            throw new DoValidException("????????????????????????");
        }
        WebAdminUserEntity webAdminUserEntity = webAdminUserDao.findById(id).orElseThrow(() -> new DoValidException("???????????????"));
        boolean enable = !webAdminUserEntity.isEnable();
        webAdminUserEntity.setEnable(enable);
        webAdminUserDao.save(webAdminUserEntity);
        if (!enable) {
            deleteWebAdminUserToken(webAdminUserEntity);
        }
        String account = webAdminUserEntity.getAccount();
        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(webAdminUserEntity), enable ? "????????????" + account : "????????????" + account, "????????????", 1), request);

        return webAdminUserEntity;
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

    @Override
    public Page<WebAdminUserVm> findAll(WebAdminUserFindAllVm model) throws Exception {
        SpecificationBuilder<WebAdminUserEntity> builder = new SpecificationBuilder<>();
        Specification<WebAdminUserEntity> specification = getSpecificationBuild(model, builder);
        Page<WebAdminUserEntity> all = webAdminUserDao.findAll(specification, builder.getPageable());
        List<WebAdminUserVm> webAdminUserVms = mapper.entityListFormatIntoDtoList(all.getContent());
        return new PageImpl<>(webAdminUserVms, all.getPageable(), all.getTotalElements());
    }

    @Override
    public List<WebAdminUserExcelBase> findAllList(WebAdminUserFindAllVm model) throws Exception {
        SpecificationBuilder<WebAdminUserEntity> builder = new SpecificationBuilder<>();
        Specification<WebAdminUserEntity> specification = getSpecificationBuild(model, builder);
        List<WebAdminUserEntity> all = webAdminUserDao.findAll(specification);
        return mapper.entityListFormatIntoExcelDtoList(all);
    }

    @Override
    public List<WebAdminUserEntity> findAllList() throws Exception {
        return webAdminUserDao.findAll();
    }

    @Override
    public List<WebAdminUserExcelBase> findAllByIds(List<String> ids) {
        List<WebAdminUserEntity> all = webAdminUserDao.findAllById(ids);
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

    @Override
    public WebAdminUserVm find(String id) throws DoValidException {
        if ("admin".equals(id)) {
            return getWebAdminUserVm(webAdminUserDao.findByAccount("admin").orElseThrow(() -> new DoValidException("???????????????")));
        } else {
            return getWebAdminUserVm(webAdminUserDao.findById(id).orElseThrow(() -> new DoValidException("???????????????")));
        }

    }

    @Override
    public Optional<WebAdminUserEntity> findById(String id) {
        return webAdminUserDao.findById(id);
    }


    @Override
    public MySignInResult login(String username, String password, String xWidth, String checkMoveId, HttpServletRequest request) throws DoValidException {
        if (isShowImageCaptcha) {
            baseService.checkMove(xWidth, checkMoveId);
        }
        if (!authzConfig.isEnabled()) {
            if (StringUtils.isEmpty(username)) {
                throw new DoValidException("?????????????????????");
            }
            if (StringUtils.isEmpty(password)) {
                throw new DoValidException("??????????????????");
            }
            return login(username, password, request);
        } else {
            return ssoLogin(request);
        }
    }

    /**
     * ??????
     */
    protected MySignInResult ssoLogin(HttpServletRequest request) throws DoValidException {
        String userInfo = "";
        try {
            userInfo = authzMyInfoService.getCurrentUser(request);
        } catch (Exception e) {
            throw new DoValidException("?????????????????????" + e.getMessage());
        }

        WebAdminUserEntity entity;
        JSONObject jsonObject = JSONObject.parseObject(userInfo);
        String account = jsonObject.getString("preferred_username");
        if ("admin".equals(account)) {
            entity = webAdminUserDao.findByAccount(account).orElse(null);
        } else {
            String sub = jsonObject.getString("sub");
            entity = webAdminUserDao.findFirstBySsoId(sub);
            //??????????????????????????????
            if (entity == null) {
                Optional<WebAdminUserEntity> userOptional = webAdminUserDao.findByAccount(account);
                if(userOptional.isPresent()){
                    entity = userOptional.get();
                    entity.setSsoId(sub);
                    webAdminUserDao.save(entity);
                }else{
                    addSSOUser(jsonObject, request);
                }
            }
        }
        WebAdminUserVm webAdminUserVm = getWebAdminUserVm(entity);

        /*????????????*/
        String token = TokenManager.getInstance().createToken(authType, (JSONObject) JSON.toJSON(webAdminUserVm), Constants.TOKEN_EXPIRE_TIME);
        MySignInResult signInResult = new MySignInResult(webAdminUserVm, token);

        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(signInResult), "??????" + entity.getAccount() + "??????", "??????", 1), entity, request);
        return signInResult;
    }

    @Override
    public MySignInResult ssoLogin(String accessToken, HttpServletRequest request) throws DoValidException {
        return null;
    }

    /**
     * ??????
     *
     * @param username ??????
     * @param password ??????
     */
    protected MySignInResult login(String username, String password, HttpServletRequest request) throws DoValidException {
        /*?????????????????????*/
        if (signInRetryLimitManager.verifyIsLocked(username)) {
            throw new DoValidException("???????????????????????????????????????????????????");
        }
        WebAdminUserEntity user = webAdminUserDao.findByAccount(username).orElseThrow(() -> new DoValidException("????????????????????????"));
        if (!user.getPassword().equals(password)) {
            /*???????????????Pwd??????*/
            signInRetryLimitManager.signInWithWrongPwd(username);
            throw new DoValidException("????????????????????????");
        }

        if (!user.isEnable()) {
            throw new DoValidException("???????????????????????????????????????????????????");
        }

        signInRetryLimitManager.signInSuccess(username);

        WebAdminUserVm webAdminUserVm = getWebAdminUserVm(user);

        user.setLastSignInTime(Calendar.getInstance());
        /*????????????*/
        String token = TokenManager.getInstance().createToken(authType, (JSONObject) JSON.toJSON(webAdminUserVm), Constants.TOKEN_EXPIRE_TIME);
        MySignInResult signInResult = new MySignInResult(webAdminUserVm, token);

        userLogService.insert(new UserLogRequestInsertVm(JSON.toJSONString(signInResult), "??????" + user.getAccount() + "??????", "??????", 1), user, request);
        return signInResult;
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

    public Optional<WebAdminUserEntity> findByAccount(String account) {
        return webAdminUserDao.findByAccount(account);
    }

    /**
     * ????????????
     */
    @Override
    public void singOut(HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        userLogService.insert(new UserLogRequestInsertVm(userByToken.toJSONString(), "??????" + userByToken.getString("account") + "????????????", "????????????", 1), request);
        TokenManager.getInstance().removeToken(request);
    }

    /**
     * ??????excel
     */
    @Override
    public void easyExcelWrite(HttpServletResponse response, List<WebAdminUserExcelBase> allList, CellWriteHandler... cellWriteHandlers) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
        String fileName = URLEncoder.encode("??????", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
//        EasyExcel.write(response.getOutputStream(), WebAdminUserExcelExport.class).sheet("??????").doWrite(allList);
        // ????????????
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // ???????????????
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // ?????????????????????
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(response.getOutputStream(), WebAdminUserExcelBase.class).sheet("??????").registerWriteHandler(horizontalCellStyleStrategy);
        if (null != cellWriteHandlers && cellWriteHandlers.length > 0) {
            for (int i = 0; i < cellWriteHandlers.length; i++) {
                excelWriterSheetBuilder.registerWriteHandler(cellWriteHandlers[i]);
            }
        }
        // ????????????
        excelWriterSheetBuilder.doWrite(allList);
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @Override
    public CellWriteHandler getCellWriteHandler(int dataSize) {
        // ????????????????????????
        List<OrganizationEntity> organizationEntityList = organizationService.findAllList();
        // ????????????????????????
        List<RoleEntity> roleEntityList = roleService.findAllList();
        List<String> orgList = organizationEntityList.stream().map(OrganizationEntity::getNameLink).collect(Collectors.toList());
        List<String> roleList = roleEntityList.stream().map(RoleEntity::getName).collect(Collectors.toList());
        // ?????????????????????
        List<Integer> columns = Arrays.asList(4, 5, 6);

        HashMap<Integer, String[]> dropDownMap = new HashMap<>();
        // ???????????????
        String[] sex = {"???", "???"};
        dropDownMap.put(4, sex);
        dropDownMap.put(5, orgList.toArray(new String[]{}));
        dropDownMap.put(6, roleList.toArray(new String[]{}));

        ExcelTitleHandler titleHandler = new ExcelTitleHandler(columns, IndexedColors.BLACK.index, null, dropDownMap, dataSize);

        return titleHandler;
    }

    private void addSSOUser(JSONObject jsonObject, HttpServletRequest request) throws DoValidException{
        String account = jsonObject.getString("preferred_username");
        String name = jsonObject.getString("given_name");
        String ssoId = jsonObject.getString("sub");

        WebAdminUserEntity entity = new WebAdminUserEntity();
        entity.setId(BaseUtils.generate32Id());
        entity.setAccount(account);
        entity.setName(name);
        entity.setEnable(true);
        entity.setPinyin(PinyinHelper.toPinyin(name));
        entity.setPassword("e10adc3949ba59abbe56e057f20f883e");

        entity.setOrganizationName("");
        entity.setCreateTime(Calendar.getInstance());
        entity.setSsoId(ssoId);

        /*????????????????????????*/
        RoleEntity role = roleService.find("default").orElseThrow(() -> new DoValidException("???????????????"));
        roleUserService.insert(new RoleUserEntity(role.getId(), role.getName(), entity), request);
        entity.setRoleName(role.getName());

        webAdminUserDao.save(entity);
    }

    /**
     * ????????????????????????
     */
    @Override
    public JSONObject getLoginUser(HttpServletRequest request) throws Exception {
        return JSONObject.parseObject(authzMyInfoService.getCurrentUser(request));
    }

    /**
     * ??????SsoCookie
     */
    @Override
    public void clearSsoCookie(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("JSESSIONID",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(HttpServletRequest request) throws Exception{
        List<WebAdminUserEntity> oldList = webAdminUserDao.findAll();
        List<UserRepresentation> userList = ssoService.findAll();

        for(UserRepresentation user : userList){
            String account = user.getUsername();
            WebAdminUserEntity webAdminUser = new WebAdminUserEntity();

            if (webAdminUserDao.existsByAccount(account)) {
                webAdminUser = webAdminUserDao.findByAccount(account).get();
                webAdminUser.setName(account);
                webAdminUser.setSsoId(user.getId());
            } else {
                webAdminUser.initInsert();
                webAdminUser.setId(BaseUtils.generate32Id());
                webAdminUser.setAccount(account);
                webAdminUser.setName(account);
                webAdminUser.setEnable(true);
                webAdminUser.setPinyin(PinyinHelper.toPinyin(account));
                webAdminUser.setSsoId(user.getId());

                roleUserService.insert(new RoleUserEntity("default", "????????????", webAdminUser), request);
                webAdminUser.setRoleName("????????????");
            }

            TokenManager.getInstance().updateCreateIdAndTime(request, webAdminUser);
            //??????????????????
            List<OrganizationUserEntity> userOrgs = organizationUserService.findByUserId(webAdminUser.getId());
            if(userOrgs.size()>0) {
                OrganizationEntity organization = organizationService.find(userOrgs.get(0).getOrganizationId()).orElseThrow(() -> new DoValidException("???????????????"));
                webAdminUser.setOrganizationName(organization.getNameLink());
            }
            webAdminUserDao.save(webAdminUser);
        }

        for(WebAdminUserEntity entity: oldList) {
            if (!userList.stream().filter(m -> m.getUsername().equals(entity.getAccount())).findAny().isPresent()) {
                webAdminUserDao.deleteByAccount(entity.getAccount());
            }
        }
    }
}
