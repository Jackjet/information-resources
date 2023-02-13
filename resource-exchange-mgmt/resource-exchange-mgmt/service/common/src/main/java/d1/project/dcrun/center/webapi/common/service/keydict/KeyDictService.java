package d1.project.dcrun.center.webapi.common.service.keydict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Buter
 * @date 2020/5/22 11:29
 */
@Service
public class KeyDictService {
    private final KeyDictDao keyDictDao;

    @Autowired
    public KeyDictService(KeyDictDao keyDictDao) {
        this.keyDictDao = keyDictDao;
    }

    /**
     * 生成6位id，包括a-z 0-9 总共36个作为备选
     */
    public String generateAppId() throws Exception {
        synchronized (this) {
            KeyDictEntity entity = keyDictDao.findByKey("appid").orElse(new KeyDictEntity("appid", "00000000"));
            String value = entity.getValue();
            if (StringUtils.isEmpty(value)) {
                throw new Exception("字典表里的appid对应的值为空，请仔细核对原因");
            }
            String newValue = new String(systemAdd(value.toCharArray()));
            entity.setValue(newValue);
            keyDictDao.save(entity);
            return newValue;
        }
    }


    /**
     * 36进制加一进位
     *
     * @param ss 传入的8位char数组
     * @return 加1后的8位char数组
     */
    private char[] systemAdd(char[] ss) {
        int length = 8;
        for (int i = length - 1; i >= 0; i--) {
            int temp = ss[i];
            int number9 = '9';
            int numberz = 'z';
            if (temp < number9 - 1) {
                ss[i] = (char) ((int) ss[i] + 1);
                break;
            } else if (temp == number9) {
                ss[i] = 'a';
                break;
            } else if (temp < numberz) {
                ss[i] = (char) ((int) ss[i] + 1);
                break;
            } else {
                ss[i] = '0';
            }
        }
        return ss;
    }
}
