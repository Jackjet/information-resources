package d1.project.container.task.utils;

import org.pentaho.di.job.Job;

import java.util.HashMap;
import java.util.Map;

/**
 * @author baozh
 */
public class JobCache {

    private static JobCache jobCache;
    private static Map<String, Job> cacheMap;

    private JobCache() {
        cacheMap = new HashMap<String, Job>();
    }

    public static JobCache getInstance() {
        if (jobCache == null) {
            jobCache = new JobCache();
        }
        return jobCache;
    }

    public void addCacheData(String key, Job obj) {
        cacheMap.put(key, obj);
    }

    public Job getCacheData(String key) {
        return cacheMap.get(key);
    }

    public void removeCacheData(String key) {
        cacheMap.remove(key);
    }
}
