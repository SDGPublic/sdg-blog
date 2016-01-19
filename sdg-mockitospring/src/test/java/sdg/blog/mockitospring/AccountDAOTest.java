package sdg.blog.mockitospring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:test-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class AccountDAOTest {

    @Autowired
    private AccountDAO accountDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void save() {

        Account newAccount = new Account("Test Account");

        assertNotNull(accountDAO);
        accountDAO.save(newAccount);

        assertNotNull(newAccount.getId());

        Account reloaded = flushAndGet(newAccount.getId());

        assertEquals(newAccount.getId(), reloaded.getId());
        assertEquals(newAccount.getName(), reloaded.getName());
    }

    private Account flushAndGet(long id) {
        entityManager.flush();
        return entityManager.find(Account.class, id);
    }
}
