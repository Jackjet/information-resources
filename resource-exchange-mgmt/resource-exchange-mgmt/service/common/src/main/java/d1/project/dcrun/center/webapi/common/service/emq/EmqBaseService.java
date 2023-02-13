package d1.project.dcrun.center.webapi.common.service.emq;

import net.dcrun.component.emq.IEmqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chengh
 **/
@Service
public class EmqBaseService {
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;
    @Value("${spring.datasource.username}")
    private String dataSourceUsername;
    @Value("${spring.datasource.password}")
    private String dataSourcePwd;

    @Autowired
    private IEmqService emqService;

    public void getConnection() throws Exception {
        Map<String, String> map = new HashMap<>(3);
        map.put("dbUrl", dataSourceUrl);
        map.put("dbUserName", dataSourceUsername);
        map.put("dbPassword", dataSourcePwd);
        emqService.getConnection("postgre", map);
    }

    public void addUserAndPermission(List<Map<String, String>> userList, List<Map<String, String>> permissionList) throws Exception {
        try {
            getConnection();
            emqService.addUser(userList);
            emqService.addPermission(permissionList);
        } finally {
            emqService.close();
        }
    }

    public void deletePermissionAndUser(List<String> userList) throws Exception {
        try {
            getConnection();
            emqService.deletePermissionForUserName(userList);
            emqService.deleteUser(userList);
        } finally {
            emqService.close();
        }
    }
}
