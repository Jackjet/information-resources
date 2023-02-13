package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.KeywordSearchDao;
import d1.project.resourcesharingmgmt.resource.entity.KeywordSearchEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * 关键字搜索记录
 *
 * @author zhengyang
 */
@Service
public class KeywordSearchService {
    private final KeywordSearchDao keywordSearchDao;

    public KeywordSearchService(KeywordSearchDao keywordSearchDao) {
        this.keywordSearchDao = keywordSearchDao;
    }

    /**
     * 保存
     *
     * @param model
     * @param request
     * @throws DoValidException
     */
    public void save(KeywordSearchEntity model, HttpServletRequest request) throws DoValidException {
        keywordSearchDao.save(model);
    }

    /**
     * 保存
     *
     * @param keyword
     * @param request
     * @throws DoValidException
     */
    public void save(String keyword, HttpServletRequest request) throws DoValidException {
        KeywordSearchEntity entity = new KeywordSearchEntity();

        if(StringUtils.isEmpty(keyword)){
            return;
        }

        Optional<KeywordSearchEntity> keywordSearchEntityOptional = keywordSearchDao.findByKeyword(keyword);
        if (keywordSearchEntityOptional.isPresent()) {
            entity = keywordSearchEntityOptional.get();
            entity.setNum(entity.getNum() + 1);
        } else {
            entity.setId(BaseUtils.generate32Id());
            entity.setKeyword(keyword);
            entity.setNum(1);
        }

        keywordSearchDao.save(entity);
    }

    /**
     * 查询排名
     *
     * @return
     * @throws Exception
     */
    public List<KeywordSearchEntity> findTopList() throws Exception {
        JSONObject paramObject = new JSONObject();
        paramObject.put("page", 1);
        paramObject.put("size", 10);

        SpecificationBuilder<KeywordSearchEntity> builder = new SpecificationBuilder<>();
        Specification<KeywordSearchEntity> specification = builder.init(paramObject)
                .dOrder("num")
                .build();
        return keywordSearchDao.findAll(specification, builder.getPageable()).getContent();
    }
}
