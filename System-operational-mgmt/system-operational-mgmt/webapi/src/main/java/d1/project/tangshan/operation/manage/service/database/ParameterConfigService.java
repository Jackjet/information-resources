package d1.project.tangshan.operation.manage.service.database;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.remote.ParameterConfigDao;
import d1.project.tangshan.operation.manage.entity.database.ParameterConfig;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lin
 */
@Service
public class ParameterConfigService {
    private final ParameterConfigDao parameterConfigDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;

    public ParameterConfigService(ParameterConfigDao parameterConfigDao, OperationLogService operationLogService, TokenService tokenService) {
        this.parameterConfigDao = parameterConfigDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }


    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        ParameterConfig parameterConfig = MyUtils.model2Entity(model, ParameterConfig.class);
        String name = parameterConfig.getName();
        try {
            MyUtils.throwMsg(parameterConfigDao.existsByName(parameterConfig.getName()), "参数配置名称已存在");
            parameterConfig.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, parameterConfig);

            parameterConfigDao.save(parameterConfig);
            operationLogService.setLog("参数配置—添加", userName, "数据库运维-参数配置", "添加了一个参数配置:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("参数配置—添加", userName, "数据库运维-参数配置", "添加了一个参数配置:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            ParameterConfig parameterConfig = findById(id);
            name = parameterConfig.getName();
            parameterConfigDao.deleteById(id);
            operationLogService.setLog("参数配置—删除", userName, "数据库运维-参数配置", "删除了一个参数配置:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("参数配置—删除", userName, "数据库运维-参数配置", "删除了一个参数配置:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            ParameterConfig parameterConfig = MyUtils.model2Entity(model, ParameterConfig.class);
            ParameterConfig oldParameterConfig = findById(parameterConfig.getId());

            tokenService.updateUpdateIdAndTime(request, oldParameterConfig);
            name = oldParameterConfig.getName();
            if (!oldParameterConfig.getName().equals(parameterConfig.getName())) {
                MyUtils.throwMsg(parameterConfigDao.existsByName(parameterConfig.getName()), "该参数配置名称已存在");
                oldParameterConfig.setName(parameterConfig.getName());
            }
            oldParameterConfig.setContent(parameterConfig.getContent());
            oldParameterConfig.setRemark(parameterConfig.getRemark());
            parameterConfigDao.save(oldParameterConfig);
            operationLogService.setLog("参数配置—编辑", userName, "数据库运维-参数配置", "编辑了一个参数配置:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("参数配置—编辑", userName, "数据库运维-参数配置", "编辑了一个参数配置:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<ParameterConfig> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ParameterConfig> builder = new SpecificationBuilder<>();
        Specification<ParameterConfig> specification = builder.init(params)
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return parameterConfigDao.findAll(specification, builder.getPageable());
    }

    public ParameterConfig findById(String id) throws Exception {
        return parameterConfigDao.findById(id).orElseThrow(() -> new ValidException("该参数配置不存在"));
    }

}
