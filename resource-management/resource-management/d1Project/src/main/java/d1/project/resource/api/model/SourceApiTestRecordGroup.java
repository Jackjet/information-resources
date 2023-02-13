package d1.project.resource.api.model;

import d1.project.resource.api.entity.SourceApiTestRecord;

import java.util.List;

public class SourceApiTestRecordGroup {
    private Integer id;
    private String requestDay;
    private List<SourceApiTestRecord> sourceApiTestRecords;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestDay() {
        return requestDay;
    }

    public void setRequestDay(String requestDay) {
        this.requestDay = requestDay;
    }

    public List<SourceApiTestRecord> getSourceApiTestRecords() {
        return sourceApiTestRecords;
    }

    public void setSourceApiTestRecords(List<SourceApiTestRecord> sourceApiTestRecords) {
        this.sourceApiTestRecords = sourceApiTestRecords;
    }
}
