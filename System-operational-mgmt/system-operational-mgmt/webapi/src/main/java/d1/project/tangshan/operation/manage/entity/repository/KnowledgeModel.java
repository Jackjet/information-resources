package d1.project.tangshan.operation.manage.entity.repository;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_knowledge_model")
@ApiModel(value = "KnowledgeModel", description = "知识模型")
public class KnowledgeModel extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("模型名称")
    private String name;
    @ApiModelProperty("模型类型 （independentService 独立服务模型\n" +
            "compositeService     复合服务模型\n" +
            "choreographyServices 编排服务模型\n" +
            "rules 规则模型\n" +
            "standardSpecification 标准规范模型\n" +
            "pageComponents 页面组件模型\n" +
            "xmlStructural XML结构模型\n" +
            "modeMatching 模式匹配模型\n" +
            "modeRecognition 模式识别模型\n" +
            "other 其它模型）")
    private String type;
    @ApiModelProperty("内容")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
