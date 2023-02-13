package d1.project.tangshan.operation.manage.service.remote;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.remote.CommandsDao;
import d1.project.tangshan.operation.manage.entity.remote.Commands;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lin
 */
@Service
public class CommandsService {
    private final CommandsDao commandsDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;

    public CommandsService(CommandsDao commandsDao, OperationLogService operationLogService, TokenService tokenService) {
        this.commandsDao = commandsDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }


    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        Commands commands = MyUtils.model2Entity(model, Commands.class);
        String name = commands.getType();
        try {
            commands.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, commands);

            commandsDao.save(commands);
            operationLogService.setLog("日常运维指令—添加", userName, "远程控制-日常运维指令", "添加了一个日常运维指令:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("日常运维指令—添加", userName, "远程控制-日常运维指令", "添加了一个日常运维指令:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            Commands commands = findById(id);
            name = commands.getType();
            commandsDao.deleteById(id);
            operationLogService.setLog("日常运维指令—删除", userName, "远程控制-日常运维指令", "删除了一个指令分类:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("日常运维指令—删除", userName, "远程控制-日常运维指令", "删除了一个指令分类:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            Commands commands = MyUtils.model2Entity(model, Commands.class);

            Commands oldCommands = findById(commands.getId());

            tokenService.updateUpdateIdAndTime(request, oldCommands);
            name = oldCommands.getType();
            oldCommands.setType(commands.getType());
            oldCommands.setTypeId(commands.getTypeId());
            oldCommands.setContent(commands.getContent());
            oldCommands.setRemark(commands.getRemark());
            commandsDao.save(oldCommands);
            operationLogService.setLog("日常运维指令—编辑", userName, "远程控制-日常运维指令", "编辑了一个指令分类:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("日常运维指令—编辑", userName, "远程控制-日常运维指令", "编辑了一个指令分类:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<Commands> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Commands> builder = new SpecificationBuilder<>();
        Specification<Commands> specification = builder.init(params)
                .sEqual("typeId", "typeId")
                .dOrder("createTime")
                .build();
        return commandsDao.findAll(specification, builder.getPageable());
    }

    public Commands findById(String id) throws Exception {
        return commandsDao.findById(id).orElseThrow(() -> new ValidException("该运维指令不存在"));
    }

    public List<Commands> findAllByContentLike(String content) {
        return commandsDao.findAllByContentLike("%" + content + "%");
    }
}
