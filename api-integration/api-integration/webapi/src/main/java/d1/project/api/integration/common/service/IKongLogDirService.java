package d1.project.api.integration.common.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.api.integration.kong.entity.KongLogDir;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IKongLogDirService {
    List<KongLogDir> findAll(JSONObject param) throws Exception;
}
