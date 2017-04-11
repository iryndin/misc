package net.iryndin.mtapi.db;

import io.dropwizard.hibernate.AbstractDAO;
import net.iryndin.mtapi.core.TransactionEntity;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;
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

    public Criteria createCriteria() {
        return super.criteria();
    }

    public List<TransactionEntity> list(Criteria criteria) throws HibernateException {
        return super.list(criteria);
    }
}
