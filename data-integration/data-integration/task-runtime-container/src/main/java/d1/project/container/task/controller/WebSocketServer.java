package d1.project.container.task.controller;

import com.alibaba.fastjson.JSONObject;
import d1.project.container.task.listener.LogWriterListener;
import org.pentaho.di.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/task/container/{taskId}")
@Component
public class WebSocketServer implements LogWriterListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String taskId = "";

    private Timer timer;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("taskId") String taskId) {
        this.session = session;
        this.taskId = taskId;
        timer = new Timer();
        if (webSocketMap.containsKey(taskId)) {
            webSocketMap.remove(taskId);
            //加入set中
            webSocketMap.put(taskId, this);
        } else {
            //加入set中
            webSocketMap.put(taskId, this);
            //在线数加1
            addOnlineCount();
        }

        logger.info("客户端连接:" + taskId + ",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("info", taskId, "连接成功");
        } catch (IOException e) {
            logger.error("客户端连接异常:" + taskId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(taskId)) {
            //从set中删除
            webSocketMap.remove(taskId);
            subOnlineCount();
        }
        if (timer != null) {
            timer.cancel();
        }
        logger.info("客户端退出:" + taskId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("客户端消息:" + taskId + ",报文:" + message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("客户端错误:" + this.taskId + "原因:", error);
    }


    @Override
    public void info(String taskId, String message) {
        log("info", taskId, message);
    }

    @Override
    public void error(String taskId, Throwable e) {
        log("error", taskId, e.getMessage());
    }


    public void log(String type, String taskId, String message) {
        logger.info("发送消息到:" + taskId + "，报文:" + message);
        if (StringUtil.isEmpty(taskId) || !webSocketMap.containsKey(taskId)) {
            logger.error("客户端" + taskId + ",不在线！");
            return;
        }

        try {
            webSocketMap.get(taskId).sendMessage(type, taskId, message);
        } catch (IOException e) {
            logger.error("客户端" + taskId + "发送失败！", e);
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String type, String taskId, Object msg) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        JSONObject message = new JSONObject();
        message.put("id", taskId);
        message.put("timestamp", sdf.format(new Date()));
        message.put("type", type);
        message.put("body", msg);
        this.session.getBasicRemote().sendText(message.toJSONString());
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String taskId, String message) throws IOException {
        logger.info("推送消息到窗口"+taskId+"，推送内容:"+message);
        webSocketMap.get(taskId).sendMessage("info", taskId, message);
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
