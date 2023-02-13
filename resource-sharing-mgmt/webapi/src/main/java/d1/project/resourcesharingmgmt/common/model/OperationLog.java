package d1.project.resourcesharingmgmt.common.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.Calendar;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:25
 */
@ApiModel(value = "OperationLog", description = "新增操作菜单")
public class OperationLog {

    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("接口")
    private String api;

    @ApiModelProperty("内容描述\ttext\t内容描述为简易信息")
    private String contentMsg;

    @ApiModelProperty("内容\ttext\t添加的详情为json")
    private String content;

    @ApiModelProperty("结果:0->失败.1->成功")
    private int result;

    @ApiModelProperty("开始时间")
    private Calendar startTime;

    @ApiModelProperty("结束时间")
    private Calendar endTime;

    @ApiModelProperty("耗时:选用")
    private long elapsedTime;

    public OperationLog(String module, String api, String contentMsg, String content, int result, Calendar startTime, Calendar endTime, long elapsedTime) {
        this.module = module;
        this.api = api;
        this.contentMsg = contentMsg;
        this.content = content;
        this.result = result;
        this.startTime = startTime;
        this.endTime = endTime;
        this.elapsedTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();
    }

    public OperationLog(String module, String api, String contentMsg, String content, int result) {
        this.module = module;
        this.api = api;
        this.contentMsg = contentMsg;
        this.content = content;
        this.result = result;
        this.startTime = null;
        this.endTime = null;
        this.elapsedTime = 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentMsg() {
        return contentMsg;
    }

    public void setContentMsg(String contentMsg) {
        this.contentMsg = contentMsg;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                "module='" + module + '\'' +
                ", api='" + api + '\'' +
                ", contentMsg='" + contentMsg + '\'' +
                ", content='" + content + '\'' +
                ", result=" + result +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", elapsedTime=" + elapsedTime +
                '}';
    }
}
