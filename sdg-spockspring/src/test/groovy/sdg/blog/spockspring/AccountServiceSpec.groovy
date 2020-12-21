package sdg.blog.spockspring

import spock.lang.Specification

class AccountServiceSpec extends Specification {

    private final NotificationService notificationService = Mock(NotificationService)
    private final AccountDAO accountDAO = Mock(AccountDAO)
    private final AuditService auditService = Mock(AuditService)

    private AccountService accountService

    def setup () {
        accountService = new AccountServiceImpl(accountDAO, notificationService, auditService)
    }

    def 'createNewAccount'() {
        given: 'expected data is setup'
        String accountName = 'Test Account'
        Account persistedAccount = new Account(accountName)
        persistedAccount.id = 12345

        when: 'the account is created using the accountService'
        Account createdAccount = accountService.createNewAccount(accountName)

        then: 'the account is saved to the database and the notification service is called'
        createdAccount
        createdAccount == persistedAccount   // Groovy calls the equals() method on Account
        1 * accountDAO.save(_) >> { Account savedAccount ->
            assert savedAccount.name == accountName
            return persistedAccount
        }
        1 * notificationService.notifyOfNewAccount(persistedAccount.id)
    }
}
