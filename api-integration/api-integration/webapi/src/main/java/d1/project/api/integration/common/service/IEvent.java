package d1.project.api.integration.common.service;

import java.util.Collection;

/**
 * @author liuyi
 */
public interface IEvent {
    /**
     * 接收到消息后触发消息的处理调用
     *
     * @param data 消息带的数据
     */
    void invoke(Collection<String> data);

}
