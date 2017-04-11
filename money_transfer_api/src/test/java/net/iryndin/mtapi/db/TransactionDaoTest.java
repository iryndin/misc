package net.iryndin.mtapi.db;

import com.google.common.collect.Lists;
import io.dropwizard.testing.junit.DAOTestRule;
import net.iryndin.mtapi.core.AccountEntity;
import net.iryndin.mtapi.core.TransactionEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class TransactionDaoTest {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(AccountEntity.class)
            .addEntityClass(TransactionEntity.class)
            .build();

    private AccountDao accountDao;
    private TransactionDao transactionDao;

    @Before
    public void setUp() throws Exception {
        accountDao = new AccountDao(daoTestRule.getSessionFactory());
        transactionDao = new TransactionDao(daoTestRule.getSessionFactory());
    }

    @Test
    public void testCreateOrUpdate() {
        final AccountEntity accCredit = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 11, new Date(), new Date())));
        final AccountEntity accDebit = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 22, new Date(), new Date())));

        final TransactionEntity tx = daoTestRule.inTransaction(() ->
                transactionDao.createOrUpdate(new TransactionEntity(null, 11, new Date(),
                        "description 1", 1, accCredit, accDebit)));

        assertThat(tx.getId()).isGreaterThan(0);
        assertThat(tx.getAmount()).isEqualTo(11);
        assertThat(tx.getCreateDate()).isNotNull();
        assertThat(tx.getDescription()).isEqualTo("description 1");
        assertThat(tx.getType()).isEqualTo(1);
        assertThat(tx.getCreditAccount().getId()).isEqualTo(accCredit.getId());
        assertThat(tx.getDebitAccount().getId()).isEqualTo(accDebit.getId());
    }

    @Test
    public void testFindById() {
        final AccountEntity accCredit = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 100, new Date(), new Date())));
        final AccountEntity accDebit = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 22, new Date(), new Date())));

        final List<Long> txtIds = Lists.newArrayList();
        daoTestRule.inTransaction(() -> {
            txtIds.add(transactionDao.createOrUpdate(new TransactionEntity(null, 11, new Date(),
                    "description 1", 1, accCredit, accDebit)).getId());
            txtIds.add(transactionDao.createOrUpdate(new TransactionEntity(null, 22, new Date(),
                    "description 1", 1, accCredit, accDebit)).getId());
            txtIds.add(transactionDao.createOrUpdate(new TransactionEntity(null, 33, new Date(),
                    "description 1", 1, accCredit, accDebit)).getId());
        });

        assertThat(transactionDao.findById(txtIds.get(0)).isPresent()).isTrue();
        assertThat(transactionDao.findById(txtIds.get(1)).isPresent()).isTrue();
        assertThat(transactionDao.findById(txtIds.get(2)).isPresent()).isTrue();
        assertThat(transactionDao.findById(777L).isPresent()).isFalse();
    }

    @Test
    public void testList() {
        final AccountEntity accCredit1 = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 100, new Date(), new Date())));
        final AccountEntity accDebit1 = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 22, new Date(), new Date())));

        final AccountEntity accCredit2 = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 100, new Date(), new Date())));
        final AccountEntity accDebit2 = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 22, new Date(), new Date())));
        final AccountEntity accDebit3 = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 33, new Date(), new Date())));

        final List<Long> txtIds = Lists.newArrayList();
        daoTestRule.inTransaction(() -> {
            txtIds.add(transactionDao.createOrUpdate(new TransactionEntity(null, 11, new Date(),
                    "description 1", 1, accCredit1, accDebit1)).getId());
            txtIds.add(transactionDao.createOrUpdate(new TransactionEntity(null, 22, new Date(),
                    "description 1", 1, accCredit2, accDebit2)).getId());
            txtIds.add(transactionDao.createOrUpdate(new TransactionEntity(null, 33, new Date(),
                    "description 1", 1, accCredit1, accDebit3)).getId());
        });

        Criteria cr = transactionDao.createCriteria();
        cr.add(Restrictions.eq("creditAccount.id", accCredit1.getId()));
        List<TransactionEntity> list = transactionDao.list(cr);
        assertThat(list.size()).isEqualTo(2);
        cr.add(Restrictions.eq("debitAccount.id", accDebit3.getId()));
        list = transactionDao.list(cr);
        assertThat(list.size()).isEqualTo(1);
    }
}
