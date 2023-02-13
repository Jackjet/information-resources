package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.keydict.KeyDictService;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUser;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUserService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author maoyy
 */
@Service
public class CoreSysUserService {

    private final SysUserService sysUserService;
    private final TokenService tokenService;
    private final KeyDictService keyDictService;

    @Autowired
    public CoreSysUserService(SysUserService sysUserService, TokenService tokenService, KeyDictService keyDictService) {
        this.sysUserService = sysUserService;
        this.tokenService = tokenService;
        this.keyDictService = keyDictService;
    }

    /**
     * 系统服务列表
     *
     * @param params 查询列表
     * @return 返回结果
     */
    public Page<SysUser> findAll(JSONObject params) throws Exception {
        return sysUserService.findAll(params);
    }

    /**
     * 系统服务详情
     *
     * @param id 系统服务Id
     * @return 返回结果
     * @throws Exception 抛出异常
     */
    public SysUser findByIdDetails(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("数据为空或获取失败");
        }
        return sysUserService.findById(id);
    }

    /**
     * 添加
     *
     * @param request 请求
     * @param params  请求实体
     * @throws Exception 抛出异常
     */
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {
        SysUser entity = JSONObject.toJavaObject(params, SysUser.class);
        entity.setId(keyDictService.generateAppId());
        if ("1".equals(params.getString("isAutomatic"))) {
            String appid = keyDictService.generateAppId();
            String appkey = MyUtils.generatePrimaryKeyId();
            entity.setAppid(appid);
            entity.setAppkey(appkey);
        } else {
            if (StringUtils.isEmpty(entity.getAppid()) || StringUtils.isEmpty(entity.getAppkey())) {
                throw new ValidException("此系统服务下appid及appkey不能为空");
            }

            Optional<SysUser> sysUser = sysUserService.findByIntegrationIdAndAppid(entity.getIntegrationId(), entity.getAppid());
            if (sysUser.isPresent()) {
                throw new ValidException("该开发者id已存在");
            }

            sysUser = sysUserService.findAllByIntegrationIdAndName(entity.getIntegrationId(), entity.getName());
            if (sysUser.isPresent()) {
                throw new ValidException("此系统服务下开发者名称已存在");
            }

            // 开发者id和密钥去除两边空格
            entity.setAppid(MyUtils.eliminateSpaces(entity.getAppid()));
            entity.setAppkey(MyUtils.eliminateSpaces(entity.getAppkey()));
        }

        tokenService.updateCreateIdAndTime(request, entity);
        sysUserService.insert(entity);
    }

    public void update(HttpServletRequest request, JSONObject params) throws Exception {
        SysUser sysUser = sysUserService.findById(params.getString("id"));
        if (sysUser == null) {
            throw new ValidException("系统服务开发者不存在");
        }
        sysUser.setRemark(params.getString("remark"));
        tokenService.updateCreateIdAndTime(request, sysUser);
        sysUserService.update(sysUser);
    }

    /**
     * 删除
     *
     * @param id id
     * @throws Exception 抛出异常
     */
    public void deleteById(String id) throws Exception {
        if (!sysUserService.existsById(id)) {
            throw new ValidException("未找到id=" + id + "的资源");
        }
        sysUserService.deleteById(id);
    }
}
