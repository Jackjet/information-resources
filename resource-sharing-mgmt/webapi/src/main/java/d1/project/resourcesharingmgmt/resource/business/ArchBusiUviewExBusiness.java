package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.AssetFileExDao;
import d1.project.resourcesharingmgmt.resource.entity.*;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.*;
import d1.project.resourcesharingmgmt.resource.model.AssetDict.AssetDictVm;
import d1.project.resourcesharingmgmt.resource.model.DicAssetCate.DictAssetCateVm;
import d1.project.resourcesharingmgmt.resource.service.*;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.entity.SystemSettingsEntity;
import d1.project.resourcesharingmgmt.system.model.OrganizationVm;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import d1.project.resourcesharingmgmt.system.service.SystemSettingsService;
import net.dcrun.component.http.HttpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 信息资源目录
 *
 * @author JungYoung
 */
@Service
public class ArchBusiUviewExBusiness {
    private final ArchBusiUviewExService archBusiUviewExService;
    private final ArchBusiUviewStrExService archBusiUviewStrExService;
    private final DictAssetCateService dictAssetCateService;
    private final AssetCateExService assetCateExService;
    private final MyFocusInfoService myFocusInfoService;
    private final IOperationLogService iOperationLogService;
    private final OrganizationService organizationService;
    private final JdbcTemplate jdbcTemplate;
    private final SystemSettingsService systemSettingsService;
    private final AssetDictService assetDictService;
    private final AssetFileExDao assetFileExDao;

    @Value("${daas.apiKey}")
    private String api_Key;
    @Value("${dass.url}")
    private String dass_url;

    public ArchBusiUviewExBusiness(ArchBusiUviewExService archBusiUviewExService, ArchBusiUviewStrExService archBusiUviewStrExService, DictAssetCateService dictAssetCateService,
                                   AssetCateExService assetCateExService, MyFocusInfoService myFocusInfoService, IOperationLogService iOperationLogService,
                                   OrganizationService organizationService, JdbcTemplate jdbcTemplate, SystemSettingsService systemSettingsService,
                                   AssetDictService assetDictService, AssetFileExDao assetFileExDao) {
        this.archBusiUviewExService = archBusiUviewExService;
        this.archBusiUviewStrExService = archBusiUviewStrExService;
        this.dictAssetCateService = dictAssetCateService;
        this.assetCateExService = assetCateExService;
        this.myFocusInfoService = myFocusInfoService;
        this.iOperationLogService = iOperationLogService;
        this.organizationService = organizationService;
        this.jdbcTemplate = jdbcTemplate;
        this.systemSettingsService = systemSettingsService;
        this.assetDictService = assetDictService;
        this.assetFileExDao = assetFileExDao;
    }

    public void update(ArchBusiUviewExUpdateVm model, HttpServletRequest request) throws DoValidException {
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.find(model.getId()).orElseThrow(() -> new DoValidException("资源目录信息不存在"));
        if (model.getStatus() != null && !"".equals(model.getStatus())) {
            archBusiUviewExEntity.setStatus(model.getStatus());
        }
        if (model.getHookStatus() != null && !"".equals(model.getHookStatus())) {
            archBusiUviewExEntity.setHookStatus(model.getHookStatus());
        }
        if (model.getFileHookStatus() != null && !"".equals(model.getFileHookStatus())) {
            archBusiUviewExEntity.setFileHookStatus(model.getFileHookStatus());
        }
        if (model.getDataHookStatus() != null && !"".equals(model.getDataHookStatus())) {
            archBusiUviewExEntity.setDataHookStatus(model.getDataHookStatus());
        }
        if (model.getHookTime() != null) {
            archBusiUviewExEntity.setHookTime(model.getHookTime());
        }
        TokenManager.getInstance().updateUpdateIdAndTime(request, archBusiUviewExEntity);
        archBusiUviewExService.update(archBusiUviewExEntity);
        iOperationLogService.insert(new OperationLog("资源目录管理_更新", "更新状态", "更新资源目录" + archBusiUviewExEntity.getUviewNm(), JSON.toJSONString(archBusiUviewExEntity), 1), request);
    }

