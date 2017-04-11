package net.iryndin.mtapi.db;

import io.dropwizard.hibernate.AbstractDAO;
import net.iryndin.mtapi.core.TransactionEntity;
import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class TransactionDao extends AbstractDAO<TransactionEntity> {

    public TransactionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<TransactionEntity> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public TransactionEntity createOrUpdate(TransactionEntity tx) {
        return persist(tx);
    }
}
