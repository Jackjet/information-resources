package d1.project.d1project.common.service;

import d1.project.d1project.business.entity.TaskGroup;

import java.util.List;

public interface ITaskGroupService {
    List<TaskGroup> findAll();
}
