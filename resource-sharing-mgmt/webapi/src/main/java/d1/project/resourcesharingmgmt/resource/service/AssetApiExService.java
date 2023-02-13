package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.business.ArchBusiUviewExBusiness;
import d1.project.resourcesharingmgmt.resource.dao.AssetApiExDao;
import d1.project.resourcesharingmgmt.resource.dao.ResourceUseInfoDao;
import d1.project.resourcesharingmgmt.resource.entity.AssetApiExEntity;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExUpdateVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.*;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.security.HmacSignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 信息资源目录与云接口关联表
 *
 * @author zhengyang
 */
@Service
public class AssetApiExService {
    private final AssetApiExDao assetApiExDao;
    private final ResourceUseInfoDao resourceUseInfoDao;
    private final IOperationLogService iOperationLogService;
    private final ArchBusiUviewExBusiness archBusiUviewExBusiness;

    @Value("${apiManage.url}")
    private String apiManage_url;

    @Value("${apiManage.appid}")
    private String apiManage_appid;

    @Value("${apiManage.appkey}")
    private String apiManage_appkey;

    public AssetApiExService(AssetApiExDao assetApiExDao, ResourceUseInfoDao resourceUseInfoDao, IOperationLogService iOperationLogService, ArchBusiUviewExBusiness archBusiUviewExBusiness) {
        this.assetApiExDao = assetApiExDao;
        this.resourceUseInfoDao = resourceUseInfoDao;
        this.iOperationLogService = iOperationLogService;
        this.archBusiUviewExBusiness = archBusiUviewExBusiness;
    }

