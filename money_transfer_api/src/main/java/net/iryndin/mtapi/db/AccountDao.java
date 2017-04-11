package net.iryndin.mtapi.db;

import io.dropwizard.hibernate.AbstractDAO;
import net.iryndin.mtapi.core.AccountEntity;
import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class AccountDao extends AbstractDAO<AccountEntity> {

    public AccountDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<AccountEntity> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public AccountEntity createOrUpdate(AccountEntity acc) {
        return persist(acc);
    }
}
