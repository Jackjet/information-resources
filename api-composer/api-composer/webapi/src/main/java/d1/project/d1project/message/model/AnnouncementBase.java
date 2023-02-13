package d1.project.d1project.message.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-19 16:02
 */
@ApiModel(value = "AnnouncementEntity", description = "通知公告基础")
public class AnnouncementBase {

    @ApiModelProperty("标题")
    @Length(message = "标题长度超过100字符", min = 1, max = 100)
    private String title;

    @ApiModelProperty("内容")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
