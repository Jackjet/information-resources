package d1.project.resourcesharingmgmt.resource.service;

import d1.project.resourcesharingmgmt.resource.dao.TimeTableDao;
import d1.project.resourcesharingmgmt.resource.entity.TimeTableEntity;

import java.util.Optional;

/**
 * @author maoyuying
 */
public class TimeTableService {

    private final TimeTableDao timeTableDao;

    public TimeTableService(TimeTableDao timeTableDao){
        this.timeTableDao=timeTableDao;
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<TimeTableEntity> find(String id) {
        return timeTableDao.findById(id);
    }
}
