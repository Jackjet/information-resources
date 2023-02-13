package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.BigDataApiDao;
import d1.project.tangshan.operation.manage.dao.BigDataApiPlanDao;
import d1.project.tangshan.operation.manage.entity.BigDataApiEntity;
import d1.project.tangshan.operation.manage.entity.BigDataApiPlanEntity;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.BigDataPlanStatus;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import d1.project.tangshan.operation.manage.utils.QuartzUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class BigDataApiPlanService {
    private final BigDataApiPlanDao bigDataApiPlanDao;
    private final BigDataApiDao bigDataApiDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;

    public BigDataApiPlanService(BigDataApiPlanDao bigDataApiPlanDao, BigDataApiDao bigDataApiDao, TokenService tokenService, OperationLogService operationLogService) {
        this.bigDataApiPlanDao = bigDataApiPlanDao;
        this.bigDataApiDao = bigDataApiDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }

    public Page<BigDataApiPlanEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        SpecificationBuilder<BigDataApiPlanEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sContain("name", "name")
                .sContain("apiName", "apiName")
                .sEqual("status", "status")
                .dOrder("createTime").build();

        return this.bigDataApiPlanDao.findAll(specification, builder.getPageable());
    }

    public BigDataApiPlanEntity findById(String id) throws Exception {
        return bigDataApiPlanDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
    }


    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");

        BigDataApiPlanEntity entity = JSON.toJavaObject(params, BigDataApiPlanEntity.class);
        String name = entity.getName();
        try {
            MyUtils.throwMsg(bigDataApiPlanDao.existsByName(entity.getName()), "计划名称已存在");
            entity.setId(MyUtils.generate32Id());
            entity.setStatus(BigDataPlanStatus.STOPPED.getName());
            tokenService.updateCreateIdAndTime(request, entity);
            bigDataApiPlanDao.save(entity);

            operationLogService.setLog("数据服务接口计划任务—添加", userName, "大数据处理服务-数据服务接口计划任务", "添加了一个数据服务接口计划任务:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据服务接口计划任务—添加", userName, "大数据处理服务-数据服务接口计划任务", "添加了一个数据服务接口计划任务:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            String id = params.getString("id");
            BigDataApiPlanEntity entity = bigDataApiPlanDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            name = entity.getName();
            BigDataApiEntity apiEntity = bigDataApiDao.findById(entity.getApiId()).orElseThrow(() -> new ValidException("找不到接口：" + entity.getApiId()));
            if (!StringUtils.isEmpty(entity.getCron()) && !entity.getCron().equals(params.getString("cron"))) {
                entity.setCron(params.getString("cron"));
                QuartzUtil.modifyJobTime(entity, apiEntity);
            }
            entity.setParams(params.getString("params"));
            entity.setBody(params.getString("body"));
            entity.setRemark(params.getString("remark"));
            tokenService.updateUpdateIdAndTime(request, entity);
            bigDataApiPlanDao.save(entity);

            operationLogService.setLog("数据服务接口计划任务—编辑", userName, "大数据处理服务-数据服务接口计划任务", "编辑了一个数据服务接口计划任务:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据服务接口计划任务—编辑", userName, "大数据处理服务-数据服务接口计划任务", "编辑了一个数据服务接口计划任务:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            BigDataApiPlanEntity entity = bigDataApiPlanDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            name = entity.getName();
            bigDataApiPlanDao.delete(entity);

            operationLogService.setLog("数据服务接口计划任务—删除", userName, "大数据处理服务-数据服务接口计划任务", "删除了一个数据服务接口计划任务:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据服务接口计划任务—删除", userName, "大数据处理服务-数据服务接口计划任务", "删除了一个数据服务接口计划任务:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void start(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            String id = params.getString("id");
            BigDataApiPlanEntity entity = bigDataApiPlanDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            name = entity.getName();
            BigDataApiEntity apiEntity = bigDataApiDao.findById(entity.getApiId()).orElseThrow(() -> new ValidException("找不到接口：" + entity.getApiId()));
            QuartzUtil.addJob(entity, apiEntity);
            entity.setStatus(BigDataPlanStatus.STARTED.getName());
            tokenService.updateUpdateIdAndTime(request, entity);
            bigDataApiPlanDao.save(entity);

            operationLogService.setLog("数据服务接口计划任务—启动", userName, "大数据处理服务-数据服务接口计划任务", "启动了" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据服务接口计划任务—启动", userName, "大数据处理服务-数据服务接口计划任务", "启动了" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void stop(JSONObject params, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            String id = params.getString("id");
            BigDataApiPlanEntity entity = bigDataApiPlanDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
            name = entity.getName();
            QuartzUtil.removeJob(entity.getId());
            entity.setStatus(BigDataPlanStatus.STOPPED.getName());
            tokenService.updateUpdateIdAndTime(request, entity);
            bigDataApiPlanDao.save(entity);

            operationLogService.setLog("数据服务接口计划任务—停止", userName, "大数据处理服务-数据服务接口计划任务", "停止了" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据服务接口计划任务—停止", userName, "大数据处理服务-数据服务接口计划任务", "停止了" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }
}
