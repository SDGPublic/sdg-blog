package sdg.blog.mockitospring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuditService auditService;

    @Override
    public Account createNewAccount(String name) {
        Account account = new Account(name);

        Account persistedAccount = accountDAO.save(account);
        notificationService.notifyOfNewAccount(persistedAccount.getId());

        return persistedAccount;
    }

    // todo: Set up Spring Security annotations to only allow delete if user has full access
    @Override
    public void delete(Account account) {
        accountDAO.delete(account);

        auditService.notifyDelete(account.getId());
    }

    @Override
    public Account get(long id) {
         return accountDAO.get(id);
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
