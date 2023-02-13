package d1.project.tangshan.operation.manage.model;

import io.swagger.annotations.ApiModelProperty;

public class SystemMonitorEntityVm {

    private String date;
    private String dataSource;
    private String memorySource;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getMemorySource() {
        return memorySource;
    }

    public void setMemorySource(String memorySource) {
        this.memorySource = memorySource;
    }
}
