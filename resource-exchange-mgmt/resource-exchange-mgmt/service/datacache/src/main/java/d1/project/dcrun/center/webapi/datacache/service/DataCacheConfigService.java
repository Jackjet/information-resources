package d1.project.dcrun.center.webapi.datacache.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.datacache.utils.DataCacheUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author maoyy
 */
@Service
public class DataCacheConfigService {
    @Autowired
    private SysServiceService sysServiceService;

    /**
     * 数据缓存服务消息服务配置信息
     *
     * @param params 查询列表
     * @return 返回结果
     * @throws Exception
     */
    public JSONObject findAll(JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");
        String appid = params.getString("appid");

        File configFile = new File(DataCacheUtils.getConfigJsonPath(integrationId, appid));
        String text = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
        return JSONObject.parseObject(text);
    }

    /**
     * 系统服务数据库配置编辑
     *
     * @param params
     * @throws Exception
     */
    public void update(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        String integrationId = params.getString("integrationId");
        SysService sysService = sysServiceService.findById(appid);
        if (sysService == null) {
            throw new ValidException("该系统服务不存在");
        }

        JSONObject dataJson = params.getJSONObject("data");
        JSONObject result = new JSONObject();
        // 去除json中id和key两边可能存在的空格
        for (Map.Entry<String, Object> entry : dataJson.entrySet()) {
            result.put(MyUtils.eliminateSpaces(entry.getKey()), MyUtils.eliminateSpaces((String) entry.getValue()));
        }
        // 写入data_cache_config.json配置文件
        FileUtils.write(new File(DataCacheUtils.getConfigJsonPath(integrationId, appid)), result.toJSONString(), StandardCharsets.UTF_8);
    }
}
