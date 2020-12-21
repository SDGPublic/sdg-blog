package sdg.blog.spockspring;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AccountDAOImpl implements AccountDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Account save(Account account) {
        entityManager.persist(account);
        return account;
    }

    public void delete(Account account) {
        entityManager.remove(account);
    }

    public Account get(long id) {
        return entityManager.find(Account.class, id);
    }
}
