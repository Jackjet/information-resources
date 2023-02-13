package d1.project.dcrun.center.webapi.basesys.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.basesys.model.CustomSysServiceConfigResultVm;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.ServiceType;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xuaa
 */
@Service
public class BaseServiceConfigService {
    @Autowired
    private SysServiceService sysServiceService;

    /**
     * 系统服务自定义配置列表
     *
     * @param params 查询列表
     * @return 返回结果
     */
    public Page<CustomSysServiceConfigResultVm> findAll(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        String integrationId = params.getString("integrationId");
        String configFilePath = Constants.getConfigJsonPath(integrationId, ServiceType.WEBAPI.getName(), appid);
        File configFile = new File(configFilePath);

        String text = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
        JSONObject jsonObj = JSON.parseObject(text);

        List<CustomSysServiceConfigResultVm> findAll = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
            CustomSysServiceConfigResultVm vm = new CustomSysServiceConfigResultVm();
            vm.setKey(entry.getKey());
            vm.setValue((String) entry.getValue());
            findAll.add(vm);
        }

        return MyUtils.pagination(findAll, params);
    }

    /**
     * 系统服务配置编辑
     *
     * @param params 修改实体
     * @throws Exception 抛出异常
     */
    public void updateCustomConfig(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        String integrationId = params.getString("integrationId");

        JSONObject dataJson = params.getJSONObject("data");
        JSONObject result = new JSONObject();
        // 去除json中id和key两边可能存在的空格
        for (Map.Entry<String, Object> entry : dataJson.entrySet()) {
            result.put(MyUtils.eliminateSpaces(entry.getKey()), MyUtils.eliminateSpaces((String) entry.getValue()));
        }
        // 写入config.json配置文件
        String configFilePath = Constants.getConfigJsonPath(integrationId, ServiceType.WEBAPI.getName(), appid);
        FileUtils.write(new File(configFilePath), result.toJSONString(), StandardCharsets.UTF_8);
    }
}

