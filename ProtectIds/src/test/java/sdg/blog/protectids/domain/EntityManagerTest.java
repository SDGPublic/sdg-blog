package sdg.blog.protectids.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import sdg.blog.protectids.helper.SetIdHelper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml",
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class EntityManagerTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testMergeWithNonExistentId() {

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Bob");
        userEntity.setLastName("Marley");

        // Create new user
        entityManager.persist(userEntity);
        assertNotNull(userEntity.getId());
        assertEquals(1, userEntity.getId(), 0);

        // Remove the user before the subsequent update is run
        entityManager.remove(userEntity);

        // Update the user with id 1 that is expected to be in the database (change the first name to Robert)
        UserEntity updatedUserEntity = new UserEntity();
        updatedUserEntity.setFirstName("Robert");
        updatedUserEntity.setLastName("Marley");
        SetIdHelper.setId(updatedUserEntity, 1L);   // This id does not exist in the database anymore
        UserEntity persistedUserEntity = entityManager.merge(updatedUserEntity);

        // A new object has been created in the database.  This may not be the behavior that is desired.
        assertNotEquals(1L, persistedUserEntity.getId(), 0);
        assertEquals(userEntity.getId() + 1, persistedUserEntity.getId(), 0);
        assertNotNull(entityManager.find(UserEntity.class, persistedUserEntity.getId()));
    }
}
