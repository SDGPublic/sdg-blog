package sdg.blog.mockitospring;

public interface AccountDAO {
    /**
     * Save the Account
     * @param account
     * @return the saved account with the persistent id
     */
    Account save(Account account);

    void delete(Account account);

    Account get(long id);
}
