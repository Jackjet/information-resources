package d1.project.tangshan.operation.manage.service.remote;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.remote.CommandTypeDao;
import d1.project.tangshan.operation.manage.entity.remote.CommandType;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lin
 */
@Service
public class CommandTypeService {
    private final CommandTypeDao commandTypeDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;

    public CommandTypeService(CommandTypeDao commandTypeDao, OperationLogService operationLogService, TokenService tokenService) {
        this.commandTypeDao = commandTypeDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }


    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        CommandType commandType = MyUtils.model2Entity(model, CommandType.class);
        String name = commandType.getName();
        try {

            MyUtils.throwMsg(commandTypeDao.existsByName(commandType.getName()), "类型名称已存在");
            commandType.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, commandType);

            commandTypeDao.save(commandType);
            operationLogService.setLog("运维指令分类—添加", userName, "远程控制-运维指令分类", "添加了一个指令分类:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("运维指令分类—添加", userName, "远程控制-运维指令分类", "添加了一个指令分类:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            CommandType commandType = findById(id);
            name = commandType.getName();
            commandTypeDao.deleteById(id);
            operationLogService.setLog("运维指令分类—删除", userName, "远程控制-运维指令分类", "删除了一个指令分类:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("运维指令分类—删除", userName, "远程控制-运维指令分类", "删除了一个指令分类:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            CommandType commandType = MyUtils.model2Entity(model, CommandType.class);

            CommandType oldCommandType = findById(commandType.getId());

            tokenService.updateUpdateIdAndTime(request, oldCommandType);
            name = oldCommandType.getName();
            oldCommandType.setRoleIds(commandType.getRoleIds());
            oldCommandType.setRoleNames(commandType.getRoleNames());
            oldCommandType.setRemark(commandType.getRemark());
            commandTypeDao.save(oldCommandType);
            operationLogService.setLog("运维指令分类—编辑", userName, "远程控制-运维指令分类", "编辑了一个指令分类:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("运维指令分类—编辑", userName, "远程控制-运维指令分类", "编辑了一个指令分类:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<CommandType> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<CommandType> builder = new SpecificationBuilder<>();
        Specification<CommandType> specification = builder.init(params)
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return commandTypeDao.findAll(specification, builder.getPageable());
    }

    public CommandType findById(String id) throws Exception {
        return commandTypeDao.findById(id).orElseThrow(() -> new ValidException("该指令分类不存在"));
    }

    public List<CommandType> findAllCommandTypes() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return commandTypeDao.findAll(sort);
    }
}
