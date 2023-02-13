package d1.project.container.task.service;

import com.alibaba.fastjson.JSONObject;
import org.pentaho.di.trans.step.StepInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

/**
 * @author baozh
 */
@Service
public class JobEntityLogService {
    @Value("${d1.project.access.url}")
    private String accessUrl;

    private String entityLogUrl = "/webadmin/task/entityLogs/insert";

    public void insert(String taskId, StepInterface step, String channelId, String groupId, String tranName) {
        JSONObject jobEntityLog = new JSONObject();
        jobEntityLog.put("taskId", taskId);
        jobEntityLog.put("status", 0);
        jobEntityLog.put("channelId", channelId);
        jobEntityLog.put("groupId", groupId);
        jobEntityLog.put("inputNum", step.getLinesInput());
        jobEntityLog.put("outputNum", step.getLinesOutput());
        jobEntityLog.put("readNum", step.getLinesRead());
        jobEntityLog.put("writeNum", step.getLinesWritten());
        jobEntityLog.put("updateNum", step.getLinesUpdated());
        jobEntityLog.put("errorNum", step.getErrors());
        jobEntityLog.put("stepName", step.getStepname());
        jobEntityLog.put("createTime", Calendar.getInstance());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(accessUrl + entityLogUrl, jobEntityLog, String.class);
    }

    public void insertLog(String taskId, String channelId, String groupId, String stepName) {
        JSONObject jobEntityLog = new JSONObject();
        jobEntityLog.put("taskId", taskId);
        jobEntityLog.put("status", 0);
        jobEntityLog.put("channelId", channelId);
        jobEntityLog.put("groupId", groupId);
        jobEntityLog.put("stepName", stepName);
        jobEntityLog.put("createTime", Calendar.getInstance());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(accessUrl + entityLogUrl, jobEntityLog, String.class);
    }

    public void sendEnd(String taskId, String channelId, String groupId) {
        JSONObject jobEntityLog = new JSONObject();
        jobEntityLog.put("taskId", taskId);
        jobEntityLog.put("channelId", channelId);
        jobEntityLog.put("groupId", groupId);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(accessUrl + "/webadmin/task/entityLogs/end", jobEntityLog, String.class);
    }
}
