package d1.project.tangshan.operation.manage.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.tangshan.operation.manage.entity.BigDataApiEntity;
import d1.project.tangshan.operation.manage.entity.BigDataApiPlanEntity;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.webservice.client.WebserviceClientService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InterfaceJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(InterfaceJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时调用接口");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        BigDataApiPlanEntity apiPlan = (BigDataApiPlanEntity) jobDataMap.get("apiPlan");
        BigDataApiEntity api = (BigDataApiEntity) jobDataMap.get("api");

        switch (api.getType()) {
            case "http":
                http(api.getUrlOrIp(), apiPlan.getBody(), api.getMethodType());
                break;
            case "webservice":
                webservice(api.getUrlOrIp(), apiPlan.getBody(), api.getMethodType());
                break;
            default:
                break;
        }

    }

    private void http(String url, String body, String methodType) {
        try {
            HttpService http = new HttpService();

            Map<String, Object> map = new HashMap<>();
            if(!"".equals(body)) {
                JSONObject parameters = (JSONObject) JSON.parse(body);
                Iterator it = parameters.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            String result = "";
            switch (methodType) {
                case "post":
                    result = http.post(url, null, map, "application/json");
                    break;
                case "get":
                    result = http.get(url, null, map);
                    break;
                case "put":
                    result = http.put(url, null, map, "application/json");
                    break;
                case "delete":
                    result = http.delete(url, null, map, "application/json");
                    break;
                default:
                    break;
            }
            logger.info("调用结果：" + result);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void webservice(String url, String body, String methodType) {
        String[] bodyArr = body.split("\\^");
        if (bodyArr.length == 2) {
            String[] paramArr = bodyArr[1].split(",");
            Object[] serviceParams = new Object[paramArr.length];
            if (bodyArr[0].equals("int")) {
                serviceParams = new Integer[paramArr.length];
                for (int i = 0; i < paramArr.length; i++) {
                    serviceParams[i] = Integer.parseInt(paramArr[i]);
                }
            } else if (bodyArr[0].equals("long")) {
                serviceParams = new Long[paramArr.length];
                for (int i = 0; i < paramArr.length; i++) {
                    serviceParams[i] = Long.parseLong(paramArr[i]);
                }
            } else if (bodyArr[0].equals("float")) {
                serviceParams = new Float[paramArr.length];
                for (int i = 0; i < paramArr.length; i++) {
                    serviceParams[i] = Float.parseFloat(paramArr[i]);
                }
            } else if (bodyArr[0].equals("double")) {
                serviceParams = new Double[paramArr.length];
                for (int i = 0; i < paramArr.length; i++) {
                    serviceParams[i] = Double.parseDouble(paramArr[i]);
                }
            } else {
                serviceParams = paramArr;
            }
            WebserviceClientService service = new WebserviceClientService();
            try {
                String result = service.request(url, methodType, serviceParams);
                logger.info("调用结果：" + result);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
