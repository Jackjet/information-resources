package d1.project.container.task.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author baozh
 */
public class JobGroupIdCache {

    private static JobGroupIdCache jobCache;
    private static Map<String, String> cacheMap;

    private JobGroupIdCache() {
        cacheMap = new HashMap<String, String>();
    }

    public static JobGroupIdCache getInstance() {
        if (jobCache == null) {
            jobCache = new JobGroupIdCache();
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
