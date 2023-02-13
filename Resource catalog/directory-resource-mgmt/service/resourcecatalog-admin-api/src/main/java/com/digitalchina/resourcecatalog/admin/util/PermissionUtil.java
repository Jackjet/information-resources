package com.digitalchina.resourcecatalog.admin.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.vo.PermVo;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.*;

public class PermissionUtil {

    public static List<PermVo> listPermVo(List<Permission> permissions) {
        List<PermVo> root = new ArrayList<>();
        for (Permission permission : permissions) {
            RequiresPermissions requiresPermissions = permission.getRequiresPermissions();
            RequiresPermissionsDesc requiresPermissionsDesc = permission.getRequiresPermissionsDesc();
            String api = permission.getApi();

            String[] menus = requiresPermissionsDesc.menu();
            if (menus.length > 3) {
                throw new RuntimeException("目前只支持三级菜单");
            }
            String menu1 = menus[0];
            PermVo perm1 = null;
            for (PermVo permVo : root) {
                if (permVo.getLabel().equals(menu1)) {
                    perm1 = permVo;
                    break;
                }
            }
            if (perm1 == null) {
                perm1 = new PermVo();
                perm1.setId(menu1);
                perm1.setLabel(menu1);
                perm1.setChildren(new ArrayList<>());
                root.add(perm1);
            }
            PermVo perm2 = null;
            PermVo perm3 = null;
            if(menus.length >1){
            	String menu2 = menus[1];
            	for (PermVo permVo : perm1.getChildren()) {
            		if (permVo.getLabel().equals(menu2)) {
            			perm2 = permVo;
            			break;
            		}
            	}
            	if (perm2 == null) {
            		perm2 = new PermVo();
            		perm2.setId(menu2);
            		perm2.setLabel(menu2);
            		perm2.setChildren(new ArrayList<>());
            		perm1.getChildren().add(perm2);
            	}
            	
            	if(menus.length ==3){
            		String menu3 = menus[2];
            		for (PermVo permVo : perm2.getChildren()) {
            			if (permVo.getLabel().equals(menu3)) {
            				perm3 = permVo;
            				break;
            			}
            		}
            		if (perm3 == null) {
            			perm3 = new PermVo();
            			perm3.setId(menu3);
            			perm3.setLabel(menu3);
            			perm3.setChildren(new ArrayList<>());
            			perm2.getChildren().add(perm3);
            		}
            	}
            }
            PermVo nextPerm = null;
            if(perm2==null){
            	nextPerm = perm1;
            }else if(perm3==null){
            	nextPerm = perm2;
            }else{
            	nextPerm = perm3;
            }
            
            String button = requiresPermissionsDesc.button();
            PermVo leftPerm = null;
           
            for (PermVo permVo : nextPerm.getChildren()) {
                if (permVo.getLabel().equals(button)) {
                    leftPerm = permVo;
                    break;
                }
            }
//            System.out.println(leftPerm);
            if (leftPerm == null) {
                leftPerm = new PermVo();
                leftPerm.setId(requiresPermissions.value()[0]);
                leftPerm.setLabel(requiresPermissionsDesc.button());
                leftPerm.setApi(api);
                nextPerm.getChildren().add(leftPerm);
            } else {
                // TODO
                // 目前限制Controller里面每个方法的RequiresPermissionsDesc注解是唯一的
                // 如果允许相同，可能会造成内部权限不一致。
                throw new RuntimeException("权限已经存在，不能添加新权限");
            }

        }
        return root;
    }

    public static List<Permission> listPermission(ApplicationContext context, String basicPackage) {
        Map<String, Object> map = context.getBeansWithAnnotation(Controller.class);
        List<Permission> permissions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object bean = entry.getValue();
            if (!StringUtils.contains(ClassUtils.getPackageName(bean.getClass()), basicPackage)) {
                continue;
            }

            Class<?> clz = bean.getClass();
            Class controllerClz = clz.getSuperclass();
            RequestMapping clazzRequestMapping = AnnotationUtils.findAnnotation(controllerClz, RequestMapping.class);
            List<Method> methods = MethodUtils.getMethodsListWithAnnotation(controllerClz, RequiresPermissions.class);
            for (Method method : methods) {
                RequiresPermissions requiresPermissions = AnnotationUtils.getAnnotation(method,
                        RequiresPermissions.class);
                RequiresPermissionsDesc requiresPermissionsDesc = AnnotationUtils.getAnnotation(method,
                        RequiresPermissionsDesc.class);

                if (requiresPermissions == null || requiresPermissionsDesc == null) {
                    continue;
                }

                String api = "";
                if (clazzRequestMapping != null) {
                    api = clazzRequestMapping.value()[0];
                }

                PostMapping postMapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
                if (postMapping != null) {
                    api = "POST " + api + postMapping.value()[0];

                    Permission permission = new Permission();
                    permission.setRequiresPermissions(requiresPermissions);
                    permission.setRequiresPermissionsDesc(requiresPermissionsDesc);
                    permission.setApi(api);
                    permissions.add(permission);
                    continue;
                }
                GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
                if (getMapping != null) {
                    api = "GET " + api + getMapping.value()[0];
                    Permission permission = new Permission();
                    permission.setRequiresPermissions(requiresPermissions);
                    permission.setRequiresPermissionsDesc(requiresPermissionsDesc);
                    permission.setApi(api);
                    permissions.add(permission);
                    continue;
                }
                // TODO
                // 这里只支持GetMapping注解或者PostMapping注解，应该进一步提供灵活性
                throw new RuntimeException("目前权限管理应该在method的前面使用GetMapping注解或者PostMapping注解");
            }
        }
        //排序
        permissions.sort(new Comparator<Permission>() {
        	@Override
        	public int compare(Permission o1, Permission o2) {
        		if(o1.getRequiresPermissionsDesc().order() == o2.getRequiresPermissionsDesc().order()){
        			if(o1.getRequiresPermissionsDesc().order2() == o2.getRequiresPermissionsDesc().order2()){
            			return o1.getRequiresPermissionsDesc().order3() - o2.getRequiresPermissionsDesc().order3();
            		}
        			return o1.getRequiresPermissionsDesc().order2() - o2.getRequiresPermissionsDesc().order2();
        		}
        		return o1.getRequiresPermissionsDesc().order() - o2.getRequiresPermissionsDesc().order();
        	}
		});
     /*   permissions.forEach( p -> {
        	System.out.println(Arrays.asList(p.getRequiresPermissionsDesc().menu()) +"---" 
        			+  p.getRequiresPermissionsDesc().order() + "---" 
        			+  p.getRequiresPermissionsDesc().order2() + "---" 
        			+  p.getRequiresPermissionsDesc().order3() + "---" 
        			);
        });*/
        return permissions;
    }

    public static Set<String> listPermissionString(List<Permission> permissions) {
        Set<String> permissionsString = new HashSet<>();
        for (Permission permission : permissions) {
            permissionsString.add(permission.getRequiresPermissions().value()[0]);
        }
        return permissionsString;
    }
}
