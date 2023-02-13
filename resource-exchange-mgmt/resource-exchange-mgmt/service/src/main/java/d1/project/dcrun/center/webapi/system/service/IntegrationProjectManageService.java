package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.keydict.KeyDictService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.system.dao.IntegrationProjectManageDao;
import d1.project.dcrun.center.webapi.system.entity.IntegrationProjectManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author libin
 */
@Service
public class IntegrationProjectManageService {
    @Autowired
    private IntegrationProjectManageDao integrationProjectManageDao;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private KeyDictService keyDictService;

    /**
     * 根据管理员账号查询用户管理信息
     *
     * @param code     管理员账号
     * @param page     页码
     * @param size 页的大小
     * @return 用户管理信息
     * @throws Exception 分页查询的报错信息
     */
    public Page<IntegrationProjectManage> findAllByCode(HttpServletRequest request, String code, Integer page, Integer size) throws Exception {
        JSONObject user = tokenService.getUserByToken(request);
        String tenantId = user.getString("id");

        JSONObject params = new JSONObject();
        params.put("tenantId", tenantId);
        params.put("code", code);
        params.put("page", page);
        params.put("size", size);

        SpecificationBuilder<IntegrationProjectManage> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("tenantId", "tenantId")
                .sEqual("code", "code")
                .dOrder("createTime").build();

        Page result = this.integrationProjectManageDao.findAll(specification, builder.getPageable());
        return result;
    }

    /**
     * 创建用户
     *
     * @param request
     * @param params  用户信息
     * @throws Exception 创建时的错误信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {
        String code = params.getString("code");
        String remark = params.getString("remark");

        IntegrationProjectManage integrationProjectManage = this.integrationProjectManageDao.findFirstByCode(code);
        if (integrationProjectManage != null) {
            throw new ValidException("该用户名已被占用，请重新输入");
        }

        integrationProjectManage = new IntegrationProjectManage();
        integrationProjectManage.setCode(code);
        integrationProjectManage.setRemark(remark);

        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        integrationProjectManage.setPassword(password);

        String appid = keyDictService.generateAppId();
        String appkey = MyUtils.generatePrimaryKeyId();
        integrationProjectManage.setAppid(appid);
        integrationProjectManage.setAppkey(appkey);
        JSONObject user = tokenService.getUserByToken(request);
        tokenService.updateCreateIdAndTime(request, integrationProjectManage);

        integrationProjectManage.setTenantId(user.getString("id"));
        integrationProjectManage.setId(keyDictService.generateAppId());

        integrationProjectManageDao.save(integrationProjectManage);
    }

    /**
     * 修改用户
     *
     * @param request
     * @param params  用户信息
     * @throws Exception 修改时的错误信息
     */
    public void update(HttpServletRequest request, JSONObject params) throws Exception {
        String id = params.getString("id");
        String remark = params.getString("remark");

        IntegrationProjectManage integrationProjectManage = this.integrationProjectManageDao.findById(id).orElseThrow(() -> new ValidException("集成项目不存在"));

        integrationProjectManage.setRemark(remark);
        tokenService.updateUpdateIdAndTime(request, integrationProjectManage);
        integrationProjectManageDao.save(integrationProjectManage);
    }

    /**
     * 重置密码
     *
     * @param request
     * @param id      集成项目id
     * @throws Exception 重置时的错误信息
     */
    public void resetPassword(HttpServletRequest request, String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("唯一标识不能为空");
        }

        IntegrationProjectManage integrationProjectManage = this.integrationProjectManageDao.findById(id).orElseThrow(() -> new ValidException("您所重置的集成项目不存在"));

        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        integrationProjectManage.setPassword(password);
        tokenService.updateUpdateIdAndTime(request, integrationProjectManage);
        integrationProjectManageDao.save(integrationProjectManage);
    }

    public IntegrationProjectManage findById(String id) {
        return integrationProjectManageDao.findById(id).orElse(null);
    }

    public void delete(String id) {
        integrationProjectManageDao.deleteById(id);
    }
}
