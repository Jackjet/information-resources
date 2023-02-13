//package com.digitalchina.resourcecatalog.db.service.impl;
//
////import com.alibaba.fastjson.JSONArray;
//import com.digitalchina.resourcecatalog.db.domain.SysUser;
////import d1.framework.authz.service.AuthzUserService;
////import org.keycloak.representations.idm.UserRepresentation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class KeycloakSynchUserServiceImpl {
////    @Autowired
////    private AuthzUserService authzUserService;
//
//    /**
//     * 同步用户
//     *
//     * @param type add-新增  update-修改  delete-删除 updatePwd-修改密码
//     * @param user
//     * @return
//     */
//    public boolean synchUser(String type, SysUser user) {
//        try {
//
//            UserRepresentation users = new UserRepresentation();
//            // 新建用户
//            if ("add".equalsIgnoreCase(type)) {
//
//                authzUserService.post(user.getUsername(), user.getName(), "123456");
//                return true;
//
//            } else if ("update".equalsIgnoreCase(type)) {
//                String userlist = authzUserService.findAll("", user.getUsername(), "", "", 0, 1000);
//                List<UserRepresentation> list = JSONArray.parseArray(userlist, UserRepresentation.class);
//
//
//                if (list != null && list.size() > 0) {
//                    authzUserService.put(users.getId(), null, user.getName(), null);
//                    return true;
//                } else {
//                    authzUserService.post(user.getUsername(), user.getName(), "123456");
//                    return true;
//                }
//            } else if ("delete".equalsIgnoreCase(type)) {
//                authzUserService.delete(users.getId());
//                return true;
//            } else if ("updatePwd".equalsIgnoreCase(type)) {
//                String userlist = authzUserService.findAll("", user.getUsername(), "", "", 0, 1000);
//                List<UserRepresentation> list = JSONArray.parseArray(userlist, UserRepresentation.class);
//                if (list != null && list.size() > 0) {
//                    authzUserService.put(users.getId(), null, null, user.getPassword());
//                    return true;
//                }
//            } else if ("disable".equalsIgnoreCase(type)) {
//                String userlist = authzUserService.findAll("", user.getUsername(), "", "", 0, 1000);
//                List<UserRepresentation> list = JSONArray.parseArray(userlist, UserRepresentation.class);
//                if (list != null && list.size() > 0) {
//                    authzUserService.setEnabled(users.getId(), user.getDisabled() != 1);
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////	    //用户Id只能在response head location 截取
////	    String userIdString = result.getHeaders().getFirst("location").toString();
////	    String[] userIdArray = userIdString.split("/");
////	    String userId = userIdArray[userIdArray.length - 1];
//        return false;
//    }
//
//}
