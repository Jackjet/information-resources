package d1.component.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.base.utils.Utils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);

    private final String METHOD_POST = "post";
    private final String METHOD_PUT = "put";
    private final String METHOD_DELETE = "delete";
    private final String METHOD_GET = "get";
    private OkHttpClient.Builder mBuilder;
    private OkHttpClient okHttpClient;

    ////// property
    private String url;
    private String method;
    private String contentType;
    private String headers;
    private String body;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);

        url = properties.get("url").toString();
        method = properties.get("method").toString();
        contentType = properties.get("contentType").toString();
        headers = properties.get("headers").toString();
        body = properties.get("body").toString();

        mBuilder = new OkHttpClient.Builder();
        mBuilder.sslSocketFactory(createSslSocketFactory(), new TrustAllManager());
        mBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        okHttpClient = mBuilder.build();

    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);

        if (Utils.isEmpty(url)) {
            throw new Exception("url请求路径不能为空");
        }

        if (Utils.isEmpty(method)) {
            method = "GET";
        }

        //替换变量
        String newHeaders = processExpressions(context, input.getInput(), headers);
        String newBody = processExpressions(context, input.getInput(), body);

        String result = request(method, url, JSONObject.parseObject(newHeaders), JSONObject.parseObject(newBody), contentType);

        Input nextInput = new Input();
        nextInput.setInput(JSON.parse(result));
        return getNextNode(context).run(context, nextInput);
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }


    //////////////

    private String request(String method, String url, Map<String, Object> headers, Map<String, Object> parameters, String contentType) throws Exception {
        Response response = requestResponse(method, url, headers, parameters, contentType);
        if (response != null && response.body() != null) {
            return response.body().string();
        }
        return "";
    }

    private Response requestResponse(String method, String url, Map<String, Object> headers, Map<String, Object> parameters, String contentType) throws Exception {
        if (Utils.isEmpty(contentType)) {
            contentType = "application/x-www-form-urlencoded";
        }

        RequestBody requestBody = getBody(contentType, parameters);
        Request.Builder requestBuilder = new Request.Builder().url(url);
        switch (method.toLowerCase()) {
            case METHOD_GET:
                requestBuilder = requestBuilder.get();
                break;
            case METHOD_POST:
                requestBuilder = requestBuilder.post(requestBody);
                break;
            case METHOD_PUT:
                requestBuilder = requestBuilder.put(requestBody);
                break;
            case METHOD_DELETE:
                requestBuilder = requestBuilder.delete(requestBody);
                break;
            default:
                throw new Exception("不支持的method类型=" + method);
        }

        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        return okHttpClient.newCall(requestBuilder.build()).execute();
    }


    private RequestBody getBody(String contentType, Map<String, Object> parameters) throws Exception {
        switch (contentType.toLowerCase()) {
            case "application/json":
                StringBuilder sb = new StringBuilder();
                if (parameters != null) {
                    sb.append(JSON.toJSONString(parameters));
                }
                return RequestBody.create(MediaType.parse("application/json"), sb.toString());
            case "application/x-www-form-urlencoded":
                FormBody.Builder builder = new FormBody.Builder();
                if (parameters != null) {
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        builder.add(entry.getKey(), entry.getValue().toString());
                    }
                }
                return builder.build();
            case "application/x-www-form-urlencoded;charset=gb2312":
            case "application/x-www-form-urlencoded;charset=gbk":
                sb = new StringBuilder();
                if (parameters != null) {
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                    if (parameters.size() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                }
                return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=gb2312"), sb.toString());
            default:
                throw new Exception("不支持的contentType=" + contentType);
        }
    }

    private SSLSocketFactory createSslSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
        }
        return sslSocketFactory;
    }


    class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


}
