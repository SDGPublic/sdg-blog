package sdg.blog.spockspring

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ContextConfiguration('classpath:test-context.xml')
@Transactional
class AccountServiceSpringAndSpockSpec extends Specification {
    @SpringBean
    private AuditService auditService = Mock(AuditService)

    @Autowired
    private AccountService accountService

    def 'deleteWithPermission'() {
        given: 'a configured Spring Security token for testing and a persisted test account'
        SecuredUser user = new SecuredUser()
        user.setUsername("test1")
        TestingAuthenticationToken token = new TestingAuthenticationToken(user, null, 'accountFullAccess')
        SecurityContextHolder.getContext().setAuthentication(token)

        Account accountToBeDeleted = accountService.createNewAccount('Test Account')
        long accountId = accountToBeDeleted.getId()

        when: 'the delete method is called on the accountService'
        accountService.delete(accountToBeDeleted)

        then: 'the account is deleted from the database and the auditService is called'
        !accountService.get(accountId)
        1 * auditService.notifyDelete(accountId)
    }
}
