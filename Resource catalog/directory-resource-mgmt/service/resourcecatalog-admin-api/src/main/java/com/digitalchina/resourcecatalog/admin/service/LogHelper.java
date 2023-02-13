package com.digitalchina.resourcecatalog.admin.service;

import jodd.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.digitalchina.resourcecatalog.core.util.IpUtil;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import com.digitalchina.resourcecatalog.db.domain.SysLog;
import com.digitalchina.resourcecatalog.db.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 这里的日志类型设计成四种（当然开发者需要可以自己扩展）
 * 一般日志：用户觉得需要查看的一般操作日志，建议是默认的日志级别
 * 安全日志：用户安全相关的操作日志，例如登录、删除管理员
 * 订单日志：用户交易相关的操作日志，例如订单发货、退款
 * 其他日志：如果以上三种不合适，可以选择其他日志，建议是优先级最低的日志级别
 * <p>
 * 当然可能很多操作是不需要记录到数据库的，例如编辑商品、编辑广告品之类。
 */
@Component
public class LogHelper {
    public static final Integer LOG_TYPE_GENERAL = 0;
    public static final Integer LOG_TYPE_AUTH = 1;
    public static final Integer LOG_TYPE_ORDER = 2;
    public static final Integer LOG_TYPE_OTHER = 3;

    @Autowired
    private SysLogService logService;

    public void logGeneralSucceed(String action) {
        logAdmin(LOG_TYPE_GENERAL, action, true, "", "");
    }

    public void logGeneralSucceed(String action, String result) {
        logAdmin(LOG_TYPE_GENERAL, action, true, result, "");
    }

    public void logGeneralFail(String action, String error) {
        logAdmin(LOG_TYPE_GENERAL, action, false, error, "");
    }

    public void logAuthSucceed(String action) {
        logAdmin(LOG_TYPE_AUTH, action, true, "", "");
    }

    public void logAuthSucceed(String action, String result) {
        logAdmin(LOG_TYPE_AUTH, action, true, result, "");
    }

    public void logAuthFail(String action, String error) {
        logAdmin(LOG_TYPE_AUTH, action, false, error, "");
    }

    public void logOrderSucceed(String action) {
        logAdmin(LOG_TYPE_ORDER, action, true, "", "");
    }

    public void logOrderSucceed(String action, String result) {
        logAdmin(LOG_TYPE_ORDER, action, true, result, "");
    }

    public void logOrderFail(String action, String error) {
        logAdmin(LOG_TYPE_ORDER, action, false, error, "");
    }

    public void logOtherSucceed(String action) {
        logAdmin(LOG_TYPE_OTHER, action, true, "", "");
    }

    public void logOrgSucceed(String action, String orgName, String orgCode) {
        logAdmin(LOG_TYPE_GENERAL, action, true, action + "【" + orgName + "/" + orgCode + "】", "");
    }

    public void logUpLoadFileSucceed(String action, String fileName) {
        logAdmin(LOG_TYPE_GENERAL, action, true, action + "【" + fileName + "】", "");
    }

    public void logBusiRelSucceed(String action, String busiNm, String busiNo, String infoName) {
        if (StringUtil.isEmpty(busiNo)) {
            logAdmin(LOG_TYPE_GENERAL, action, true, action + "【" + busiNm + "】" + "【" + infoName + "】", "");
        } else {
            logAdmin(LOG_TYPE_GENERAL, action, true, action + "【" + busiNm + "/" + busiNo + "】" + "【" + infoName + "】", "");
        }
    }

    public void logOtherSucceed(String action, String result) {
        logAdmin(LOG_TYPE_OTHER, action, true, result, "");
    }


    public void logOtherFail(String action, String error) {
        logAdmin(LOG_TYPE_OTHER, action, false, error, "");
    }

    public void logAdmin(Integer type, String action, Boolean succeed, String result, String comment) {
        SysLog log = new SysLog();

        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            SysUser admin = (SysUser) currentUser.getPrincipal();
            if (admin != null) {
                log.setAdmin(admin.getUsername());
                log.setAccount(admin.getName());
            } else {
                log.setAdmin("匿名用户");
                log.setAccount("匿名用户");
            }
        } else {
            log.setAdmin("匿名用户");
            log.setAccount("匿名用户");
        }

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null) {
            log.setIp(IpUtil.getIpAddr(request));
        }
        log.setAddTime(LocalDateTime.now());
        log.setType(type);
        log.setAction(action);
        log.setStatus(succeed);
        log.setResult(result);
        log.setComment(comment);
        log.setDeleted(0);
        logService.save(log);
    }

}
