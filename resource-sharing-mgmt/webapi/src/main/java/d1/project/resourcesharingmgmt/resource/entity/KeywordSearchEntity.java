package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

/**
 * 关键字搜索记录
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_keyword_search_log")
@ApiModel(value = "KeywordSearchEntity", description = "关键字搜索记录")
public class KeywordSearchEntity extends BaseEntity {
    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("搜索数量")
    private int num;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
