package d1.project.api.integration.log.model;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Date;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-22 15:35
 */
public class LogExcelBase {
    @ExcelProperty(value = "操作人", index = 0)
    private String createByName;

    @ExcelProperty(value = "操作类型", index = 2)
    private String api;

    @ExcelProperty(value = "操作内容", index = 3)
    private String contentMsg;

    @ExcelProperty(value = "操作结果", index = 4)
    private String result;

    @ExcelProperty(value = "操作时间", index = 5)
    private Date createTime;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getContentMsg() {
        return contentMsg;
    }

    public void setContentMsg(String contentMsg) {
        this.contentMsg = contentMsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(int result) {
        if (result == 0) {
            this.result = "失败";
        } else {
            this.result = "成功";
        }
    }


    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        }
        return (Date) createTime.clone();
    }

    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }
}
