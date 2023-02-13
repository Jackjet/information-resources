package d1.project.d1project.apidesign.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.d1project.common.Constants;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Locale;

/**
 * VUE GOJS文件转demo文件
 *
 * @author baozh
 */
public class VueToContainer {

    public static JSONObject getContainerFile(String vueFile, String apiId, String apiName, String path, String method, String contentType) throws Exception {
        JSONObject vueParams = JSONObject.parseObject(vueFile);
        JSONObject containerJson = new JSONObject();
        containerJson.put("id", apiId);
        containerJson.put("name", apiName);
        containerJson.put("path", path);
        containerJson.put("method", method);
        containerJson.put("contentType", contentType);
        containerJson.put("nodes", assemblyContainer(vueParams));
        return containerJson;
    }

    /**
     * 组装容器内的节点组件
     *
     * @param vueParams 前端组件文件
     * @return 拼装完成的容器文件内容
     * @throws Exception 异常处理
     */
    private static JSONArray assemblyContainer(JSONObject vueParams) throws Exception {
        JSONArray nodeArray = new JSONArray();
        JSONArray vueNodeArray = vueParams.getJSONArray("nodes");
        JSONArray linkList = vueParams.getJSONArray("links");
        if (vueNodeArray.size() <= 0) {
            throw new Exception(Constants.VUE_FILE_NULL);
        }
        for (Object o : vueNodeArray) {
            if (o != null) {
                JSONObject json = JSONObject.parseObject(o.toString());
                String type = json.getString("category");
                switch (type) {
                    case "Request":
                        JSONObject requestNode = requestAssembly(json);
                        getNextNode(requestNode, linkList);
                        nodeArray.add(requestNode);
                        break;
                    case "Respond":
                        JSONObject respondNode = respondAssembly(json);
                        getNextNode(respondNode, linkList);
                        nodeArray.add(respondNode);
                        break;
                    case "HttpClient":
                        JSONObject httpNode = httpAssembly(json);
                        getNextNode(httpNode, linkList);
                        nodeArray.add(httpNode);
                        break;
                    case "switch":
                        JSONObject conditionNode = conditionAssembly(json);
                        nodeArray.add(conditionNode);
                        break;
                    case "js脚本":
                        JSONObject functionNode = functionAssembly(json);
                        getNextNode(functionNode, linkList);
                        nodeArray.add(functionNode);
                        break;
                    case "MySQL":
                    case "PostgreSQL":
                    case "Oracle":
                    case "Sqlserver":
                    case "Dameng":
                        JSONObject databaseNode = sqlAssembly(json);
                        getNextNode(databaseNode, linkList);
                        nodeArray.add(databaseNode);
                        break;
                    case "excel":
                        JSONObject excelNode = excelAssembly(json);
                        getNextNode(excelNode, linkList);
                        nodeArray.add(excelNode);
                        break;
                    case "AliSMS":
                        JSONObject alosmsNode = alismsAssembly(json);
                        getNextNode(alosmsNode, linkList);
                        nodeArray.add(alosmsNode);
                        break;
                    case "Email":
                        JSONObject emailNode = emailAssembly(json);
                        getNextNode(emailNode, linkList);
                        nodeArray.add(emailNode);
                        break;
                    case "WebService":
                        JSONObject webserviceNode = webserviceAssembly(json);
                        getNextNode(webserviceNode, linkList);
                        nodeArray.add(webserviceNode);
                        break;
                    default:
                        break;
                }
            }
        }
        return nodeArray;
    }


