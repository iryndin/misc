package net.iryndin.mtapi.resources;

/**
 * @author iryndin
 * @since 12/04/17
 */

import io.dropwizard.testing.junit.ResourceTestRule;
import net.iryndin.mtapi.api.ApiResponse;
import net.iryndin.mtapi.api.ApiResponseError;
import net.iryndin.mtapi.api.ApiResponseOK;
import net.iryndin.mtapi.api.TxResponse;
import net.iryndin.mtapi.core.AccountEntity;
import net.iryndin.mtapi.core.TransactionEntity;
import net.iryndin.mtapi.db.AccountDao;
import net.iryndin.mtapi.db.TransactionDao;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.GenericType;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionResourceTest {

    private static final AccountDao accountDao = mock(AccountDao.class);
    private static final TransactionDao transactionDao = mock(TransactionDao.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addResource(new TransactionResource(accountDao, transactionDao))
            .build();

    private AccountEntity accountCredit;
    private AccountEntity accountDebit;

    @Before
    public void setUp() {
        accountCredit = new AccountEntity(1L, 40, new Date(), new Date(), 1);
        accountDebit = new AccountEntity(2L, 30, new Date(), new Date(), 1);
    }

    @After
    public void tearDown() {
        reset(accountDao);
    }

    @Test
    public void testGetTx() {
        final long id = 10;
        TransactionEntity tx = new TransactionEntity(id, 11, new Date(),
                "description 1", 1, accountCredit, accountDebit);
        when(transactionDao.findById(id)).thenReturn(Optional.of(tx));
        when(transactionDao.findById(33L)).thenReturn(Optional.empty());
        ApiResponse response = RESOURCES.target("/transfer/10").request()
                .get(new GenericType<ApiResponseOK<TxResponse>>() {});
        assertThat(response instanceof ApiResponseOK).isTrue();
        ApiResponseOK<TxResponse> respOk = (ApiResponseOK)response;

        assertThat(respOk.getData().getId()).isEqualTo(tx.getId());
        assertThat(respOk.getData().getAmount()).isEqualTo(tx.getAmount());
        assertThat(respOk.getData().getCreateDate()).isEqualTo(tx.getCreateDate());
        assertThat(respOk.getData().getDescription()).isEqualTo(tx.getDescription());
        assertThat(respOk.getData().getType()).isEqualTo(tx.getType());
        assertThat(respOk.getData().getCreditAccountId()).isEqualTo(tx.getCreditAccount().getId());
        assertThat(respOk.getData().getDebitAccountId()).isEqualTo(tx.getDebitAccount().getId());

        ApiResponse response2 = RESOURCES.target("/transfer/33").request()
                .get(new GenericType<ApiResponseError>() {});
        assertThat(response2 instanceof ApiResponseError).isTrue();
        ApiResponseError respError = (ApiResponseError)response2;
        assertThat(respError.getStatus()).isEqualTo(ApiResponse.STATUS_FAIL);
        assertThat(respError.getErrorMessage()).isNotBlank();
        assertThat(respError.getErrorCode()).isEqualTo(ApiResponseError.ERROR_NO_TX);
    }

    // some more tests to follow....
}
