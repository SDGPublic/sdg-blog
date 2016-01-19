package sdg.blog.mockitospring;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:test-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class AccountServiceSpringAndMockitoTest {

    @Mock
    private AuditService mockAuditService;

    @InjectMocks
    @Autowired
    private AccountService accountService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Transactional
    public void deleteWithPermission() {

        // Set up Spring Security token for testing
        SecuredUser user = new SecuredUser();
        user.setUsername("test1");
        TestingAuthenticationToken token = new TestingAuthenticationToken(user, null, "accountFullAccess");
        SecurityContextHolder.getContext().setAuthentication(token);

        Account accountToBeDeleted = accountService.createNewAccount("Test Account");

        long accountId = accountToBeDeleted.getId();
        doNothing().when(mockAuditService).notifyDelete(accountId);

        accountService.delete(accountToBeDeleted);

        assertNull(accountService.get(accountId));
        verify(mockAuditService).notifyDelete(accountId);
    }
}
