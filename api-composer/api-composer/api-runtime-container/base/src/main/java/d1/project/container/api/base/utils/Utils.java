package d1.project.container.api.base.utils;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Utils {
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }


    public static String getJsonValue(JSONObject jsonObject, String key, String defaultValue) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getString(key);
        }
        return defaultValue;
    }

    public static List<String> getPathExpressions(String source) {
        List<String> pathExpressions = new ArrayList<>();

        Stack<Index> stack = new Stack<>();
        char[] ch = source.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            switch (ch[i]) {
                case '{':
                    stack.push(new Index(i, ch[i]));
                    break;
                case '}':
                    Index index = stack.peek();
                    if (index.startChar == '{') {
                        //判断当前表达式前面是否有'$'，如果没有表示不是
                        if (index.startIndex > 0 && source.charAt(index.startIndex - 1) == '$') {
                            pathExpressions.add(source.substring(index.startIndex + 1, i));
                        }
                        stack.pop();
                    }
                    break;
            }
        }

        return pathExpressions;
    }

    public static Object getValueByJsonPath(JSONObject object, String pathExpression) {
        if (Utils.isEmpty(pathExpression)) {
            return null;
        }
        return JsonPath.read(object, pathExpression);
    }
}

class Index {
    int startIndex;
    char startChar;

    public Index(int startIndex, char startChar) {
        this.startIndex = startIndex;
        this.startChar = startChar;
    }
}
