package d1.project.resourcesharingmgmt.common.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.common.model.UserMessage;

import java.util.Collection;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-16 22:03
 */
public interface IUserMessageService {
    /**
     * 插入消息(没有发送者)
     *
     * @param model  模型
     * @param userId 用户id
     * @throws DoValidException 自定义异常
     */
    void send(UserMessage model, Collection<String> userId) throws DoValidException;

    /**
     * 插入消息
     *
     * @param model    模型
     * @param userId   用户id
     * @param senderId 发送者Id
     * @throws DoValidException 自定义异常
     */
    void send(UserMessage model, Collection<String> userId, String senderId) throws DoValidException;
}
