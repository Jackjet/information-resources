package d1.project.container.api.controller;

import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.listener.LogWriterListener;
import d1.project.container.api.base.service.LogService;
import d1.project.container.api.base.utils.Utils;
import d1.project.container.api.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/api/container/{apiId}")
@Component
public class WebSocketServer implements LogWriterListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String apiId = "";

    private Timer timer;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("apiId") String apiId) {
        this.session = session;
        this.apiId = apiId;
        timer = new Timer();
        LogService.setLogWriterListener(apiId, this);
        if (webSocketMap.containsKey(apiId)) {
            webSocketMap.remove(apiId);
            //加入set中
            webSocketMap.put(apiId, this);
        } else {
            //加入set中
            webSocketMap.put(apiId, this);
            //在线数加1
            addOnlineCount();
        }

        logger.info("客户端连接:" + apiId + ",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("info", apiId, "连接成功");
        } catch (IOException e) {
            logger.error("客户端连接异常:" + apiId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(apiId)) {
            //从set中删除
            webSocketMap.remove(apiId);
            subOnlineCount();
        }
        if (timer != null) {
            timer.cancel();
        }
        LogService.rmLogWriterListener(apiId);
        logger.info("客户端退出:" + apiId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("客户端消息:" + apiId + ",报文:" + message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("客户端错误:" + this.apiId + "原因:", error);
    }


    @Override
    public void info(String apiId, String message) {
        log("info", apiId, message);
    }

    @Override
    public void error(String apiId, Throwable e) {
        log("error", apiId, e.getMessage());
    }


    public void log(String type, String apiId, String message) {
        logger.info("发送消息到:" + apiId + "，报文:" + message);
        if (Utils.isEmpty(apiId) || !webSocketMap.containsKey(apiId)) {
            logger.error("客户端" + apiId + ",不在线！");
            return;
        }

        try {
            webSocketMap.get(apiId).sendMessage(type, apiId, message);
        } catch (IOException e) {
            logger.error("客户端" + apiId + "发送失败！", e);
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String type, String apiId, Object msg) throws IOException {
        JSONObject message = new JSONObject();
        message.put("id", apiId);
        message.put("timestamp", MyUtils.getCurrentFormatTime());
        message.put("type", type);
        message.put("body", msg);
        this.session.getBasicRemote().sendText(message.toJSONString());
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
