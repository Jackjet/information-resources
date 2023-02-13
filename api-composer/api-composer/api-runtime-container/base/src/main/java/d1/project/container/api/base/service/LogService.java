package d1.project.container.api.base.service;

import d1.project.container.api.base.listener.LogWriterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    private static Map<String, LogWriterListener> listeners = new HashMap<>();

    public static void localLog(String apiId, String message) {
        //写入日志文件
        logger.info(apiId + "\t" + message);
    }

    public static void localError(String apiId, Throwable e) {
        logger.error(apiId, e);
    }

    public static void info(String apiId, String message) {
        //写入日志文件
        localLog(apiId, message);

        //实时返回日志
        for (LogWriterListener listener : listeners.values()) {
            listener.info(apiId, message);
        }
    }

    public static void error(String apiId, Throwable e) {
        //写入日志文件
        localError(apiId, e);

        //实时返回日志
        for (LogWriterListener listener : listeners.values()) {
            listener.error(apiId, e);
        }
    }


    public static void setLogWriterListener(String apiId, LogWriterListener listener) {
        if (listener == null) {
            return;
        }
        listeners.put(apiId, listener);
    }

    public static void rmLogWriterListener(String apiId) {
        listeners.remove(apiId);
    }

}
