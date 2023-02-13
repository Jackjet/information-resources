package com.digitalchina.resourcecatalog.db.service.impl;

import com.digitalchina.resourcecatalog.db.domain.SysUser;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakSynchUserOneServiceImpl {
    @Value("${userKeycloak.username}")
    private String username;

    @Value("${userKeycloak.password}")
    private String password;

    @Value("${userKeycloak.defaultPwd}")
    private String defaultPwd;

    @Value("${keycloak.auth-server-url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${userKeycloak.enable}")
    private Boolean keycloakEnable;
    /**
     * 同步用户
     * @param type add-新增  update-修改  delete-删除 updatePwd-修改密码
     * @param user
     * @return
     */
    public boolean synchUser(String type, SysUser user) {
        if(!keycloakEnable) {
            return false;
        }
        try {
            Keycloak keycloak = Keycloak.getInstance(
                    // keycloak地址
                    url,
                    // 指定 Realm master
                    "master",
                    // 管理员账号
                    username,
                    // 管理员密码
                    password,
                    // 指定client（admin-cli是Master Realm中的内置client,Direct Access
                    // Grants Enabled）
                    "admin-cli");
            //	    String accessTokenStr = keycloak.tokenManager().getAccessTokenString();
            // 进入 share
            RealmResource realmResource = keycloak.realm(realm);
            Response result = null;
            UserRepresentation users = new UserRepresentation();
            // 新建用户
            if("add".equalsIgnoreCase(type)){
                // 设置登录账号
                users.setUsername(user.getUsername());
                // 设置账号“启用”
                users.setEnabled(user.getDisabled()!=1);
                // 设置密码
                List<CredentialRepresentation> credentials = new ArrayList<CredentialRepresentation>();
                CredentialRepresentation cr = new CredentialRepresentation();
                cr.setType(CredentialRepresentation.PASSWORD);
                cr.setValue(defaultPwd);
                cr.setTemporary(false);
                credentials.add(cr);
                users.setCredentials(credentials);
                users.setEmail(user.getEmail());
                users.setFirstName(user.getName());
                // 创建用户
                result = realmResource.users().create(users);
                if(result.getStatus()==201){
                    return true;
                }
            }else if("update".equalsIgnoreCase(type)){
                List<UserRepresentation> list=realmResource.users().search(user.getUsername());
                if(list!=null&&list.size()>0){
                    users=list.get(0);
                    //根据id获取用户信息
                    UserResource sarchUser=realmResource.users().get(users.getId());
                    users.setEmail(user.getEmail());
                    users.setFirstName(user.getName());
                    sarchUser.update(users);
                    return true;
                }else{
                    users = new UserRepresentation();
                    users.setUsername(user.getUsername());
                    // 设置账号“启用”
                    users.setEnabled(user.getDisabled()!=1);
                    // 设置密码
                    List<CredentialRepresentation> credentials = new ArrayList<CredentialRepresentation>();
                    CredentialRepresentation cr = new CredentialRepresentation();
                    cr.setType(CredentialRepresentation.PASSWORD);
                    cr.setValue(defaultPwd);
                    cr.setTemporary(false);
                    credentials.add(cr);
                    users.setCredentials(credentials);
                    users.setEmail(user.getEmail());
                    users.setFirstName(user.getName());
                    // 创建用户
                    result = realmResource.users().create(users);
                    if(result.getStatus()==201){
                        return true;
                    }
                }
            }else if("delete".equalsIgnoreCase(type)){
                List<UserRepresentation> list=realmResource.users().search(user.getUsername());
                if(list!=null&&list.size()>0){
                    users=list.get(0);
                    //删除user信息
                    result=realmResource.users().delete(users.getId());
                    if(result.getStatus()==204){
                        return true;
                    }
                }
            }else if("updatePwd".equalsIgnoreCase(type)){
                List<UserRepresentation> list=realmResource.users().search(user.getUsername());
                if(list!=null&&list.size()>0){
                    users=list.get(0);
                    //根据id获取用户信息
                    UserResource sarchUser=realmResource.users().get(users.getId());
                    // 设置密码
                    List<CredentialRepresentation> credentials = new ArrayList<CredentialRepresentation>();
                    CredentialRepresentation cr = new CredentialRepresentation();
                    cr.setType(CredentialRepresentation.PASSWORD);
                    cr.setValue(user.getPassword());
                    cr.setTemporary(false);
                    credentials.add(cr);
                    users.setCredentials(credentials);
                    sarchUser.update(users);
                    return true;
                }
            }else if("disable".equalsIgnoreCase(type)){
                List<UserRepresentation> list=realmResource.users().search(user.getUsername());
                if(list!=null&&list.size()>0){
                    users=list.get(0);
                    //根据id获取用户信息
                    UserResource sarchUser=realmResource.users().get(users.getId());
                    // 设置账号“启用”
                    users.setEnabled(user.getDisabled()!=1);
                    sarchUser.update(users);
                    return true;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
//	    //用户Id只能在response head location 截取
//	    String userIdString = result.getHeaders().getFirst("location").toString();
//	    String[] userIdArray = userIdString.split("/");
//	    String userId = userIdArray[userIdArray.length - 1];
        return false;
    }
}
