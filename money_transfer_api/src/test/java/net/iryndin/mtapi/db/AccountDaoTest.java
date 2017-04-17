package net.iryndin.mtapi.db;

import com.google.common.collect.Lists;
import io.dropwizard.testing.junit.DAOTestRule;
import net.iryndin.mtapi.core.AccountEntity;
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
public class AccountDaoTest {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(AccountEntity.class)
            .build();

    private AccountDao accountDao;

    @Before
    public void setUp() throws Exception {
        accountDao = new AccountDao(daoTestRule.getSessionFactory());
    }

    @Test
    public void testCreateOrUpdate() {
        final AccountEntity acc = daoTestRule.inTransaction(() ->
                accountDao.createOrUpdate(new AccountEntity(null, 11, new Date(), new Date(), 1)));
        assertThat(acc.getId()).isGreaterThan(0);
        assertThat(acc.getBalance()).isEqualTo(11);
        assertThat(acc.getCreateDate()).isNotNull();
        assertThat(acc.getUpdateDate()).isNotNull();
    }

    @Test
    public void testFindById() {
        final List<Long> accIds = Lists.newArrayList();
        daoTestRule.inTransaction(() -> {
            accIds.add(accountDao.createOrUpdate(new AccountEntity(null, 11, new Date(), new Date(), 1)).getId());
            accIds.add(accountDao.createOrUpdate(new AccountEntity(null, 22, new Date(), new Date(), 1)).getId());
            accIds.add(accountDao.createOrUpdate(new AccountEntity(null, 33, new Date(), new Date(), 1)).getId());
        });

        assertThat(accountDao.findById(accIds.get(0)).isPresent()).isTrue();
        assertThat(accountDao.findById(accIds.get(1)).isPresent()).isTrue();
        assertThat(accountDao.findById(accIds.get(2)).isPresent()).isTrue();
        assertThat(accountDao.findById(777L).isPresent()).isFalse();
    }
}
