/**
 * 公司名称  : 唐山启奥（Shinow）
 * 项目名称   : SHINOWIT2
 * JDK 版本号        : jdk1.6.2
 * 版本号                 : 1.0
 * 创建时间：2015年8月18日 上午9:13:16
 * 创建人：郭建
 **/
package d1.project.resourcesharingmgmt.common.utils;

/**
 * 类说明：字符串通用类
 * 
 * @author kevin
 * @version Version 1.0
 * @since JDK 1.6
 */
public class StringUtils {
	/**
	 * 
	 * <p>
	 * 方法说明:判断字符串是否为空
	 * </p>
	 * 
	 * @param str
	 * @return
	 * 
	 */
	public static boolean isNull(String str) {
		if (str == null || "".equals(str) || "null".equals(str)) {
			return true;
		}
		return false;
	}


	/**
	 * <p>
	 * 方法说明:将过长的字符串加上换行
	 * </p>
	 * 
	 * @param content
	 * @return
	 **/
	public static String joinStrByBr(String content) {
		String res = content.replaceAll("<[^>]*>", "");
		StringBuffer sb = new StringBuffer();
		int len = res.length();
		int x = 20;
		if (content.length() < x) {
			return content;
		}
		if (len >= x) {
			int size = len / x;
			int ls = size;
			if (len % x != 0) {
				ls += 1;
			}
			for (int i = 1; i <= ls; i++) {
				String l = null;
				if (len % x != 0 && i == ls) {
					l = res.substring(size * x + 1);
					sb.append(l);
				} else {
					if (i < ls) {
						l = res.substring((i - 1) * x, i * x);
						sb.append(l + "<br>");
					}
				}
			}
		}
		return sb.toString();
	}
	// public static void main(String[] sssss) throws Exception {
	// System.out.println(joinStrByBr(""));
	// }
	/**
	 * 半角转全角
	 * @param input 输入字符串参数
	 * @return 全角字符串.
	 */
	public static String convert2DoubleByte(String input) {
		char c[] = input.toCharArray();
		for(int i = 0; i < c.length; i++) {
			if (c[i] == ' ')  c[i] = '\u3000';
			else if (c[i] < '\177')
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}
	/**
	 * 全角转半角
	 * @param input 输入字符串参数
	 * @return 半角字符串
	 */
	public static String convertSingleByte(String input) {
		if (StringUtils.isNull(input))
			return "";
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000')
				c[i] = ' ';
			else if (c[i] > '\uFF00' && c[i] < '\uFF5F')
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
