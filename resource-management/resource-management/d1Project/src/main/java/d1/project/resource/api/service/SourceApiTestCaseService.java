package d1.project.resource.api.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resource.api.dao.SourceApiTestCaseDao;
import d1.project.resource.api.entity.SourceApiTestCase;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baozh
 */
@Service
public class SourceApiTestCaseService {
    private final SourceApiTestCaseDao sourceApiTestCaseDao;

    public SourceApiTestCaseService(SourceApiTestCaseDao sourceApiTestCaseDao) {
        this.sourceApiTestCaseDao = sourceApiTestCaseDao;
    }

    public List<SourceApiTestCase> findAll() {
        return this.sourceApiTestCaseDao.findAll();
    }

    public void save(SourceApiTestCase data) {
        this.sourceApiTestCaseDao.save(data);
    }


    public SourceApiTestCase findById(String id) throws DoValidException {
        return this.sourceApiTestCaseDao.findById(id).orElseThrow(() -> new DoValidException("测试用例不存在"));
    }

    public void delete(SourceApiTestCase data) {
        this.sourceApiTestCaseDao.delete(data);
    }

    public List<SourceApiTestCase> findAllBySourceApiId(String sourceApiId) {
        return this.sourceApiTestCaseDao.findAllBySourceApiIdOrderByCreateTimeDesc(sourceApiId);
    }
}
