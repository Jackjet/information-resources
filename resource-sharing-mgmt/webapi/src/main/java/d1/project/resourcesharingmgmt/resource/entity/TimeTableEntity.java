package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 同步数据时间表
 * @author maoyuying
 */
@Entity
@Table(name = "d1_time_table")
public class TimeTableEntity extends BaseCreateEntity {
    /**
     * 表名
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
