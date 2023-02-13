package d1.project.api.integration.apianalysis.model;


/**
 * @author baozh
 */
public class ApiStatistic {

    /**
     * 0应用统计 1API统计
     */
    private Integer type;
    private String id;
    private String startTime;
    private String endTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
