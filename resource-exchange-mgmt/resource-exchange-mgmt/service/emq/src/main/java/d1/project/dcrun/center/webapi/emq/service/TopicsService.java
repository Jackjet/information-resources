package d1.project.dcrun.center.webapi.emq.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.keydict.KeyDictService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.emq.dao.TopicConfigDao;
import d1.project.dcrun.center.webapi.emq.dao.TopicsDao;
import d1.project.dcrun.center.webapi.emq.entity.TopicConfig;
import d1.project.dcrun.center.webapi.emq.entity.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author xuaa
 */
@Service
public class TopicsService {

    private final TopicsDao topicDao;
    private final TopicConfigDao topicConfigDao;
    private final TokenService tokenService;
    private final KeyDictService keyDictService;
    @Autowired
    public TopicsService(TopicsDao topicDao, TopicConfigDao topicConfigDao, TokenService tokenService, KeyDictService keyDictService) {
        this.topicDao = topicDao;
        this.topicConfigDao = topicConfigDao;
        this.tokenService = tokenService;
        this.keyDictService = keyDictService;
    }

    /**
     * 查询列表
     *
     * @param params 查询参数
     * @return 返回参数
     * @throws Exception 抛出异常
     */
    public Page<Topics> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Topics> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sContain("topicName", "topicName")
                .dOrder("createTime").build();

        return this.topicDao.findAll(specification, builder.getPageable());
    }

    public void insert(HttpServletRequest request, JSONObject model) throws Exception {

        String integrationId = model.getString("integrationId");
        String topicName = MyUtils.eliminateSpaces(model.getString("topicName"));
        String sysServiceId = model.getString("sysServiceId");

        if (this.topicDao.existsByIntegrationIdAndTopicNameAndSysServiceId(integrationId, topicName, sysServiceId)) {
            throw new ValidException("该系统服务下的topic名称已存在");
        }

        Topics entity = JSONObject.parseObject(model.toJSONString(), Topics.class);
        entity.setId(keyDictService.generateAppId());
        tokenService.updateCreateIdAndTime(request, entity);
        topicDao.save(entity);
    }

    /**
     * 删除
     *
     * @param id id
     * @throws Exception 抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) throws Exception {
        if (!topicDao.existsById(id)) {
            throw new ValidException("未找到id=" + id + "的资源");
        }

        List<TopicConfig> topicCons = topicConfigDao.findAllByTopicId(id);
        if (topicCons != null && topicCons.size() > 0) {
            topicConfigDao.deleteInBatch(topicCons);
        }
        topicDao.deleteById(id);
    }

    public Topics findById(String topicId) {
        Optional<Topics> topics = topicDao.findById(topicId);
        return topics.orElse(null);
    }
}
