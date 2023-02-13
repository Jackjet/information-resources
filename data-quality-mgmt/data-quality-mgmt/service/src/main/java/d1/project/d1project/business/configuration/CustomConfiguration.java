package d1.project.d1project.business.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Buter
 * @date 2020/3/10 10:37
 */
@Configuration
public class CustomConfiguration {

//    @Bean
//    IHttpService httpService() {
//        return new HttpService();
//    }
//
//    @Bean
//    IFileServerService fileServerService() {
//        return new FileServerService();
//    }
//
//    @Bean
//    IHmacSignService hmacSignService() {
//        return new HmacSignService();
//    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
