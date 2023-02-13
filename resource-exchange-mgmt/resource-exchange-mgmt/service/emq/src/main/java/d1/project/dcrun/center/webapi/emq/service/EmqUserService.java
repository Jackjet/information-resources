package d1.project.dcrun.center.webapi.emq.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.keydict.KeyDictService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.emq.dao.EmqUserDao;
import d1.project.dcrun.center.webapi.emq.entity.EmqUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author xuaa
 */
@Service
public class EmqUserService {
    private final EmqUserDao emqUserDao;
    private final TokenService tokenService;
    private final KeyDictService keyDictService;
    @Value("${file.server.uri}")
    private String fileDownload;

    @Autowired
    public EmqUserService(EmqUserDao emqUserDao, TokenService tokenService, KeyDictService keyDictService) {
        this.keyDictService = keyDictService;
        this.emqUserDao = emqUserDao;
        this.tokenService = tokenService;
    }

    /**
     * 查询列表
     *
     * @param params 请求参数
     * @return 返回参数
     * @throws Exception 抛出异常
     */
    public Page<EmqUser> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<EmqUser> builder = new SpecificationBuilder<>();
        Specification<EmqUser> specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sEqual("appid", "appid")
                .dOrder("createTime").build();

        return this.emqUserDao.findAll(specification, builder.getPageable());
    }

    /**
     * 添加
     *
     * @param request 请求
     * @param params  请求实体
     * @throws Exception 抛出异常
     */
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {

        EmqUser entity = JSONObject.parseObject(params.toJSONString(), EmqUser.class);
        // 开发者id和密钥去除两边空格
        entity.setAppid(MyUtils.eliminateSpaces(entity.getAppid()));
        entity.setAppkey(MyUtils.eliminateSpaces(entity.getAppkey()));

        EmqUser emqUser = emqUserDao.findByIntegrationIdAndAppidAndSysServiceId(entity.getIntegrationId(), entity.getAppid(), entity.getSysServiceId());
        if (emqUser != null) {
            throw new ValidException("该系统服务下appid已存在");
        }

        entity.setId(keyDictService.generateAppId());
        tokenService.updateCreateIdAndTime(request, entity);
        emqUserDao.save(entity);
    }

    public EmqUser findById(String id) {
        Optional<EmqUser> emqUserOptional = emqUserDao.findById(id);
        return emqUserOptional.orElse(null);
    }

    public void delete(String id) {
        emqUserDao.deleteById(id);
    }
}
