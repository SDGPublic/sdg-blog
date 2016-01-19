package sdg.blog.mockitospring;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private AccountDAO accountDAO;

    @InjectMocks
    private AccountServiceImpl accountService = new AccountServiceImpl();

    @Test
    public void createNewAccount() {

        // Expected objects
        String name = "Test Account";
        Account accountToSave = new Account(name);
        long accountId = 12345;
        Account persistedAccount = new Account(name);
        persistedAccount.setId(accountId);

        when(accountDAO.save(any(Account.class))).thenReturn(persistedAccount);
        doNothing().when(notificationService).notifyOfNewAccount(accountId);

        Account newAccount = accountService.createNewAccount(name);

        assertNotNull(newAccount);
        assertEquals(accountId, newAccount.getId());
        assertEquals(name, newAccount.getName());

        verify(notificationService).notifyOfNewAccount(accountId);
        verify(accountDAO).save(accountToSave);
    }
}
