package d1.project.resource.api.model;

import d1.project.resource.api.entity.SourceApiTestCase;

import java.util.List;

public class SourceApiTestCaseGroup {
    private Integer id;
    private String createDay;
    private List<SourceApiTestCase> sourceApiTestCases;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public List<SourceApiTestCase> getSourceApiTestCases() {
        return sourceApiTestCases;
    }

    public void setSourceApiTestCases(List<SourceApiTestCase> sourceApiTestCases) {
        this.sourceApiTestCases = sourceApiTestCases;
    }
}
