package sdg.blog.mockitospring;

public interface AccountService {

    Account createNewAccount(String name);

    void delete(Account account);

    Account get(long id);
}
