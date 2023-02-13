package d1.project.dcrun.center.webapi.datacache.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.keydict.KeyDictService;
import d1.project.dcrun.center.webapi.datacache.dao.KeysDao;
import d1.project.dcrun.center.webapi.datacache.entity.KeyConfig;
import d1.project.dcrun.center.webapi.datacache.entity.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author maoyy
 */
@Service
public class KeysService {
    private final KeysDao keysDao;
    private final TokenService tokenService;
    private final KeyConfigService keyConfigService;
    private final KeyDictService keyDictService;

    @Autowired
    public KeysService(KeysDao keysDao, TokenService tokenService, KeyConfigService keyConfigService, KeyDictService keyDictService) {
        this.keysDao = keysDao;
        this.tokenService = tokenService;
        this.keyConfigService = keyConfigService;
        this.keyDictService = keyDictService;
    }

    public Page<Keys> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Keys> builder = new SpecificationBuilder<>();
        Specification<Keys> specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sContain("key", "key")
                .dOrder("createTime").build();

        return this.keysDao.findAll(specification, builder.getPageable());
    }

    public Keys findById(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("id不能为空");
        }
        Optional<Keys> key = keysDao.findById(id);
        if (!key.isPresent()) {
            throw new ValidException("找不到记录：" + id);
        }
        return key.orElse(null);
    }

    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        //判断key唯一
        Keys keys = keysDao.findByIntegrationIdAndSysServiceIdAndKey(params.getString("integrationId"), params.getString("sysServiceId"), params.getString("key"));
        if (keys != null) {
            throw new ValidException("该系统服务下的key已存在");
        }

        Keys entity = JSON.toJavaObject(params, Keys.class);
        entity.setId(keyDictService.generateAppId());
        tokenService.updateCreateIdAndTime(request, entity);
        keysDao.save(entity);
    }

    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        Keys entity = keysDao.findById(params.getString("id")).orElseThrow(() -> new Exception("找不到记录：" + params.getString("id")));

        if (!entity.getKey().equals(params.getString("key"))) {
            Keys keys = keysDao.findByIntegrationIdAndSysServiceIdAndKey(entity.getIntegrationId(), entity.getSysServiceId(), params.getString("key"));
            if (keys != null) {
                throw new ValidException("该系统服务下的key已存在");
            }
        }
        entity.setExpire(Integer.parseInt(params.getString("expire")));
        entity.setKey(params.getString("key"));
        entity.setValue(params.getString("value"));
        tokenService.updateUpdateIdAndTime(request, entity);
        keysDao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("id不能为空");
        }
        Keys keys = keysDao.findById(id).orElseThrow(() -> new Exception("找不到记录：" + id));

        List<KeyConfig> keyConfigs = keyConfigService.findAllBySysServiceIdAndKey(keys.getSysServiceId(), keys.getKey());
        if (keyConfigs != null && keyConfigs.size() > 0) {
            keyConfigService.deleteBatch(keyConfigs);
        }

        keysDao.delete(keys);
    }
}
