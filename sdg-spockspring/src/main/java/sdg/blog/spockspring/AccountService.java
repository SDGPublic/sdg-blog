package sdg.blog.spockspring;

public interface AccountService {

    Account createNewAccount(String name);

    void delete(Account account);

    Account get(long id);
}
