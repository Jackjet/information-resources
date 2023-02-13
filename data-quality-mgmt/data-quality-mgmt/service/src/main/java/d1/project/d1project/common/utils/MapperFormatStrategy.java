package d1.project.d1project.common.utils;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-21 17:05
 */
public class MapperFormatStrategy {

    public String intToSex(int value) {

        if (value == 0) {
            return "女";
        }

        return "男";
    }

    public int stringToSex(String value) {

        if ("男".equals(value)) {
            return 1;
        }

        return 0;
    }
}
