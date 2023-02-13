package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.ApiVisitLogDao;
import d1.project.resourcesharingmgmt.resource.entity.ApiVisitLogEntity;
import d1.project.resourcesharingmgmt.system.entity.SystemSettingsEntity;
import d1.project.resourcesharingmgmt.system.service.SystemSettingsService;
import net.dcrun.component.http.HttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * API访问日志
 *
 * @author zhengyang
 */
@Service
public class ApiVisitLogService {
    @Value("${apiIntegrate.url}")
    private String apiIntegrate;

    private final ApiVisitLogDao apiVisitLogDao;
    private final SystemSettingsService systemSettingsService;

    public ApiVisitLogService(ApiVisitLogDao apiVisitLogDao, SystemSettingsService systemSettingsService) {
        this.apiVisitLogDao = apiVisitLogDao;
        this.systemSettingsService = systemSettingsService;
    }

    public void sync() throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> headers = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        Optional<SystemSettingsEntity> entityOptional = systemSettingsService.findByType("apiLogLastSync");
        SystemSettingsEntity entity = new SystemSettingsEntity();
        Calendar updateTime = Calendar.getInstance();

        int page = 1;
        params.put("page", page);
        params.put("size", 100);
        if (entityOptional.isPresent()) {
            entity = entityOptional.get();
            params.put("startTime", entityOptional.get().getValue());
        } else {
            entity.setId(BaseUtils.generate32Id());
            entity.setType("apiLogLastSync");
            entity.setValue(dateFormat.format(updateTime.getTime()));
            entity.setCreateTime(Calendar.getInstance());
            entity.setUpdateTime(Calendar.getInstance());
        }
        JSONObject jsonObject = httpServiceGet(apiIntegrate + "/webadmin/api/ext/findAllLog", headers, params);
        String data = jsonObject.getString("data");
        if (data == null) {
            return;
        }
        JSONObject jData = JSONObject.parseObject(data);
        int totalPage = jData.getInteger("totalPages");
        String content = jData.getString("content");
        for (int i = 1; i <= totalPage; i++) {
            if (i == 1) {
                addApiVisitLog(content);
            }
            if (i != 1 && page <= totalPage) {
                params.put("page", i);
                JSONObject jsonObjectByPage = httpServiceGet(apiIntegrate + "/webadmin/api/ext/findAllLog", headers, params);
                String dataByPage = jsonObjectByPage.getString("data");
                JSONObject jDataByPage = JSONObject.parseObject(dataByPage);
                String contentByPage = jDataByPage.getString("content");
                addApiVisitLog(contentByPage);
            }
        }

        entity.setValue(dateFormat.format(updateTime.getTime()));
        systemSettingsService.save(entity);
    }



    public long count(){
        return apiVisitLogDao.count();
    }

    public long countByDay(String day){
        return apiVisitLogDao.countByDay(day);
    }

    public long countUseByOrgId(String orgId){
        return apiVisitLogDao.countUseByOrgId(orgId);
    }

    public long countGiveByOrgId(String orgId){
        return apiVisitLogDao.countGiveByOrgId(orgId);
    }

    private JSONObject httpServiceGet(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        HttpService httpService = new HttpService();
        httpService.setTimeout(600, 600, 600);
        String result = httpService.get(url, headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    private void addApiVisitLog(String content){
        List<ApiVisitLogEntity> apiVisitLogEntityList = JSONObject.parseArray(content, ApiVisitLogEntity.class);
        for(ApiVisitLogEntity entity : apiVisitLogEntityList) {
            if (!apiVisitLogDao.existsById(entity.getId())){
                apiVisitLogDao.save(entity);
            }
        }
    }
}
