package d1.project.dataintegration.message.dao;

import d1.framework.webapi.dao.BatchExecuteRepository;
import d1.project.dataintegration.message.entity.UserMessageEntity;

import javax.persistence.EntityManager;

/**
 * @author Buter
 * 重写saveall，提高saveall效率
 * @date 2020/9/25 10:07
 */
public class UserMessageDaoImpl extends BatchExecuteRepository<UserMessageEntity, String> {
    public UserMessageDaoImpl(EntityManager entityManager) {
        super(UserMessageEntity.class, entityManager);
    }
}
