package d1.project.resource.api.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;
import d1.project.resource.common.utils.BaseUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * @author baozh
 */
public class TokenManager {

    public static void updateCreateIdAndTime(HttpServletRequest request, BaseCreateEntity entity) throws DoValidException {
        JSONObject user = BaseUtils.getUserInfo(request);
        entity.setCreateById(user.getString("id"));
        entity.setCreateTime(Calendar.getInstance());
    }

    public static void updateUpdateIdAndTime(HttpServletRequest request, BaseCreateAndUpdateEntity entity) throws DoValidException {
        JSONObject user = BaseUtils.getUserInfo(request);
        entity.setUpdateById(user.getString("id"));
        entity.setUpdateTime(Calendar.getInstance());
    }

}
