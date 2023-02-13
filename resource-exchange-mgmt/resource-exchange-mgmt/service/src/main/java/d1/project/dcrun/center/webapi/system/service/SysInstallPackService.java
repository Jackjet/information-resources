package d1.project.dcrun.center.webapi.system.service;


import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.ServiceType;
import d1.project.dcrun.center.webapi.system.dao.SysInstallPackDao;
import d1.project.dcrun.center.webapi.system.entity.SysInstallPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

/**
 * @author libin
 */
@Service
public class SysInstallPackService {
    @Autowired
    private SysInstallPackDao sysInstallPackDao;

    @Autowired
    private TokenService tokenService;

    /**
     * 根据模板名称和类型查询
     *
     * @param name 模板名称
     * @param type 模板类型
     * @param page 页码
     * @param size 页的大小
     * @return 系统服务模板信息
     * @throws Exception 查询时的错误信息
     */
    public Page<SysInstallPack> findAllByUserAndNameAndType(HttpServletRequest request, String name, String type, Integer page, Integer size) throws Exception {
        JSONObject user = tokenService.getUserByToken(request);

        JSONObject params = new JSONObject();
        params.put("name", name);
        params.put("type", type);
        params.put("createById", user.getString("id"));
        params.put("page", page);
        params.put("size", size);

        SpecificationBuilder<SysInstallPack> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("type", "type")
                .sEqual("createById", "createById")
                .dOrder("createTime").build();

        Page result = this.sysInstallPackDao.findAll(specification, builder.getPageable());
        return result;
    }


    /**
     * 创建系统服务模板
     *
     * @param request
     * @param name
     * @param type
     * @param filename
     */
    public void insert(HttpServletRequest request, String name, String type, String versionStr, String filename, String remark) {

        Long version = 1L;
        if (type.equals(ServiceType.WEBAPI.getName())) {
            version = Long.parseLong(versionStr);
        } else {
            SysInstallPack sysInstallPack = this.sysInstallPackDao.findFirstByTypeOrderByVersionDesc(type);
            if (sysInstallPack != null && sysInstallPack.getVersion() != null) {
                version += sysInstallPack.getVersion();
            }
        }

        SysInstallPack sysInstallPack = new SysInstallPack();
        sysInstallPack.setId(MyUtils.generatePrimaryKeyId());
        sysInstallPack.setName(name);
        sysInstallPack.setType(type);
        sysInstallPack.setFilename(filename);
        sysInstallPack.setRemark(remark);
        sysInstallPack.setVersion(version);

        JSONObject user = tokenService.getUserByToken(request);
        sysInstallPack.setCreateById(user.getString("id"));
        sysInstallPack.setCreateTime(Calendar.getInstance());

        sysInstallPackDao.save(sysInstallPack);
    }

    /**
     * 修改系统服务模板
     *
     * @param request
     * @param id      模板id
     * @param name    模板名称
     * @throws Exception
     */
    public void update(HttpServletRequest request, String id, String name, String remark) throws Exception {

        SysInstallPack sysInstallPack = this.sysInstallPackDao.getOne(id);
        if (sysInstallPack == null) {
            throw new Exception("你所修改的模板不存在");
        }

        sysInstallPack.setName(name);
        sysInstallPack.setRemark(remark);

        JSONObject user = tokenService.getUserByToken(request);
        sysInstallPack.setUpdateById(user.getString("id"));
        sysInstallPack.setUpdateTime(Calendar.getInstance());

        sysInstallPackDao.save(sysInstallPack);
    }

    public List<SysInstallPack> findAllVersion(HttpServletRequest request, String type) {
        if (ServiceType.WEBAPI.getName().equals(type)) {
            JSONObject user = tokenService.getUserByToken(request);
            return sysInstallPackDao.findAllVersionByCreateById(user.getString("id"), type);
        }
        return sysInstallPackDao.findAllVersion(type);
    }

    public SysInstallPack findById(String id) {
        return sysInstallPackDao.findById(id).orElse(null);
    }
}
