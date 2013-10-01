package sdg.blog.protectids.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sdg.blog.protectids.dao.UserDao;
import sdg.blog.protectids.domain.UserEntity;
import sdg.blog.protectids.helper.SetIdHelper;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserDao mockUserDao;

    @Mock
    AuditService mockAuditService;

    @InjectMocks
    UserService userService;

    @Test
    public void testUpdateName() throws Exception {

        Long id = 789L;
        UserEntity userEntity = buildPersistedUserEntity(id);
        when(mockUserDao.find(id)).thenReturn(userEntity);

        userService.updateName(id, "Robert", "McKenzie");

        //Verify that the update method is called with the user entity object returned from the find method
        verify(mockUserDao).update(userEntity);
        //Verify that the auditUpdate method is called with the id from the entity object returned from the find method
        verify(mockAuditService).auditUpdate(UserEntity.class, id);
    }

    private UserEntity buildPersistedUserEntity(Long id) {
        UserEntity user = new UserEntity();
        user.setFirstName("Bob");
        user.setLastName("McKenzie");
        // Example of using SetIdHelper
        SetIdHelper.setId(user, id);
        return user;
    }
}
