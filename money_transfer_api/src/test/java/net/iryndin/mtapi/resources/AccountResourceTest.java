package net.iryndin.mtapi.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import net.iryndin.mtapi.api.ApiResponse;
import net.iryndin.mtapi.api.ApiResponseOK;
import net.iryndin.mtapi.api.BalanceResponse;
import net.iryndin.mtapi.core.AccountEntity;
import net.iryndin.mtapi.db.AccountDao;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.GenericType;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author iryndin
 * @since 11/04/17
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountResourceTest {

    private static final AccountDao accountDao = mock(AccountDao.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new AccountResource(accountDao))
            .build();

    private AccountEntity account;

    @Before
    public void setUp() {
        account = new AccountEntity(11L, 22L, new Date(), new Date(), 1);
    }

    @After
    public void tearDown() {
        reset(accountDao);
    }

    @Test
    public void testGetBalance() throws Exception {
        when(accountDao.findById(Mockito.eq(11L))).thenReturn(Optional.of(account));

        ApiResponse response = RESOURCES.target("/balance/11").request()
                .get(new GenericType<ApiResponseOK<BalanceResponse>>() {});

        verify(accountDao).findById(Mockito.eq(11L));
        assertThat(response instanceof ApiResponseOK).isTrue();
        ApiResponseOK<BalanceResponse> responseOk = (ApiResponseOK)response;
        assertThat(responseOk.getData().getBalance()).isEqualTo(account.getBalance());
    }
}