    /**
     * 删除云接口
     *
     * @param id
     * @param request
     * @throws DoValidException
     */
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        AssetApiExEntity assetApiExEntity = assetApiExDao.findById(id).orElseThrow(() -> new DoValidException("此资源目录下无该云接口信息"));
        assetApiExDao.delete(assetApiExEntity);
        if (assetApiExDao.countByUviewId(assetApiExEntity.getUviewId()) == 0) {
            ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
            archBusiUviewExUpdateVm.setId(assetApiExEntity.getUviewId());
            archBusiUviewExUpdateVm.setHookStatus("0");
            archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);
        }

        iOperationLogService.insert(new OperationLog("资源管理_云接口_删除", "云接口删除", "云接口删除：" + assetApiExEntity.getId(), JSON.toJSONString(assetApiExEntity), 1), request);
    }

    /**
     * 删除云接口
     *
     * @param ids
     * @param request
     * @throws DoValidException
     */
    public void deleteAll(Collection<String> ids, HttpServletRequest request) throws DoValidException {
        List<AssetApiExEntity> assetApiExEntitys = assetApiExDao.findAllById(ids);
        assetApiExDao.deleteAll(assetApiExEntitys);
        if (assetApiExDao.countByUviewId(assetApiExEntitys.get(0).getUviewId()) == 0) {
            ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
            archBusiUviewExUpdateVm.setId(assetApiExEntitys.get(0).getUviewId());
            archBusiUviewExUpdateVm.setHookStatus("0");
            archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);
        }

        iOperationLogService.insert(new OperationLog("资源管理_云接口_批量删除", "云接口批量删除", "云接口批量删除", JSON.toJSONString(assetApiExEntitys), 1), request);
    }

    /**
     * 新增云接口
     *
     * @param assetApiExInsertVm
     * @param request
     */
    public void insert(AssetApiExInsertVm assetApiExInsertVm, HttpServletRequest request) throws DoValidException {
        String UviewId = assetApiExInsertVm.getUviewId();
        String SourceApiId = assetApiExInsertVm.getSourceApiId();
        if (assetApiExDao.existsByUviewIdAndSourceApiId(UviewId, SourceApiId)) {
            throw new DoValidException("选中的资源数据中有已关联的，请核查后再添加");
        } else {
            AssetApiExEntity entity = new AssetApiExEntity();
            BeanUtils.copyProperties(assetApiExInsertVm, entity);
            entity.setId(BaseUtils.generate32Id());
            TokenManager.getInstance().updateCreateIdAndTime(request, entity);
            assetApiExDao.save(entity);
            //更新资源目录表的挂接状态
            ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
            archBusiUviewExUpdateVm.setId(entity.getUviewId());
            archBusiUviewExUpdateVm.setHookStatus("1");
            archBusiUviewExUpdateVm.setHookTime(Calendar.getInstance());
            archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);
            iOperationLogService.insert(new OperationLog("资源管理_云接口_新增", "云接口新增", "云接口新增：" + entity.getId(), JSON.toJSONString(entity), 1), request);
        }
    }

    /**
     * 批量新增云接口
     *
     * @param assetApiExInsertAllVm
     * @param request
     */
    public void insertAll(AssetApiExInsertAllVm assetApiExInsertAllVm, HttpServletRequest request) throws DoValidException {
        String UviewId = assetApiExInsertAllVm.getUviewId();
        List<ApiExInsertAllVm> sourceApis = assetApiExInsertAllVm.getSourceApis();
        for (ApiExInsertAllVm sourceApi : sourceApis) {
            if (assetApiExDao.existsByUviewIdAndSourceApiId(UviewId, sourceApi.getSourceApiId())) {
                throw new DoValidException("选中的资源数据中有已关联的，请核查后再添加");
            } else {
                AssetApiExEntity entity = new AssetApiExEntity();
                BeanUtils.copyProperties(sourceApi, entity);
                entity.setUviewId(assetApiExInsertAllVm.getUviewId());
                entity.setUviewNm(assetApiExInsertAllVm.getUviewNm());
                entity.setId(BaseUtils.generate32Id());
                TokenManager.getInstance().updateCreateIdAndTime(request, entity);
                assetApiExDao.save(entity);

            }
        }
        //更新资源目录表的挂接状态
        ArchBusiUviewExUpdateVm archBusiUviewExUpdateVm = new ArchBusiUviewExUpdateVm();
        archBusiUviewExUpdateVm.setId(assetApiExInsertAllVm.getUviewId());
        archBusiUviewExUpdateVm.setHookStatus("1");
        archBusiUviewExUpdateVm.setHookTime(Calendar.getInstance());
        archBusiUviewExBusiness.update(archBusiUviewExUpdateVm, request);

        iOperationLogService.insert(new OperationLog("资源管理_云接口_批量新增", "云接口批量新增", "云接口批量新增：", JSON.toJSONString(assetApiExInsertAllVm), 1), request);
    }

    private Map<String, Object> getHttpHeaders() throws Exception {
        String appid = apiManage_appid;
        String appkey = apiManage_appkey;
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.put("timestamp", timestamp);
        HmacSignService hmacSignService = new HmacSignService();
        String sign = hmacSignService.sign(appid, timestamp, appkey);
        String authorization = "sign " + sign;
        headers.put("Authorization", authorization);
        headers.put("appid", appid);
        return headers;
    }

    public ApiEntityVm findByApiId(String id) throws Exception {
        HttpService httpService = new HttpService();
        Map<String, Object> headers = this.getHttpHeaders();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String result = httpService.get(apiManage_url + "/service/api/source/find", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");
        ApiEntityVm apiEntityVm = JSON.parseObject(data, ApiEntityVm.class);
        return apiEntityVm;
    }

    /**
     * 调用外部接口，获取所有云接口信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<ApiEntityVm> findAllByApi(AssetApiExFindAllVm model) throws Exception {
        int totalElements = 0;

        List<AssetApiExEntity> apiList = new ArrayList<>();
        if (!StringUtils.isEmpty(model.getUviewId())) {
            apiList = assetApiExDao.findByUviewId(model.getUviewId());
        }

        HttpService httpService = new HttpService();
        Map<String, Object> headers = this.getHttpHeaders();
        Map<String, Object> params = new HashMap<>();
        params.put("page", model.getPage());
        params.put("size", model.getSize());
        params.put("name", model.getName());
        params.put("groupId", model.getGroupId());
        if (apiList.size() > 0) {
            List<String> ids = apiList.stream().map(AssetApiExEntity::getSourceApiId).collect(Collectors.toList());
            params.put("ids", Joiner.on(",").join(ids));
        }
        String result = httpService.get(apiManage_url + "/service/api/source/findAll", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");
        JSONObject jdata = JSONObject.parseObject(data);
        String content = jdata.getString("content");

        if ("1".equals(jsonObject.getString("code"))) {
            totalElements = jdata.getInteger("totalElements");
        }
        List<ApiEntityVm> listVm = JSON.parseObject(content, new TypeReference<List<ApiEntityVm>>() {
        });
        return new PageImpl<>(listVm, getPageable(null, model.getPage(), model.getSize()), totalElements);
    }

    /**
     * 调用外部接口，获取所有云接口分组
     *
     * @return
     * @throws Exception
     */
    public List<ApiGroupVm> findGroupTree() throws Exception {
        HttpService httpService = new HttpService();
        Map<String, Object> headers = this.getHttpHeaders();
        Map<String, Object> params = new HashMap<>();
        String result = httpService.get(apiManage_url + "/service/api/source/findGroupTree", headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String data = jsonObject.getString("data");
        List<ApiGroupVm> listVm = JSON.parseObject(data, new TypeReference<List<ApiGroupVm>>() {
        });
        return listVm;
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

    /**
     * 查询资源目录下所有云接口信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<AssetApiExEntity> findAll(AssetApiExFindAllVm model) throws Exception {
        SpecificationBuilder<AssetApiExEntity> builder = new SpecificationBuilder<>();
        Specification<AssetApiExEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sEqual("uviewId", "uviewId")
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return assetApiExDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<AssetApiExEntity> find(String id) {
        return assetApiExDao.findById(id);
    }

    /**
     * 详情
     *
     * @param id id
     */
    public AssetApiExVm findById(String id, HttpServletRequest request) throws DoValidException{
        JSONObject webAdminUserVm = new JSONObject();
        AssetApiExVm vm = new AssetApiExVm();
        AssetApiExEntity assetApiExEntity = assetApiExDao.findById(id).orElseThrow(() -> new DoValidException("云接口不存在"));;
        BeanUtils.copyProperties(assetApiExEntity, vm);

        try {
            webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        } catch (Exception e) {

        }
        if (webAdminUserVm != null) {
            String userId = webAdminUserVm.getString("id");
            Optional<ResourceUseInfoEntity> resourceUseInfoEntity = resourceUseInfoDao.findFirstByCreateByIdAndApiIdAndUviewId(userId, assetApiExEntity.getSourceApiId(), assetApiExEntity.getUviewId());
            if(resourceUseInfoEntity.isPresent()){
                vm.setContainer(resourceUseInfoEntity.get().getContainer());
                vm.setRouteInfo(resourceUseInfoEntity.get().getRouteInfo());
            }
        }

        return vm;
    }

    /**
     * 统计挂接数
     *
     * @param orgId orgId
     */
    public long countByOrgId(String orgId) {
        return assetApiExDao.countByOrgId(orgId);
    }

    public long count(){
        return assetApiExDao.count();
    }
}
