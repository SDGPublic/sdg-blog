package sdg.blog.protectids.dao;

import org.springframework.stereotype.Component;
import sdg.blog.protectids.domain.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class is for demonstration purposes only.
 */
@Component
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;

    public void create(UserEntity newUser) {
        entityManager.persist(newUser);
    }

    public UserEntity find(Long id) {
        return entityManager.find(UserEntity.class, id);
    }

    public UserEntity update(UserEntity userEntity) {
        return entityManager.merge(userEntity);
    }

    public void delete(UserEntity userEntity) {
        entityManager.remove(userEntity);
    }
}
