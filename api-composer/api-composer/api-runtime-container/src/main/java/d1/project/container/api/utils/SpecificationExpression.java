package d1.project.container.api.utils;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 解析类似表达式 10 and 1 or 2 and (4 or 15)
 *
 * @author Buter
 * @date 2020/4/3 21:07
 */
public class SpecificationExpression {
    private String expression;

    public SpecificationExpression(String expression) {
        this.expression = expression;
    }

    /**
     * 将中缀表达式转换为后缀表达式（逆波兰表达式）
     * 1.数字直接入队列
     * 2.运算符要与栈顶元素比较
     *  ①栈为空直接入栈
     *  ②运算符优先级大于栈顶元素优先级则直接入栈
     *  ③小于或等于则出栈入列，再与栈顶元素进行比较，直到运算符优先级小于栈顶元
     *     素优先级后，操作符再入栈
     * 3.操作符是 ( 则无条件入栈
     * 4.操作符为 )，则依次出栈入列，直到匹配到第一个(为止，此操作符直接舍弃，(直接出栈舍弃
     *
     * @throws Exception 校验错误
     */
    public List<String> transfer() throws Exception {
        Stack<String> stack = new Stack<>();
        List<String> list = new ArrayList<>();
        StringBuilder temp = null;
        if (StringUtils.isEmpty(expression)) {
            throw new Exception("where条件表达式不能为空");
        }
        //先转成  0&1|2&(4|15)&(5|6）
        this.expression = this.expression.replace(" ", "").trim().replace("and", "&").replace("or", "|");
        char[] chars = this.expression.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c)) {
                if (temp == null) {
                    temp = new StringBuilder(String.valueOf(c));
                } else {
                    temp.append(String.valueOf(c));
                }
            } else {
                if (temp != null) {
                    list.add(temp.toString());
                    temp = null;
                }

                if (c == '&' || c == '|') {
                    if (stack.empty()) {
                        stack.push(String.valueOf(c));
                        continue;
                    }
                    //上一个元素不为（，且当前运算符优先级小于上一个元素则，将比这个运算符优先级大的元素全部加入到队列中
                    while (!stack.isEmpty() && !"(".equals(stack.lastElement())) {
                        list.add(stack.pop());
                    }
                    stack.push(String.valueOf(c));

                } else if (c == '(') {
                    //遇到左小括号无条件加入
                    stack.push("(");
                } else if (c == ')') {
                    //遇到右小括号，则寻找上一小括号，然后把中间的值全部放入队列中
                    while (!("(").equals(stack.lastElement())) {
                        list.add(stack.pop());
                    }
                    //上述循环停止，这栈顶元素必为"("
                    stack.pop();
                } else {
                    throw new Exception("where条件表达式只能包含字符&&||()以及数字:" + expression + "包含了" + c);
                }
            }
        }
        if (temp != null) {
            list.add(temp.toString());
            temp = null;
        }
        //将栈中剩余元素加入到队列中
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }
        return list;
    }
}
