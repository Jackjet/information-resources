package d1.project.resource.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.system.model.vm.PostgreSqlBackupVm;
import net.dcrun.component.schedule.ScheduleService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledFuture;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 19:09
 */
@Service
public class PostgreSqlBackupService {
    private final Logger logger = LoggerFactory.getLogger(PostgreSqlBackupService.class);
    private final IOperationLogService iOperationLogService;
    private final ScheduleService scheduleService = new ScheduleService();
    private static final String CONFIG_JSON = "./config/PostgreSqlBackup.json";
    private ScheduledFuture<?> schedule;
    private final BaseService baseService;

    public PostgreSqlBackupService(BaseService baseService, IOperationLogService iOperationLogService) {
        this.baseService = baseService;
        this.iOperationLogService = iOperationLogService;
    }

    public void update(PostgreSqlBackupVm model, HttpServletRequest request) throws Exception {
        FileUtils.writeStringToFile(new File(CONFIG_JSON), JSON.toJSONString(model), StandardCharsets.UTF_8);
        startAndRun();
        iOperationLogService.insert(new OperationLog("系统管理_数据备份", "编辑", "编辑数据备份", JSON.toJSONString(model), 1), request);
    }

    public void startAndRun() throws Exception {
        if (schedule != null) {
            //不管目前是启动了备份还是没有启动备份，都先停止已有的schedule
            scheduleService.cancel(schedule);
        }
        File configFile = new File(CONFIG_JSON);
        if (!configFile.exists()) {
            FileUtils.writeStringToFile(configFile, JSON.toJSONString(new PostgreSqlBackupVm()), StandardCharsets.UTF_8);
        }
        String content = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
        PostgreSqlBackupVm model = JSONObject.parseObject(content, PostgreSqlBackupVm.class);
        if (model.isEnable()) {
            if (StringUtils.isEmpty(model.getCron())) {
                throw new DoValidException("备份cron不能为空");
            }
            if (StringUtils.isEmpty(model.getPath())) {
                throw new DoValidException("备份路径不能为空");
            }

            schedule = scheduleService.schedule(stringObjectMap -> {
                try {
                    baseService.backupPostgres(model.getPath());
                } catch (Exception e) {
                    logger.error("数据备份错误：", e);
                }
            }, null, model.getCron());
        }
    }

    public PostgreSqlBackupVm find() throws IOException {
        String content = FileUtils.readFileToString(new File(CONFIG_JSON), StandardCharsets.UTF_8);
        return JSONObject.parseObject(content, PostgreSqlBackupVm.class);
    }
}
