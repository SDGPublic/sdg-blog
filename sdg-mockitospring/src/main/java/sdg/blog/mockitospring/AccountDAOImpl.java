package sdg.blog.mockitospring;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AccountDAOImpl implements AccountDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Account save(Account account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    public void delete(Account account) {
        entityManager.remove(account);
    }

    @Override
    public Account get(long id) {
        return entityManager.find(Account.class, id);
    }
}
