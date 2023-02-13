package d1.project.resource.common.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.resource.common.model.OperationLog;

import javax.servlet.http.HttpServletRequest;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-16 18:49
 */
public interface IOperationLogService {

    /**
     * 插入
     *
     * @param model   模型
     * @param request 请求
     * @throws DoValidException 自定义异常
     */
    void insert(OperationLog model, HttpServletRequest request) throws DoValidException;

    /**
     * 插入
     *
     * @param model    模型
     * @param userInfo 用户信息
     * @throws DoValidException 自定义异常
     */
    void insertLog(OperationLog model, JSONObject userInfo) throws DoValidException;
}
