package d1.project.resourcesharingmgmt.log.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.service.MyTokenService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.log.dao.OperationLogDao;
import d1.project.resourcesharingmgmt.log.entity.OperationLogEntity;
import d1.project.resourcesharingmgmt.log.mapper.OperationLogMapper;
import d1.project.resourcesharingmgmt.log.model.OperationLogExcel;
import d1.project.resourcesharingmgmt.log.model.vm.OperationLogFindAllVm;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:12
 */
@Service
public class OperationLogServiceImpl implements IOperationLogService {

    private final OperationLogDao operationLogDao;
    private final MyTokenService myTokenService;

    private final OperationLogMapper mapper;

    public OperationLogServiceImpl(OperationLogDao userLogDao, MyTokenService myTokenService) {
        this.mapper = Mappers.getMapper(OperationLogMapper.class);
        this.operationLogDao = userLogDao;
        this.myTokenService = myTokenService;
    }


    /**
     * 用户带请求日志
     *
     * @param model 实体
     */
    @Override
    public void insert(OperationLog model, HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        OperationLogEntity operationLog = mapper.dtoFormatIntoRequestInsertEntity(model);
        operationLog.setId(BaseUtils.generate32Id());
        /*0->系统,1->人为*/
        operationLog.setType(1);

        if (userByToken != null) {
            operationLog.setCreateByAccount(userByToken.getString("account"));
            operationLog.setCreateByName(userByToken.getString("name"));
            operationLog.setCreateByPhone(userByToken.getString("phone"));
            operationLog.setCreateById(userByToken.getString("id"));
        }

        operationLog.setCreateTime(Calendar.getInstance());
        operationLogDao.save(operationLog);
    }

    /**
     * 查询所有(分页)
     */
    public Page<OperationLogEntity> findAll(OperationLogFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        myTokenService.makeUpRoleJsonObjectByToken(request, paramObject);
        SpecificationBuilder<OperationLogEntity> builder = new SpecificationBuilder<>();
        Specification<OperationLogEntity> specification = getUserLogSpecification(paramObject, builder);
        return operationLogDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询所有
     */
    public List<OperationLogExcel> findAllList(OperationLogFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        myTokenService.makeUpRoleJsonObjectByToken(request, paramObject);
        SpecificationBuilder<OperationLogEntity> builder = new SpecificationBuilder<>();
        Specification<OperationLogEntity> specification = getUserLogSpecification(paramObject, builder);
        List<OperationLogEntity> all = operationLogDao.findAll(specification);
        return mapper.entityListFormatIntoExcelDtoList(all);
    }

    /**
     * 获取查询规则
     */
    private Specification<OperationLogEntity> getUserLogSpecification(JSONObject paramObject, SpecificationBuilder<OperationLogEntity> builder) throws Exception {
        return builder.init(paramObject)
                .sContain("createByName", "createByName")
                .sEqual("createById", "createById")
                .sEqual("api", "api")
                .sEqual("result", "result")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
    }

    public Optional<OperationLogEntity> find(String id) {
        return operationLogDao.findById(id);
    }

    /**
     * 导出
     */
    public void export(OperationLogFindAllVm model, HttpServletResponse response, HttpServletRequest request) throws Exception {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("操作日志", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<OperationLogExcel> allList = findAllList(model, request);
        EasyExcel.write(response.getOutputStream(), OperationLogExcel.class).sheet("操作日志").doWrite(allList);

        insert(new OperationLog("日志管理_操作日志", "导出", "导出操作日志", JSON.toJSONString(model), 1), request);
    }

}
