package d1.project.api.integration.kong.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 * 网关默认存在的用户，用以测试api
 */
@Entity
@Table(name = "d1_default_consumer")
public class DefaultConsumer extends KongConsumer{
}
