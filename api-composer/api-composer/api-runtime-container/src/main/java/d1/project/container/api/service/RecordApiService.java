package d1.project.container.api.service;

import d1.project.container.api.base.Context;

import java.util.HashMap;
import java.util.Map;

public class RecordApiService {
    private final static RecordApiService RECORD_API_SERVICE = new RecordApiService();

    private Map<String, Context> recordApis = new HashMap<>();

    private RecordApiService() {
    }

    public static RecordApiService getInstance() {
        return RECORD_API_SERVICE;
    }

    public void registerApi(String path, String method, Context context) {
        recordApis.put(path + "##" + method.toLowerCase(), context);
    }

    public void unRegisterApi(String path, String method) {
        recordApis.remove(path + "##" + method.toLowerCase());
    }

    public Context getContext(String path, String method){
        return recordApis.get(path + "##" + method.toLowerCase());
    }

    public void clear() {
        recordApis.clear();
    }


}
