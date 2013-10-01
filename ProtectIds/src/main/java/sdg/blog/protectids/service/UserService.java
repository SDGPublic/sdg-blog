package sdg.blog.protectids.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdg.blog.protectids.dao.UserDao;
import sdg.blog.protectids.domain.UserEntity;

/**
 * An example User Service that has a single function to update a name.  In an actual project, more
 * functions would be included.
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuditService auditService;

    public void updateName(Long id, String firstName, String lastName) {

        UserEntity userEntity = userDao.find(id);
        if (userEntity != null) {
            userEntity.setFirstName(firstName);
            userEntity.setLastName(lastName);

            userDao.update(userEntity);
            auditService.auditUpdate(UserEntity.class, userEntity.getId());
        }
        else {
            // This would probably be a different exception in an actual project
            throw new RuntimeException("A user with id " + id + " could not be found");
        }
    }

}