    public Page<ArchBusiUviewExResVm> findAll(ArchBusiUviewExFindAllVm model, HttpServletRequest request) throws Exception {
        String userId = "";
        List<String> orgs = new ArrayList<>();
        JSONObject webAdminUserVm = new JSONObject();
        try {
            webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        } catch (Exception e) {

        }
        if (webAdminUserVm != null) {
            userId = webAdminUserVm.getString("id");
        }
        if (!StringUtils.isEmpty(model.getProvOrgId())) {
            orgs.add(model.getProvOrgId());
        }

        //筛选上架的资源
        model.setStatus("0");
        String sql = archBusiUviewExService.findAllSql(model, userId, orgs);
        List<ArchBusiUviewExResVm> listVm = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<ArchBusiUviewExResVm>(ArchBusiUviewExResVm.class));
        String countSql = archBusiUviewExService.findAllCountSql(model, userId, orgs);
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        return new PageImpl<>(listVm, getPageable(null, model.getPage(), model.getSize()), count);
    }

    public Page<ArchBusiUviewExEntity> findAllBySql(ArchBusiUviewExFindAllVm model, HttpServletRequest request) throws Exception {
        List<String> orgs = new ArrayList<>();
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);

        String userId = webAdminUserVm.getString("id");
        String orgId = webAdminUserVm.getString("organizationId");
        List<OrganizationEntity> orgList = organizationService.findAllByOrganizationId(orgId);
        if (model.getProvOrgId() == null || model.getProvOrgId().isEmpty()) {
            if (!"admin".equals(userId)) {
                orgs = orgList.stream().map(OrganizationEntity::getId).collect(Collectors.toList());
            }
        } else {
            orgs.add(model.getProvOrgId());
        }

        String sql = archBusiUviewExService.findAllSql(model, "", orgs);
        List<ArchBusiUviewExEntity> listVm = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<ArchBusiUviewExEntity>(ArchBusiUviewExEntity.class));
        String countSql = archBusiUviewExService.findAllCountSql(model, "", orgs);
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        return new PageImpl<>(listVm, getPageable(null, model.getPage(), model.getSize()), count);
    }

    private Pageable getPageable(Sort sort, Integer page, Integer size) {
        page = page != null ? page : 1;
        size = size != null ? size : 10;
        page = page != null && page >= 1 ? page - 1 : 0;
        if (size == null || size < 1) {
            size = 10;
        }

        return sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);
    }

    public ArchBusiUviewExVm find(String id, HttpServletRequest request) throws DoValidException {
        String userId = "";
        JSONObject webAdminUserVm = new JSONObject();
        try {
            webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        } catch (DoValidException e) {

        }
        if (webAdminUserVm != null) {
            userId = webAdminUserVm.getString("id");
        }
        ArchBusiUviewExVm archBusiUviewExVm = new ArchBusiUviewExVm();
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录不存在"));
        Optional<OrganizationEntity> organizationEntity = organizationService.find(archBusiUviewExEntity.getProvOrgId());
        BeanUtils.copyProperties(archBusiUviewExEntity, archBusiUviewExVm);
        if (organizationEntity.isPresent()) {
            archBusiUviewExVm.setName(organizationEntity.get().getName());
        }
        //设置关注数量
        long focusCount = myFocusInfoService.countAllByUviewId(archBusiUviewExVm.getUviewId());
        archBusiUviewExVm.setFocusCount(focusCount);
        //设置是否关注
        if (myFocusInfoService.existsAllByUviewIdAndCreateById(archBusiUviewExEntity.getUviewId(), userId)) {
            archBusiUviewExVm.setIsFocus(1);
        } else {
            archBusiUviewExVm.setIsFocus(0);
        }
        return archBusiUviewExVm;
    }

    public int findVisits(String id, HttpServletRequest request) throws DoValidException {
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录不存在"));
        //增加访问量
        archBusiUviewExEntity.setVisitsCount(archBusiUviewExEntity.getVisitsCount() + 1);
        archBusiUviewExService.update(archBusiUviewExEntity);

        return archBusiUviewExEntity.getVisitsCount();
    }

    /**
     * 同步资源目录信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncData() throws Exception {
        syncOrganization();
        syncAssetDict();
        syncArchBusiUviewEx();
        syncDictAssetCate();
    }

    /**
     * 同步资源目录信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void fileMount() throws Exception {
        List<String> orgList = getList(Constants.FILE);
        for (String orgPath : orgList) {
            String orgName = orgPath.replace("\\", "/");
            orgName = orgName.substring(orgName.lastIndexOf("/") + 1);
            if(!"ts".equals(orgName)) {
                mountFileList(orgPath);
            }
        }
    }

    /**
     * 挂接目录
     * @param path
     * @throws Exception
     */
    public void mountFileList(String path) throws Exception {
        List<String> codeList = getList(path);
        for (String code : codeList) {
            String codeName = code.replace("\\", "/");
            codeName = codeName.substring(codeName.lastIndexOf("/") + 1);
            if (codeName.contains("t_")) {
                String realCode = codeName.substring(2).replace("_", "/");
                List<String> fileList = getFileList(code);

                if (fileList.size() > 0) {
                    //查询CODE
                    HttpService httpService = new HttpService();
                    Map<String, Object> headers = new HashMap<String, Object>();
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("apiKey", api_Key);
                    params.put("page", "1");
                    params.put("limit", "5");
                    params.put("cityCataCode", realCode);
                    String result = httpService.get(dass_url + "/resourceCatalog", headers, params);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    JSONObject jData = jsonObject.getJSONObject("data");
                    JSONArray jRecord = jData.getJSONArray("records");
                    if (jRecord.size() > 0) {
                        JSONObject archObj = jRecord.getJSONObject(0);
                        String uviewId = archObj.getString("uviewId");
                        String cityCataCode = archObj.getString("cityCataCode");

                        //这里主要是判断接口返回的数据是不是正确的
                        if (realCode.equals(cityCataCode)) {
                            Optional<ArchBusiUviewExEntity> optional = archBusiUviewExService.findByUviewId(uviewId);

                            if (optional.isPresent()) {
                                ArchBusiUviewExEntity archBusiUviewExEntity = optional.get();
                                archBusiUviewExEntity.setFileHookStatus("1");
                                archBusiUviewExEntity.setHookTime(Calendar.getInstance());

                                for (String file : fileList) {
                                    String fileName = file.replace("\\", "/");
                                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                                    String downloadUri = file.replace(Constants.FILE, "").replace("\\", "/");
                                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                                    Optional<AssetFileExEntity> entityOptional = assetFileExDao.findFirstByUviewIdAndName(uviewId, fileName);
                                    if (!entityOptional.isPresent()) {
                                        AssetFileExEntity entity = new AssetFileExEntity();
                                        entity.setId(BaseUtils.generate32Id());
                                        entity.setUviewId(archBusiUviewExEntity.getUviewId());
                                        entity.setUviewNm(archBusiUviewExEntity.getUviewNm());
                                        entity.setName(fileName);
                                        entity.setFileDownloadUri("webadmin/file/download/" + downloadUri);
                                        entity.setFileType(fileType);
                                        entity.setCreateById("admin");
                                        entity.setCreateTime(Calendar.getInstance());

                                        assetFileExDao.save(entity);
                                    }
                                }

                                archBusiUviewExService.save(archBusiUviewExEntity);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 所有的部门
     */
    public void syncOrganization() throws Exception {
        HttpService httpService = new HttpService();
        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("apiKey", api_Key);
        params.put("page", "1");
        params.put("limit", "99999999");
        String result = httpService.get(dass_url + "/allOrg", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");
        JSONObject jData = JSONObject.parseObject(data);
        String jRecord = jData.getString("records");
        Integer totalCount = jData.getInteger("total");
        List<OrganizationEntity> orgList = organizationService.findAllList();
        List<String> oldOrgIds = orgList.stream().map(OrganizationEntity::getId).collect(Collectors.toList());
        List<String> deleteIds = new ArrayList<>();

        if (totalCount > 0) {
            List<OrganizationVm> organizationVmList = JSONObject.parseArray(jRecord, OrganizationVm.class);
            List<String> newOrgIds = organizationVmList.stream().map(s -> String.valueOf(s.getOrgId())).collect(Collectors.toList());
            organizationVmList.sort(Comparator.comparing(OrganizationVm::getOrgId));
            for (OrganizationVm entity : organizationVmList) {
                OrganizationEntity organizationEntity = new OrganizationEntity();

                if (!orgList.stream().filter(m -> m.getId().equals(entity.getOrgId().toString())).findAny().isPresent()) {
                    organizationEntity.setId(entity.getOrgId().toString());
                    organizationEntity.setName(entity.getOrgNm());
                    if(StringUtils.isEmpty(entity.getOrgAlias())){
                        organizationEntity.setAlias("");
                    }else {
                        organizationEntity.setAlias(entity.getOrgAlias());
                    }
                    if (entity.getParOrgId() != null) {
                        organizationEntity.setParentId(entity.getParOrgId());
                    } else {
                        organizationEntity.setParentId("headquarters");
                    }
                    organizationEntity.setSeq(entity.getDispalySn());

                    organizationService.insert(organizationEntity);
                    organizationEntity.setCreateTime(Calendar.getInstance());
                } else {
                    organizationEntity = orgList.stream().filter(m -> m.getId().equals(entity.getOrgId().toString())).collect(Collectors.toList()).get(0);
                    OrganizationEntity newEntity = new OrganizationEntity();
                    newEntity.setId(entity.getOrgId().toString());
                    newEntity.setName(entity.getOrgNm());
                    newEntity.setAlias(entity.getOrgAlias());
                    if (entity.getParOrgId() != null) {
                        newEntity.setParentId(entity.getParOrgId());
                    } else {
                        newEntity.setParentId("headquarters");
                    }
                    newEntity.setSeq(entity.getDispalySn());
                    if (!organizationEntity.equals(newEntity)) {
                        organizationEntity.setId(entity.getOrgId().toString());
                        organizationEntity.setName(entity.getOrgNm());
                        organizationEntity.setAlias(entity.getOrgAlias());
                        if (entity.getParOrgId() != null) {
                            organizationEntity.setParentId(entity.getParOrgId());
                        } else {
                            organizationEntity.setParentId("headquarters");
                        }
                        organizationEntity.setSeq(entity.getDispalySn());
                        organizationEntity.setUpdateTime(Calendar.getInstance());
                        organizationService.insert(organizationEntity);
                    }
                }

            }

            //删除多余机构
            for (String oldid : oldOrgIds) {
                if (!newOrgIds.stream().filter(item -> item.equals(oldid)).findAny().isPresent()) {
                    if (!"headquarters".equals(oldid)) {
                        deleteIds.add(oldid);
                    }
                }
            }
            if (deleteIds.size() > 0) {
                organizationService.deleteBatch(deleteIds);
            }
        }
    }

    /**
     * 更新所有资源目录信息
     */
    public void syncArchBusiUviewEx() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        Optional<SystemSettingsEntity> entityOptional = systemSettingsService.findByType("archLastUpdate");
        SystemSettingsEntity entity = new SystemSettingsEntity();
        Calendar updateTime = Calendar.getInstance();

        int page = 1;
        params.put("apiKey", api_Key);
        params.put("page", String.valueOf(page));
        params.put("limit", "100");
        if (entityOptional.isPresent()) {
            entity = entityOptional.get();
            params.put("startTime", entityOptional.get().getValue());
        } else {
            SystemSettingsEntity systemSettingsEntity = new SystemSettingsEntity();
            systemSettingsEntity.setId(BaseUtils.generate32Id());
            systemSettingsEntity.setType("archLastUpdate");
            systemSettingsEntity.setValue(dateFormat.format(updateTime.getTime()));
            systemSettingsEntity.setCreateTime(Calendar.getInstance());
            systemSettingsEntity.setUpdateTime(Calendar.getInstance());
            systemSettingsService.save(systemSettingsEntity);
        }
        JSONObject jsonObject = httpServiceGet(dass_url + "/allResdoc", headers, params);
        String data = jsonObject.getString("data");
        if (data == null) {
            return;
        }
        JSONObject jData = JSONObject.parseObject(data);
        String jRecord = jData.getString("records");
        int totalPage = jData.getInteger("pages");

        List<String> uviewIds = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            if (i == 1) {
                uviewIds.addAll(addArchBusiUviewEx(jRecord));
            }
            if (i != 1 && page <= totalPage) {
                params.put("page", String.valueOf(i));
                JSONObject jsonObjectByPage = httpServiceGet(dass_url + "/allResdoc", headers, params);
                String dataByPage = jsonObjectByPage.getString("data");
                JSONObject jDataByPage = JSONObject.parseObject(dataByPage);
                String jRecordByPage = jDataByPage.getString("records");
                uviewIds.addAll(addArchBusiUviewEx(jRecordByPage));
            }
        }
        syncArchBusiUviewStrEx(uviewIds);
        syncAssetCateEx(uviewIds);

        entity.setValue(dateFormat.format(updateTime.getTime()));
        systemSettingsService.save(entity);
    }

    /**
     * 所有的资源目录信息项
     *
     * @throws Exception
     */
    public void syncArchBusiUviewStrEx(List<String> uviewIds) throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        for (String uviewId : uviewIds) {
            int page = 1;
            params.put("apiKey", api_Key);
            params.put("pubSts", "");
            params.put("uviewId", uviewId);
            params.put("page", String.valueOf(page));
            params.put("limit", "100");
            JSONObject jsonObject = httpServiceGet(dass_url + "/allResdocStr", headers, params);
            String data = jsonObject.getString("data");
            JSONObject jData = JSONObject.parseObject(data);
            String jRecord = jData.getString("records");
            int totalPage = jData.getInteger("pages");

            for (int i = 1; i <= totalPage; i++) {
                if (i == 1) {
                    addArchBusiUviewStrEx(jRecord, uviewId);
                }
                if (i != 1 && page <= totalPage) {
                    params.put("page", String.valueOf(i));
                    JSONObject jsonObjectByPage = httpServiceGet(dass_url + "/allResdocStr", headers, params);
                    String dataByPage = jsonObjectByPage.getString("data");
                    JSONObject jDataByPage = JSONObject.parseObject(dataByPage);
                    String jRecordByPage = jDataByPage.getString("records");
                    addArchBusiUviewStrEx(jRecordByPage, uviewId);
                }
            }
        }
    }

    /**
     * 所有的目录分类
     */
    public void syncDictAssetCate() throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        int page = 1;
        params.put("apiKey", api_Key);
        params.put("page", String.valueOf(page));
        params.put("limit", "100");
        JSONObject jsonObject = httpServiceGet(dass_url + "/allType", headers, params);
        String data = jsonObject.getString("data");
        JSONObject jData = JSONObject.parseObject(data);
        String jRecord = jData.getString("records");
        int totalPage = jData.getInteger("pages");

        List<String> oldTypeIds = dictAssetCateService.findAll().stream().map(DictAssetCateEntity::getTypId).collect(Collectors.toList());
        List<String> newtypeIds = new ArrayList<>();
        List<String> deleteIds = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            if (i == 1) {
                newtypeIds.addAll(addDictAssetCate(jRecord));
            }
            if (i != 1 && page <= totalPage) {
                params.put("page", String.valueOf(i));
                JSONObject jsonObjectByPage = httpServiceGet(dass_url + "/allType", headers, params);
                String dataByPage = jsonObjectByPage.getString("data");
                JSONObject jDataByPage = JSONObject.parseObject(dataByPage);
                String jRecordByPage = jDataByPage.getString("records");
                newtypeIds.addAll(addDictAssetCate(jRecordByPage));
            }
        }
        for (String oldid : oldTypeIds) {
            if (!newtypeIds.stream().filter(item -> item.equals(oldid)).findAny().isPresent()) {
                deleteIds.add(oldid);
            }
        }
        if (deleteIds.size() > 0) {
            dictAssetCateService.deleteBatchByTypId(deleteIds);
        }
    }

    /**
     * 所有的字典
     */
    public void syncAssetDict() throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("apiKey", api_Key);
        JSONObject jsonObject = httpServiceGet(dass_url + "/queryDict", headers, params);
        String data = jsonObject.getString("data");

        List<AssetDictEntity> oldDicts = assetDictService.findAll();
        List<String> oldDictIds = oldDicts.stream().map(AssetDictEntity::getId).collect(Collectors.toList());
        List<AssetDictVm> assetDictVms = JSONObject.parseArray(data, AssetDictVm.class);
        List<String> newDictIds = new ArrayList<>();
        List<String> deleteIds = new ArrayList<>();
        for (AssetDictVm assetDictVm :
                assetDictVms) {
            String id = assetDictVm.getId();
            newDictIds.add(id);

            if (!oldDicts.stream().filter(m -> m.getId().equals(id)).findAny().isPresent()) {
                AssetDictEntity entity = new AssetDictEntity();
                BeanUtils.copyProperties(assetDictVm, entity);
                entity.setId(id);
                assetDictService.save(entity);
            } else {
                AssetDictEntity entity = oldDicts.stream().filter(m -> m.getId().equals(id)).collect(Collectors.toList()).get(0);
                AssetDictEntity newEntity = new AssetDictEntity();
                BeanUtils.copyProperties(assetDictVm, newEntity);
                if (!entity.equals(newEntity)) {
                    BeanUtils.copyProperties(assetDictVm, entity);
                    entity.setId(id);
                    assetDictService.save(entity);
                }
            }
        }
        for (String oldid : oldDictIds) {
            if (!newDictIds.stream().filter(item -> item.equals(oldid)).findAny().isPresent()) {
                deleteIds.add(oldid);
            }
        }
        if (deleteIds.size() > 0) {
            assetDictService.deleteBatch(deleteIds);
        }
    }

    /**
     * 资源目录与分类关联
     */
    public void syncAssetCateEx(List<String> uviewIds) throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        for (String uviewId : uviewIds) {
            int page = 1;
            params.put("apiKey", api_Key);
//        params.put("type", "");
            params.put("infoId", uviewId);
//        params.put("typeId", "");
            params.put("page", String.valueOf(page));
            params.put("limit", "100");
            JSONObject jsonObject = httpServiceGet(dass_url + "/allTypeRel", headers, params);
            String data = jsonObject.getString("data");
            JSONObject jData = JSONObject.parseObject(data);
            String jRecord = jData.getString("records");
            int totalPage = jData.getInteger("pages");

            //删除旧的关联
            assetCateExService.deleteByInfoId(uviewId);

            for (int i = 1; i <= totalPage; i++) {
                if (i == 1) {
                    addAssetCateEx(jRecord);
                }
                if (i != 1 && page <= totalPage) {
                    params.put("page", String.valueOf(i));
                    JSONObject jsonObjectByPage = httpServiceGet(dass_url + "/allTypeRel", headers, params);
                    String dataByPage = jsonObjectByPage.getString("data");
                    JSONObject jDataByPage = JSONObject.parseObject(dataByPage);
                    String jRecordByPage = jDataByPage.getString("records");
                    addAssetCateEx(jRecordByPage);
                }
            }
        }
    }

    private List<String> addArchBusiUviewEx(String jRecord) throws Exception {
        List<ArchBusiUviewExSyncVm> archBusiUviewExEntityList = JSONObject.parseArray(jRecord, ArchBusiUviewExSyncVm.class);
        List<String> uviewIds = new ArrayList<>();
        for (ArchBusiUviewExSyncVm uviewExEntity : archBusiUviewExEntityList) {
            //判断数据是否删除
            if (uviewExEntity.getDeleted() == 0) {
                uviewIds.add(uviewExEntity.getUviewId());
                if (!archBusiUviewExService.existsByUviewNo(uviewExEntity.getUviewNo())) {
                    ArchBusiUviewExEntity archBusiUviewExEntity = new ArchBusiUviewExEntity();
                    BeanUtils.copyProperties(uviewExEntity, archBusiUviewExEntity);
                    archBusiUviewExEntity.setId(uviewExEntity.getUviewId());
                    archBusiUviewExEntity.setCreateTime(uviewExEntity.getCrtDt());
                    archBusiUviewExEntity.setUpdateTime(uviewExEntity.getUpdateDt());
                    archBusiUviewExEntity.setStatus("0");
                    archBusiUviewExEntity.setHookStatus("0");
                    archBusiUviewExEntity.setFileHookStatus("0");
                    archBusiUviewExEntity.setDataHookStatus("0");
                    archBusiUviewExEntity.setVisitsCount(0);
                    archBusiUviewExService.save(archBusiUviewExEntity);
                } else {
                    ArchBusiUviewExEntity newArchBusiUviewExEntity = new ArchBusiUviewExEntity();
                    ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.findByUviewNo(uviewExEntity.getUviewNo()).orElseThrow(() -> new DoValidException("资源目录信息不存在"));
                    //复制旧的目录到新的目录上，如果目录ID改变了，就删除旧目录
                    BeanUtils.copyProperties(archBusiUviewExEntity, newArchBusiUviewExEntity);
                    newArchBusiUviewExEntity.setId(uviewExEntity.getUviewId());
                    if(!archBusiUviewExEntity.getId().equals(uviewExEntity.getUviewId())){
                        archBusiUviewExService.deleteById(archBusiUviewExEntity.getId());
                    }
                    BeanUtils.copyProperties(uviewExEntity, newArchBusiUviewExEntity);
                    newArchBusiUviewExEntity.setCreateTime(uviewExEntity.getCrtDt());
                    newArchBusiUviewExEntity.setUpdateTime(uviewExEntity.getUpdateDt());
                    if (StringUtils.isEmpty(newArchBusiUviewExEntity.getStatus())) {
                        newArchBusiUviewExEntity.setStatus("0");
                    }
                    if (StringUtils.isEmpty(newArchBusiUviewExEntity.getHookStatus())) {
                        newArchBusiUviewExEntity.setHookStatus("0");
                    }
                    if (StringUtils.isEmpty(newArchBusiUviewExEntity.getFileHookStatus())) {
                        newArchBusiUviewExEntity.setFileHookStatus("0");
                    }
                    if (StringUtils.isEmpty(newArchBusiUviewExEntity.getDataHookStatus())) {
                        newArchBusiUviewExEntity.setDataHookStatus("0");
                    }
                    archBusiUviewExService.save(newArchBusiUviewExEntity);
                }
            } else if (uviewExEntity.getDeleted() == 1) {
                Optional<ArchBusiUviewExEntity> archBusiUviewExEntity = archBusiUviewExService.findByUviewNo(uviewExEntity.getUviewNo());
                if (archBusiUviewExEntity.isPresent()) {
                    archBusiUviewExService.deleteById(archBusiUviewExEntity.get().getId());
                }
            }
        }

        return uviewIds;
    }

    private void addArchBusiUviewStrEx(String jRecord, String uviewId) throws Exception {
        List<ArchBusiUviewStrExEntity> archBusiUviewStrExEntityList = JSONObject.parseArray(jRecord, ArchBusiUviewStrExEntity.class);
        List<ArchBusiUviewStrExEntity> list = new ArrayList<>();
        //删除旧的项
        archBusiUviewStrExService.deleteByUviewId(uviewId);
        for (ArchBusiUviewStrExEntity uviewStrExEntity :
                archBusiUviewStrExEntityList) {
            if (!archBusiUviewStrExService.existsByUviewstrId(uviewStrExEntity.getUviewstrId())) {
                ArchBusiUviewStrExEntity archBusiUviewStrExEntity = new ArchBusiUviewStrExEntity();
                BeanUtils.copyProperties(uviewStrExEntity, archBusiUviewStrExEntity);
                archBusiUviewStrExEntity.setId(uviewStrExEntity.getUviewstrId());
                archBusiUviewStrExEntity.setCreateTime(Calendar.getInstance());
                list.add(archBusiUviewStrExEntity);
            }else{
                Optional<ArchBusiUviewStrExEntity> archBusiUviewStrExEntityOptional = archBusiUviewStrExService.find(uviewStrExEntity.getUviewstrId());
                if(archBusiUviewStrExEntityOptional.isPresent()){
                    ArchBusiUviewStrExEntity archBusiUviewStrExEntity = archBusiUviewStrExEntityOptional.get();
                    BeanUtils.copyProperties(uviewStrExEntity, archBusiUviewStrExEntity);
                    archBusiUviewStrExEntity.setId(uviewStrExEntity.getUviewstrId());
                    archBusiUviewStrExEntity.setCreateTime(Calendar.getInstance());
                    list.add(archBusiUviewStrExEntity);
                }
            }
        }
        if (list.size() > 0) {
            archBusiUviewStrExService.insertAll(list);
        }
    }

    private List<String> addDictAssetCate(String jRecord) throws Exception {
        List<String> ids = new ArrayList<>();
        List<DictAssetCateVm> dictAssetCateEntityList = JSONObject.parseArray(jRecord, DictAssetCateVm.class);
        for (DictAssetCateVm entity :
                dictAssetCateEntityList) {
            if (!dictAssetCateService.existsByTypId(entity.getTypId())) {
                DictAssetCateEntity dictAssetCateEntity = new DictAssetCateEntity();
                BeanUtils.copyProperties(entity, dictAssetCateEntity);
                dictAssetCateEntity.setId(entity.getTypId());
                dictAssetCateEntity.setCreateTime(Calendar.getInstance());
                dictAssetCateService.insert(dictAssetCateEntity);
            } else {
                DictAssetCateEntity dictAssetCateEntity = dictAssetCateService.find(entity.getTypId()).orElseThrow(() -> new DoValidException("资源目录信息项不存在"));
                BeanUtils.copyProperties(entity, dictAssetCateEntity);
                dictAssetCateEntity.setId(entity.getTypId());
                dictAssetCateEntity.setUpdateTime(Calendar.getInstance());
                dictAssetCateService.insert(dictAssetCateEntity);
            }
            ids.add(entity.getTypId());
        }
        return ids;
    }

    private void addAssetCateEx(String jRecord) throws Exception {
        List<AssetCateExEntityVm> assetCateExEntityList = JSONObject.parseArray(jRecord, AssetCateExEntityVm.class);
        for (AssetCateExEntityVm cateExEntity :
                assetCateExEntityList) {
            if (!assetCateExService.existsByInfoIdAndAndTypId(cateExEntity.getInfoId(), cateExEntity.getTypeid())) {
                AssetCateExEntity assetCateExEntity = new AssetCateExEntity();
                assetCateExEntity.setId(String.valueOf(UUID.randomUUID()));
                assetCateExEntity.setCreateTime(Calendar.getInstance());
                assetCateExEntity.setTyp(cateExEntity.getType());
                assetCateExEntity.setInfoId(cateExEntity.getInfoId());
                assetCateExEntity.setTypId(cateExEntity.getTypeid());
                assetCateExService.insert(assetCateExEntity);
            }
        }
    }

    private JSONObject httpServiceGet(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        HttpService httpService = new HttpService();
        httpService.setTimeout(600, 600, 600);
        String result = httpService.get(url, headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    /**
     * 获取路径下所有的文件夹
     *
     * @param path
     * @return
     */
    private List<String> getList(String path) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isDirectory()) {
                list.add(tempList[i].getPath());
                //递归：
                getList(tempList[i].getPath());
            }
        }
        return list;
    }

    /**
     * 获取路径下所有的文件夹
     *
     * @param path
     * @return
     */
    private List<String> getFileList(String path) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                list.add(tempList[i].getPath());
            }
        }
        return list;
    }
}
