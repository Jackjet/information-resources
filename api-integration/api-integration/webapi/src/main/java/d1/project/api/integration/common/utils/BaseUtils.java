package d1.project.api.integration.common.utils;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;
import d1.framework.webapi.utils.TokenManager;
import d1.project.api.integration.common.Constants;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 16:00
 */
public class BaseUtils {
    private static final Logger logger = LoggerFactory.getLogger("api");

    private static final String[] HAN_ARR = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] UNIT_ARR = {"十", "百", "千", "万", "十", "白", "千", "亿", "十", "百", "千"};
    private static Map<String, ScheduledFuture<?>> timedMap = new HashMap<>(1);

    public static String generate32Id() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 获取真实ip地址，避免获取代理ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        String ipAddresses = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String toHanStr(Integer number) {
        String numStr = number + "";
        StringBuilder result = new StringBuilder();
        int numLen = numStr.length();

        for (int i = 0; i < numLen; i++) {
            int num = numStr.charAt(i) - 48;
            if (i != numLen - 1 && num != 0) {
                result.append(HAN_ARR[num]).append(UNIT_ARR[numLen - 2 - i]);
                if (number >= 10 && number < 20) {
                    result = new StringBuilder(result.substring(1));
                }
            } else {
                if (!(number >= 10 && number % 10 == 0)) {
                    result.append(HAN_ARR[num]);
                }
            }
        }

        return result.toString();

    }

    public static Map<String, ScheduledFuture<?>> getTimedMap() {
        return timedMap;
    }

    public static void setTimedMap(Map<String, ScheduledFuture<?>> timedMap) {
        BaseUtils.timedMap = timedMap;
    }


    /**
     * 获取操作用户ID
     *
     * @param request 请求信息
     * @return 用户ID
     * @throws Exception 抛出异常
     */
    public static String getUserId(HttpServletRequest request) throws Exception {
        JSONObject json = BaseUtils.getUserInfo(request);
        String userId = json.getString("id");
        if (StringUtils.isEmpty(userId)) {
            throw new Exception(Constants.USER_IS_NULL);
        }
        return userId;
    }

    public static String calendarToString(Calendar time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(time.getTime());
    }

    public static String getGmtTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Calendar calendar = Calendar.getInstance();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(calendar.getTime());
    }

    public static String getSignature(byte[] content, byte[] secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret, "HmacSHA256");
        hmacSHA256.init(secretKey);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(hmacSHA256.doFinal(content));
    }


    /**
     * 获取当天的0点
     *
     * @return 当天0点时间
     */
    public static Calendar todayFirstTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 获取当天23点59分
     *
     * @return 23点59分59秒
     */
    public static Calendar todayLastTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    /**
     * 获取七天前的0点0分
     *
     * @return 0点0分0秒
     */
    public static Calendar sevenLastTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, -7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static CloseableHttpClient acceptsUntrustedCertsHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        //
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        b.setSSLContext(sslContext);

        // don't check Hostnames, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

        // here's the special part:
        //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        //      -- and create a Registry, to register it.
        //
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        // now, we create connection-manager using our Registry.
        //      -- allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(200);
        connMgr.setDefaultMaxPerRoute(100);
        b.setConnectionManager(connMgr);

        // finally, build the HttpClient;
        //      -- done!
        CloseableHttpClient client = b.build();

        return client;
    }

    public static void exportText(String apiName, String ip, String method, String caseContent, HttpServletResponse response) throws Exception {
        JSONObject data = new JSONObject();
        data.put("apiName", apiName);
        data.put("ip", ip);
        data.put("method", method);
        data.put("content", JSONObject.parseObject(caseContent));

        String content = data.toJSONString();

        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        response.setContentType("application/json");
        response.addHeader("Content-Disposition", "attachment;filename=testCase.json");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(content.getBytes(StandardCharsets.UTF_8));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                }
                if (outStr != null) {
                    outStr.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }


    public static JSONObject getUserInfo(HttpServletRequest request){
        String URL = "";
        try {
            final Properties properties = new Properties();
            final String filePath = System.getProperty("user.dir") + "/config/application.properties";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(in);
            URL = properties.getProperty("d1.project.user.manage");
        }catch (Exception e){

        }
        String accessToken = getTokenByHttpServletRequest(request);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);
        ResponseEntity<JSONObject> res = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<String>(headers), JSONObject.class);
        return Objects.requireNonNull(res.getBody()).getJSONObject("data");
    }

    public static void setCreateTimeAndCreateById(HttpServletRequest request,BaseCreateEntity baseCreateEntity){
        JSONObject userInfo = getUserInfo(request);
        baseCreateEntity.setCreateById(userInfo.getString("id"));
        baseCreateEntity.setCreateTime(Calendar.getInstance());
    }

    public static void setUpdateTimeAndCreateById(HttpServletRequest request,BaseCreateAndUpdateEntity baseCreateAndUpdateEntity){
        JSONObject userInfo = getUserInfo(request);
        baseCreateAndUpdateEntity.setUpdateById(userInfo.getString("id"));
        baseCreateAndUpdateEntity.setUpdateTime(Calendar.getInstance());
    }


    private static String getTokenByHttpServletRequest(HttpServletRequest request) {
        String authPrefix = "token";
        String auth = request.getHeader("Authorization");
        if (StringUtils.isEmpty(auth)) {
            return null;
        } else {
            String[] authArray = auth.split(" ");
            return authPrefix.equals(authArray[0]) && authArray.length > 1 ? authArray[1] : null;
        }
    }
}
