package d1.project.container.task.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


/**
 * 设置跨域，缺省是*,生产环境需要单独设置，如果是多域名，中间用,隔开
 *
 * @author Buter
 */
@Component
public class CorsFilter implements Filter {
    @Value("${d1.framework.webapi.cors:*}")
    private String cors;


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        initCorsHeader(cors, (HttpServletRequest) req, response);
        //System.out.println("*********************************过滤器被使用**************************");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
//--------------------------------Pour another drink---------------------------------------------------------//

    private void initCorsHeader(String cors, HttpServletRequest req, HttpServletResponse response) {
        String cosAll = "*";
        if (StringUtils.isEmpty(cors) || cosAll.equals(cors)) {
            response.setHeader("Access-Control-Allow-Origin", cosAll);
        } else {
            String originHeader = req.getHeader("Origin");
            if (originHeader != null) {
                String[] corsArray = cors.split(",");
                if (!Arrays.asList(corsArray).contains(originHeader)) {
                    return;
                }
            }
            response.setHeader("Access-Control-Allow-Origin", safeHttpHeader(originHeader));
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        //ie11比较特殊，这里的值不能设置为*，必须写全
        response.setHeader("Access-Control-Allow-Headers", "custom,authorization,x-requested-with,content-type");
    }

    private String safeHttpHeader(String value) {
        //参考https://en.wikipedia.org/wiki/HTTP_response_splitting
        //https://stackoverflow.com/questions/39630946/his-use-of-httpservletresponse-setheader-might-be-used-to-include-crlf-char/39837458#39837458
        String result = "";
        if (value != null) {
            char[] chars = value.toCharArray();
            StringBuilder buffer = new StringBuilder(chars.length);
            for (char aChar : chars) {
                switch (aChar) {
                    case '\r':
                        buffer.append('%');
                        buffer.append('0');
                        buffer.append('D');
                        break;
                    case '\n':
                        buffer.append('%');
                        buffer.append('0');
                        buffer.append('A');
                        break;
                    default:
                        buffer.append(aChar);
                        break;
                }
            }
            result = buffer.toString();
        }
        return result;
    }
}