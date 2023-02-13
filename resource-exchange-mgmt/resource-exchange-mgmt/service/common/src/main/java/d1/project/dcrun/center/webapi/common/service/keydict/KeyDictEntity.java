package d1.project.dcrun.center.webapi.common.service.keydict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 记录一些值的最大值，比如用于创建appid，每次取出appid的值，然后加1
 *
 * @author Buter
 * @date 2020/5/22 11:20
 */

@Entity
@Table(name = "d1_keydict")
@ApiModel(value = "KeyDict", description = "字典表")
public class KeyDictEntity{
    @Id
    @ApiModelProperty(value = "Key")
    private String key;
    @Column(length = 256)
    @ApiModelProperty(value = "Value")
    private String value;

    public KeyDictEntity() {

    }

    public KeyDictEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
