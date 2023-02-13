package d1.project.api.integration.kong.utils;

import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class KongUtils {
    private static KongUtils instance;

    private KongUtils() {

    }

    public static KongUtils getInstance() {
        if (instance == null) {
            instance = new KongUtils();
        }
        return instance;
    }

    public String listToString(List<String> list) {
        String result = "";
        for (String str : list) {
            result += str + ",";
        }

        if (!StringUtils.isEmpty(result)) {
            result.subSequence(0, result.length() - 2);
        }
        return result;
    }

    public List<String> stringToList(String str) {
        String[] strs = str.split(",");

        List<String> result = new Vector<>();
        for (int i = 0; i < strs.length; i++) {
            result.add(strs[i]);
        }
        return result;
    }

    public String getGMTTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Calendar calendar = Calendar.getInstance();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        return sdf.format(calendar.getTime());
    }

    public String getSignature(byte[] content, byte[] secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret, "HmacSHA256");
        sha256_HMAC.init(secret_key);

        String hash = org.apache.commons.codec.binary.Base64.encodeBase64String(sha256_HMAC.doFinal(content));// 重点
        System.out.println(hash);

        return hash;
    }
}
