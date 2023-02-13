package d1.project.api.integration.common.service;

import d1.project.api.integration.apianalysis.entity.ApiLogRecord;

import java.util.List;

public interface IApiLogRecordService {
    int countAll();
    int countByStatus(Integer status);
}
