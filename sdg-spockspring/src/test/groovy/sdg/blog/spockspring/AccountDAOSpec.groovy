package sdg.blog.spockspring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@ContextConfiguration('classpath:test-context.xml')
@Transactional
class AccountDAOSpec extends Specification {
    @Autowired
    private AccountDAO accountDAO;

    @PersistenceContext
    private EntityManager entityManager;

    def 'saveNewAccount'() {
        given: 'a new account to save'
        Account newAccount = new Account("Test Account")

        when: 'the account is saved to the database'
        accountDAO.save(newAccount)

        then: 'the id is set on the account object and the data saved to the database as expected'
        newAccount.id
        Account reloaded = flushAndGet(newAccount.getId())
        newAccount.id == reloaded.id
        newAccount.name == reloaded.name
    }

    private Account flushAndGet(long id) {
        entityManager.flush()
        return entityManager.find(Account.class, id)
    }
}
