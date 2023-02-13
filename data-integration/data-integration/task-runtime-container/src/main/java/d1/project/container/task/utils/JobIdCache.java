package d1.project.container.task.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author baozh
 */
public class JobIdCache {

    private static JobIdCache jobCache;
    private static Map<String, String> cacheMap;

    private JobIdCache() {
        cacheMap = new HashMap<String, String>();
    }

    public static JobIdCache getInstance() {
        if (jobCache == null) {
            jobCache = new JobIdCache();
        }
        return jobCache;
    }

    public void addCacheData(String key, String obj) {
        cacheMap.put(key, obj);
    }

    public String getCacheData(String key) {
        return cacheMap.get(key);
    }

    public void removeCacheData(String key) {
        cacheMap.remove(key);
    }
}
