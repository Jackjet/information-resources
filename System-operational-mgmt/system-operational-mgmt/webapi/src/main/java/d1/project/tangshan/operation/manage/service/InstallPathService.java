package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.InstallPathDao;
import d1.project.tangshan.operation.manage.entity.InstallPathEntity;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class InstallPathService {
    private final InstallPathDao installPathDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;

    @Autowired
    public InstallPathService(InstallPathDao installPathDao, TokenService tokenService, OperationLogService operationLogService) {
        this.installPathDao = installPathDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }

    public Page<InstallPathEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        SpecificationBuilder<InstallPathEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("nodeId", "nodeId")
                .sEqual("sysId", "sysId")
                .dOrder("createTime").build();

        return this.installPathDao.findAll(specification, builder.getPageable());
    }

    public InstallPathEntity findById(String id) throws Exception {
        return installPathDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
    }


    public void insert(JSONObject params, HttpServletRequest request) throws Exception{
        InstallPathEntity entity = JSON.toJavaObject(params, InstallPathEntity.class);

        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = entity.getNodeName();
        try {
            entity.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, entity);
            installPathDao.save(entity);
            operationLogService.setLog("安装路径管理—添加", userName, "自动化部署-安装路径管理", "添加了一个:" + nodeName + "下的安装路径", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("安装路径管理—添加", userName, "自动化部署-安装路径管理", "添加了一个:" + nodeName + "下的安装路径", "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "未知";

        try {
            String id = params.getString("id");
            InstallPathEntity entity = installPathDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            nodeName = entity.getNodeName();
            entity.setPath(params.getString("path"));
            entity.setOs(params.getString("os"));
            entity.setRemark(params.getString("remark"));
            tokenService.updateUpdateIdAndTime(request, entity);
            installPathDao.save(entity);

            operationLogService.setLog("安装路径管理—编辑", userName, "自动化部署-安装路径管理", "编辑了一个:" + nodeName + "下的安装路径", "1", request);
        } catch (ValidException e) {
            operationLogService.setLog("安装路径管理—编辑", userName, "自动化部署-安装路径管理", "编辑了一个:" + nodeName + "下的安装路径", "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "未知";

        try {
            InstallPathEntity entity = installPathDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            nodeName = entity.getNodeName();
            installPathDao.delete(entity);

            operationLogService.setLog("安装路径管理—删除", userName, "自动化部署-安装路径管理", "删除了一个:" + nodeName + "下的安装路径", "1", request);
        } catch (ValidException e) {
            operationLogService.setLog("安装路径管理—删除", userName, "自动化部署-安装路径管理", "删除了一个:" + nodeName + "下的安装路径", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public InstallPathEntity findByNodeIdAndSysId(String nodeId, String sysId) throws Exception {
        return installPathDao.findByNodeIdAndSysId(nodeId, sysId).orElseThrow(() -> new ValidException("找不到记录：nodeId=" + nodeId + ",sysId=" + sysId));
    }


}
