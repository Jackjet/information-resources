package d1.project.resource.api.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resource.api.dao.SourceApiTestRecordDao;
import d1.project.resource.api.entity.SourceApiTestRecord;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baozh
 */
@Service
public class SourceApiTestRecordService {
    private final SourceApiTestRecordDao sourceApiTestRecordDao;

    public SourceApiTestRecordService(SourceApiTestRecordDao sourceApiTestRecordDao) {
        this.sourceApiTestRecordDao = sourceApiTestRecordDao;
    }

    public void save(SourceApiTestRecord data) {
        this.sourceApiTestRecordDao.save(data);
    }

    public List<SourceApiTestRecord> findAllBySourceApiId(String sourceApiId) throws DoValidException {
        return this.sourceApiTestRecordDao.findAllBySourceApiIdOrderByRequestTimeDesc(sourceApiId);
    }

    public SourceApiTestRecord findById(String id) throws DoValidException {
        return this.sourceApiTestRecordDao.findById(id).orElseThrow(() -> new DoValidException("测试记录不存在"));
    }

    public void delete(SourceApiTestRecord data) {
        this.sourceApiTestRecordDao.delete(data);
    }
}