    /**
     * request组件拼装
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject requestAssembly(JSONObject vueFile) {
        String name = vueFile.getString("text");
        String id = vueFile.getString("key");
        JSONObject json = new JSONObject();
        json.put("type", "start");
        json.put("id", id);
        json.put("name", name);
        return json;
    }

    /**
     * respond组件拼装
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject respondAssembly(JSONObject vueFile) {
        String name = vueFile.getString("text");
        String id = vueFile.getString("key");
        JSONObject json = new JSONObject();
        json.put("type", "end");
        json.put("id", id);
        json.put("name", name);
        return json;
    }

    /**
     * http组件拼装
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject httpAssembly(JSONObject vueFile) {
        String name = vueFile.getString("text");
        String id = vueFile.getString("key");
        String method = vueFile.getString("method");
        String bodyType = vueFile.getString("bodyType");
        JSONObject properties = new JSONObject();
        properties.put("method", getMethod(method));
        properties.put("contentType", getContentType(bodyType));
        //解析url
        JSONArray params = vueFile.getJSONArray("param");
        String url = vueFile.getString("url");
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object param : params) {
                JSONObject paramJson = JSONObject.parseObject(param.toString());
                String key = paramJson.getString("name");
                String value = paramJson.getString("value");
                if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                    sb.append("?").append(key).append("=").append(value);
                }
            }
            url = url + sb.toString();
        }
        properties.put("url", url);
        //解析header
        JSONArray headers = vueFile.getJSONArray("head");
        if (headers != null && headers.size() > 0) {
            JSONObject tempHeader = new JSONObject();
            for (Object head : headers) {
                JSONObject paramJson = JSONObject.parseObject(head.toString());
                String headerName = paramJson.getString("name");
                String headerValue = paramJson.getString("value");
                if (!StringUtils.isEmpty(headerName) && !StringUtils.isEmpty(headerValue)) {
                    tempHeader.put(paramJson.getString("name"), paramJson.getString("value"));
                }
            }
            properties.put("headers", tempHeader);
        } else {
            properties.put("headers", "");
        }
        //解析body
        String body = vueFile.getString("body");
        if (StringUtils.isEmpty(body)) {
            properties.put("body", "");
        } else {
            //解析XML和JSON/FORM-DATA
            String contentType = "3";
            if (!contentType.equals(bodyType)) {
                String temp = "[";
                if (body.contains(temp)) {
                    JSONArray jsonArray = JSONArray.parseArray(body);
                    properties.put("body", jsonArray);
                } else {
                    JSONObject bodyJson = JSONObject.parseObject(body);
                    properties.put("body", bodyJson);
                }
            } else {
                properties.put("body", body);
            }
        }
        JSONObject json = new JSONObject();
        json.put("type", "http");
        json.put("id", id);
        json.put("name", name);
        json.put("properties", properties);
        return json;
    }

    /**
     * condition组件拼装
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject conditionAssembly(JSONObject vueFile) {
        String name = vueFile.getString("text");
        String id = vueFile.getString("key");
        JSONObject json = new JSONObject();
        json.put("type", "switch");
        json.put("id", id);
        json.put("name", name);
        JSONArray tempCondition = new JSONArray();
        JSONArray vueConditions = vueFile.getJSONArray("conditions");
        if (vueConditions != null && vueConditions.size() > 0) {
            for (Object vueCondition : vueConditions) {
                JSONObject condition = JSONObject.parseObject(vueCondition.toString());
                JSONObject tempJson = new JSONObject();
                tempJson.put("expression", condition.getString("condition"));
                tempJson.put("next", condition.getString("node"));
                tempCondition.add(tempJson);
            }
        }
        JSONObject properties = new JSONObject();
        properties.put("condition", tempCondition);
        json.put("properties", properties);
        return json;
    }

    /**
     * 拼装script组件信息
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject functionAssembly(JSONObject vueFile) {
        String name = vueFile.getString("text");
        String id = vueFile.getString("key");
        JSONObject json = new JSONObject();
        json.put("type", "script");
        json.put("id", id);
        json.put("name", name);
        JSONObject properties = new JSONObject();
        properties.put("code", vueFile.getString("connectObject"));
        json.put("properties", properties);
        return json;
    }

    /**
     * 拼装sql组件信息
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject sqlAssembly(JSONObject vueFile) {
        String name = vueFile.getString("text");
        String id = vueFile.getString("key");
        JSONObject dataSource = vueFile.getJSONObject("dataSource");
        String sqlStr = vueFile.getString("sql");
        String type = dataSource.getString("type").toLowerCase(Locale.ROOT);
        JSONObject source = new JSONObject();
        source.put("host", dataSource.getString("host"));
        source.put("port", dataSource.getString("port"));
        source.put("user", dataSource.getString("userName"));
        source.put("password", dataSource.getString("password"));
        source.put("database", dataSource.getString("dataName"));
        JSONObject properties = new JSONObject();
        properties.put("source", source);
        properties.put("sql", sqlStr);
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("id", id);
        json.put("name", name);
        json.put("properties", properties);
        return json;
    }

    /**
     * 拼装excel组件信息
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject excelAssembly(JSONObject vueFile) {
        String name = vueFile.getString("text");
        String excelName = vueFile.getString("name");
        String id = vueFile.getString("key");
        String path = vueFile.getString("path");
        JSONObject properties = new JSONObject();
        properties.put("name", excelName);
        properties.put("path", path);
        JSONObject json = new JSONObject();
        json.put("type", "excel");
        json.put("id", id);
        json.put("name", name);
        json.put("properties", properties);
        return json;
    }

    /**
     * 拼装阿里短信组件信息
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject alismsAssembly(JSONObject vueFile) {
        JSONObject properties = new JSONObject();
        properties.put("accessKeyId", vueFile.getString("akId"));
        properties.put("accessKeySecret", vueFile.getString("akSecret"));
        properties.put("signName", vueFile.getString("signName"));
        properties.put("templateId", vueFile.getString("templateId"));
        properties.put("mobiles", vueFile.getString("mobiles"));
        properties.put("content", vueFile.getString("content"));
        JSONObject json = new JSONObject();
        json.put("type", "smsAliyun");
        json.put("id", vueFile.getString("key"));
        json.put("name", vueFile.getString("text"));
        json.put("properties", properties);
        return json;
    }

    /**
     * 拼装Email组件信息
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject emailAssembly(JSONObject vueFile) {
        JSONObject properties = new JSONObject();
        properties.put("host", vueFile.getString("smtpUrl"));
        properties.put("from", vueFile.getString("sender"));
        properties.put("to", vueFile.getString("recipients"));
        properties.put("account", vueFile.getString("email"));
        properties.put("password", vueFile.getString("authCode"));
        properties.put("port", vueFile.getString("smtpPort"));
        properties.put("subject", vueFile.getString("subject"));
        properties.put("content", vueFile.getString("content"));
        JSONObject json = new JSONObject();
        json.put("type", "email");
        json.put("id", vueFile.getString("key"));
        json.put("name", vueFile.getString("text"));
        json.put("properties", properties);
        return json;
    }

    /**
     * 拼装WebService组件信息
     *
     * @param vueFile VUE文件内容
     * @return 拼装完成的容器文件内容
     */
    private static JSONObject webserviceAssembly(JSONObject vueFile) {
        String[] param = vueFile.getString("functionParam").split(",");
        JSONArray params = new JSONArray();
        Arrays.asList(param).stream().forEach(x -> params.add(x));
        JSONObject properties = new JSONObject();
        properties.put("url", vueFile.getString("url"));
        properties.put("method", vueFile.getString("functionName"));
        properties.put("params", params);
        JSONObject json = new JSONObject();
        json.put("type", "smsAliyun");
        json.put("id", vueFile.getString("key"));
        json.put("name", vueFile.getString("text"));
        json.put("properties", properties);
        return json;
    }

    /**
     * 查询连接的下一个节点
     *
     * @param file     节点文件内容
     * @param linkList 链接文件内容
     */
    private static void getNextNode(JSONObject file, JSONArray linkList) {
        String id = file.getString("id");
        for (Object o : linkList) {
            JSONObject link = JSONObject.parseObject(o.toString());
            String from = link.getString("from");
            if (id.equals(from)) {
                file.put("next", link.getString("to"));
                break;
            }
        }
    }

    private static String getMethod(String type) {
        String method = null;
        switch (type) {
            case "1":
                method = "GET";
                break;
            case "2":
                method = "POST";
                break;
            case "3":
                method = "PUT";
                break;
            case "4":
                method = "DELETE";
                break;
            default:
                break;
        }
        return method;
    }

    private static String getContentType(String type) {
        String contentType = null;
        switch (type) {
            case "1":
                contentType = "application/json";
                break;
            case "2":
                contentType = "multipart/form-data";
                break;
            case "3":
                contentType = "application/xml";
                break;
            default:
                break;
        }
        return contentType;
    }
}
