package d1.project.api.integration.message.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.model.OperationLog;
import d1.project.api.integration.common.model.UserMessage;
import d1.project.api.integration.common.service.EventBus;
import d1.project.api.integration.common.service.IEvent;
import d1.project.api.integration.common.service.IOperationLogService;
import d1.project.api.integration.common.service.IUserMessageService;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.message.dao.UserMessageDao;
import d1.project.api.integration.message.entity.UserMessageEntity;
import d1.project.api.integration.system.model.vm.UserMessageFindAllVm;
import d1.project.api.integration.system.model.vm.UserMessageUpdateStatusAllVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 22:09
 */
@Service
public class UserMessageServiceImpl implements IEvent, IUserMessageService {

    private final UserMessageDao userMessageDao;

    private final IOperationLogService iOperationLogService;

    public UserMessageServiceImpl(UserMessageDao userMessageDao, IOperationLogService iOperationLogService) {
        this.userMessageDao = userMessageDao;
        this.iOperationLogService = iOperationLogService;
        EventBus.getInstance().on(Constants.USER_DELETE_EVENT, this);
    }

    @Override
    public void send(UserMessage message, Collection<String> userIds) throws DoValidException {
        send(message, userIds, null);
    }

    @Override
    public void send(UserMessage message, Collection<String> userIds, String sendId) throws DoValidException {
        if (message == null) {
            throw new DoValidException("??????????????????");
        }
        if (userIds.isEmpty()) {
            throw new DoValidException("????????????????????????");
        }
        List<UserMessageEntity> collect = userIds.stream().map(
                userId -> new UserMessageEntity(message, userId, sendId)).collect(Collectors.toList());
        userMessageDao.saveAll(collect);
    }

    /**
     * ??????
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        String userId = BaseUtils.getUserInfo(request).getString("id");
        UserMessageEntity userMessageEntity = userMessageDao.findByIdAndUserId(id, userId).orElseThrow(() -> new DoValidException("?????????????????????"));
        if (userMessageEntity != null) {
            userMessageDao.delete(userMessageEntity);
            iOperationLogService.insert(new OperationLog("????????????_????????????", "??????", "????????????" + userMessageEntity.getMessageTitle(), JSON.toJSONString(userMessageEntity), 1), request);
        }
    }

    /**
     * ????????????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Collection<String> ids, HttpServletRequest request) throws DoValidException {
        String userId = BaseUtils.getUserInfo(request).getString("id");
        List<UserMessageEntity> allByIdInAndUserId = userMessageDao.findByIdInAndUserId(ids, userId);
        List<String> collect = allByIdInAndUserId.stream().map(UserMessageEntity::getMessageTitle).collect(Collectors.toList());

        userMessageDao.deleteAll(allByIdInAndUserId);
        iOperationLogService.insert(new OperationLog("????????????_????????????", "????????????", "??????????????????" + String.join(",", collect), JSONArray.toJSONString(allByIdInAndUserId), 1), request);
    }

    /**
     * ??????????????????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAllStatus(UserMessageUpdateStatusAllVm model, HttpServletRequest request) throws DoValidException {
        String userId = BaseUtils.getUserInfo(request).getString("id");
        List<UserMessageEntity> allByIdInAndUserId = userMessageDao.findByIdInAndUserId(model.getIds(), userId);
        List<UserMessageEntity> collect = allByIdInAndUserId.stream().peek(userMessage -> userMessage.setStatus(1)).collect(Collectors.toList());
        userMessageDao.saveAll(collect);
        List<String> collect1 = collect.stream().map(UserMessageEntity::getMessageTitle).collect(Collectors.toList());
        iOperationLogService.insert(new OperationLog("????????????_????????????", "????????????", "??????????????????" + String.join(",", collect1), JSONArray.toJSONString(collect), 1), request);
    }

    /**
     * ??????????????????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusAll(HttpServletRequest request) throws DoValidException {
        String userId = BaseUtils.getUserInfo(request).getString("id");
        List<UserMessageEntity> allByIdInAndUserId = userMessageDao.findByUserIdAndStatus(userId, 0);
        List<UserMessageEntity> collect = allByIdInAndUserId.stream().peek(userMessage -> userMessage.setStatus(1)).collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new DoValidException("??????????????????????????????");
        }
        userMessageDao.saveAll(collect);
        List<String> collect1 = collect.stream().map(UserMessageEntity::getMessageTitle).collect(Collectors.toList());
        iOperationLogService.insert(new OperationLog("????????????_????????????", "????????????", "??????????????????" + String.join(",", collect1), JSONArray.toJSONString(collect), 1), request);
    }

    /**
     * ????????????
     *
     * @param model ??????
     */
    public Page<UserMessageEntity> findAll(UserMessageFindAllVm model, HttpServletRequest request) throws Exception {
        String userId = BaseUtils.getUserInfo(request).getString("id");
        SpecificationBuilder<UserMessageEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        paramObject.put("userId", userId);
        Specification<UserMessageEntity> specification = builder.init(paramObject)
                .sContain("messageTitle", "messageTitle")
                .sContain("userId", "userId")
                .sContain("messageModule", "messageModule")
                .nEqual("status", "status")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return userMessageDao.findAll(specification, builder.getPageable());
    }

    /**
     * ??????,??????????????????
     *
     * @param id id
     */
    public Optional<UserMessageEntity> find(String id, HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = BaseUtils.getUserInfo(request);
        String userId = userByToken.getString("id");
        Optional<UserMessageEntity> byId = userMessageDao.findById(id);
        byId.ifPresent(userMessage -> {
            if (userMessage.getUserId().equals(userId)) {
                userMessage.setStatus(1);
                userMessageDao.save(userMessage);
            }
        });
        return byId;
    }


    @Override
    public void invoke(Collection<String> data) {
        userMessageDao.deleteByUserIdIn(data);
    }


}
