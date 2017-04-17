package net.iryndin.mtapi.resources;

import io.dropwizard.hibernate.UnitOfWork;
import net.iryndin.mtapi.MTException;
import net.iryndin.mtapi.api.ApiResponse;
import net.iryndin.mtapi.api.ApiResponseError;
import net.iryndin.mtapi.api.ApiResponseOK;
import net.iryndin.mtapi.api.BalanceResponse;
import net.iryndin.mtapi.core.AccountEntity;
import net.iryndin.mtapi.db.AccountDao;
import org.hibernate.CacheMode;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author iryndin
 * @since 11/04/17
 */
@Path("/balance/{accountId}")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private AccountDao accountDao;

    public AccountResource(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.IGNORE)
    public ApiResponse getBalance(@PathParam("accountId") long accountId) {
        AccountEntity acc = accountDao.findById(accountId)
                .orElseThrow(() -> new MTException("No account with this ID", ApiResponseError.ERROR_NO_ACCOUNT));
        return new ApiResponseOK<>(new BalanceResponse(acc.getBalance()));
    }
}