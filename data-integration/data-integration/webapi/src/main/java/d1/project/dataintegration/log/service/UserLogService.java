package d1.project.dataintegration.log.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.dataintegration.common.Constants;
import d1.project.dataintegration.common.model.OperationLog;
import d1.project.dataintegration.common.service.IOperationLogService;
import d1.project.dataintegration.common.service.MyTokenService;
import d1.project.dataintegration.common.utils.BaseUtils;
import d1.project.dataintegration.log.dao.UserLogDao;
import d1.project.dataintegration.log.entity.UserLogEntity;
import d1.project.dataintegration.log.mapper.UserLogMapper;
import d1.project.dataintegration.log.model.UserLogExcel;
import d1.project.dataintegration.log.model.vm.UserLogFindAllVm;
import d1.project.dataintegration.log.model.vm.UserLogRequestInsertVm;
import d1.project.dataintegration.system.entity.WebAdminUserEntity;
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
 * @date 2020-09-07 16:38
 */
@Service
public class UserLogService {

    private final UserLogDao userLogDao;
    private final MyTokenService myTokenService;
    private final IOperationLogService iOperationLogService;

    private final UserLogMapper mapper;

    public UserLogService(UserLogDao userLogDao, MyTokenService myTokenService, IOperationLogService iOperationLogService) {
        this.userLogDao = userLogDao;
        this.mapper = Mappers.getMapper(UserLogMapper.class);
        this.myTokenService = myTokenService;
        this.iOperationLogService = iOperationLogService;
    }

    /**
     * ?????????????????????
     *
     * @param model ??????
     */
    public void insert(UserLogRequestInsertVm model, HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        UserLogEntity userLog = mapper.dtoFormatIntoRequestInsertEntity(model);
        userLog.setId(BaseUtils.generate32Id());
        userLog.setSourceIp(BaseUtils.getIpAddress(request));
        /*0->??????,1->??????*/
        userLog.setType(1);

        if (userByToken != null) {
            userLog.setCreateByAccount(userByToken.getString("account"));
            userLog.setCreateByName(userByToken.getString("name"));
            userLog.setCreateByPhone(userByToken.getString("phone"));
            userLog.setCreateById(userByToken.getString("id"));
        }

        userLog.setCreateTime(Calendar.getInstance());
        userLogDao.save(userLog);
    }

    /**
     * ?????????????????????
     *
     * @param model ??????
     */
    public void insert(UserLogRequestInsertVm model, WebAdminUserEntity webAdminUser, HttpServletRequest request) {
        UserLogEntity userLog = mapper.dtoFormatIntoRequestInsertEntity(model);
        userLog.setId(BaseUtils.generate32Id());
        userLog.setSourceIp(BaseUtils.getIpAddress(request));
        /*0->??????,1->??????*/
        userLog.setType(1);
        userLog.setCreateByAccount(webAdminUser.getAccount());
        userLog.setCreateByName(webAdminUser.getName());
        userLog.setCreateByPhone(webAdminUser.getPhone());
        userLog.setCreateById(webAdminUser.getId());
        userLog.setCreateTime(Calendar.getInstance());
        userLogDao.save(userLog);
    }


    /**
     * ????????????
     */
    public Page<UserLogEntity> findAll(UserLogFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        myTokenService.makeUpRoleJsonObjectByToken(request, paramObject);
        SpecificationBuilder<UserLogEntity> builder = new SpecificationBuilder<>();
        Specification<UserLogEntity> specification = getUserLogSpecification(paramObject, builder);
        return userLogDao.findAll(specification, builder.getPageable());
    }

    /**
     * ????????????
     */
    public List<UserLogExcel> findAllList(UserLogFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        myTokenService.makeUpRoleJsonObjectByToken(request, paramObject);
        SpecificationBuilder<UserLogEntity> builder = new SpecificationBuilder<>();
        Specification<UserLogEntity> specification = getUserLogSpecification(paramObject, builder);
        List<UserLogEntity> all = userLogDao.findAll(specification);
        return mapper.entityListFormatIntoExcelDtoList(all);
    }

    /**
     * ??????????????????
     */
    private Specification<UserLogEntity> getUserLogSpecification(JSONObject paramObject, SpecificationBuilder<UserLogEntity> builder) throws Exception {
        return builder.init(paramObject)
                .sContain("createByName", "createByName")
                .sEqual("createById", "createById")
                .sEqual("api", "api")
                .sEqual("result", "result")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
    }

    /**
     * ??????
     *
     * @param id id
     */
    public Optional<UserLogEntity> find(String id) {
        return userLogDao.findById(id);
    }

    public void export(UserLogFindAllVm model, HttpServletResponse response, HttpServletRequest request) throws Exception {
        // ???????????? ?????????????????????swagger ??????????????????????????????????????????????????????postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
        String fileName = URLEncoder.encode("????????????", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<UserLogExcel> allList = findAllList(model, request);
        EasyExcel.write(response.getOutputStream(), UserLogExcel.class).sheet("????????????").doWrite(allList);
        iOperationLogService.insert(new OperationLog("????????????_????????????", "??????", "??????????????????", JSON.toJSONString(model), 1), request);
    }
}
