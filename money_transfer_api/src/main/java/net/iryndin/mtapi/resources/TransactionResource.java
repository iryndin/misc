package net.iryndin.mtapi.resources;

import io.dropwizard.hibernate.UnitOfWork;
import net.iryndin.mtapi.api.ApiResponse;
import net.iryndin.mtapi.api.ApiResponseError;
import net.iryndin.mtapi.api.ApiResponseOK;
import net.iryndin.mtapi.api.TransferRequest;
import net.iryndin.mtapi.api.TransferResponse;
import net.iryndin.mtapi.api.TxResponse;
import net.iryndin.mtapi.core.AccountEntity;
import net.iryndin.mtapi.core.TransactionEntity;
import net.iryndin.mtapi.db.AccountDao;
import net.iryndin.mtapi.db.TransactionDao;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author iryndin
 * @since 11/04/17
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private AccountDao accountDao;
    private TransactionDao transactionDao;

    public TransactionResource(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    @GET
    @UnitOfWork
    @Path("/transfer/{txId}")
    public ApiResponse getTx(@PathParam("txId") Long txId) {
        Optional<TransactionEntity> txOpt = transactionDao.findById(txId);
        if (!txOpt.isPresent()) {
            return new ApiResponseError("No transaction with this ID", ApiResponseError.ERROR_NO_TX);
        } else {
            TransactionEntity tx = txOpt.get();
            return new ApiResponseOK<>(new TxResponse(tx.getId(),
                    tx.getCreditAccount().getId(), tx.getDebitAccount().getId(),
                    tx.getAmount(), tx.getCreateDate(),
                    tx.getDescription(), tx.getType()));
        }
    }

    @GET
    @Path("/transfers")
    public ApiResponse getTxs(
            @QueryParam("credit_account_id") Long creditAccountId,
            @QueryParam("debit_account_id") Long debitAccountId,
            @QueryParam("type") Integer type,
            @QueryParam("start_ts") Long startTs,
            @QueryParam("end_ts") Long endTs) {
        List<TxResponse> list = Arrays.asList(
                new TxResponse(11L, 22L, 33L, 7777L, new Date(), "example tx 1", 1),
                new TxResponse(111L, 222L, 333L, 8888L, new Date(), "example tx 2", 2),
                new TxResponse(1111L, 2222L, 3333L, 9999L, new Date(), "example tx 3", 3)
                );
        return new ApiResponseOK<>(list);
    }

    @PUT
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/transfer/{creditAccountId}/{debitAccountId}")
    public ApiResponse transfer(
            @PathParam("creditAccountId") Long creditAccountId,
            @PathParam("debitAccountId") Long debitAccountId,
            TransferRequest txData) {
        if (creditAccountId == debitAccountId) {
            return new ApiResponseError("Credit account ID should not be equal to debit account ID",
                    ApiResponseError.ERROR_WRONG_ACCOUNT_IDS);
        }

        Optional<AccountEntity> creditAccOpt = accountDao.findById(creditAccountId);
        if (!creditAccOpt.isPresent()) {
            return new ApiResponseError("Credit account ID wrong",
                    ApiResponseError.ERROR_NO_ACCOUNT);
        }

        Optional<AccountEntity> debitAccOpt = accountDao.findById(debitAccountId);
        if (!debitAccOpt.isPresent()) {
            return new ApiResponseError("Debit account ID wrong",
                    ApiResponseError.ERROR_NO_ACCOUNT);
        }

        if (txData.getAmount() <= 0) {
            return new ApiResponseError("Zero or negative transaction amount",
                    ApiResponseError.ERROR_WRONG_TX_AMOUNT);
        }

        if (creditAccOpt.get().getBalance() < txData.getAmount()) {
            return new ApiResponseError("Insufficient balance on credit account",
                    ApiResponseError.ERROR_INSUFFICIENT_BALANCE);
        }

        Date now = new Date();
        creditAccOpt.get().incrBalance(-txData.getAmount(), now);
        debitAccOpt.get().incrBalance(-txData.getAmount(), now);

        TransactionEntity tx = new TransactionEntity(null,
                txData.getAmount(), now, txData.getDescription(), txData.getType(),
                creditAccOpt.get(), debitAccOpt.get());

        accountDao.createOrUpdate(creditAccOpt.get());
        accountDao.createOrUpdate(debitAccOpt.get());
        tx = transactionDao.createOrUpdate(tx);

        return new ApiResponseOK<>(new TransferResponse(tx.getId()));
    }
}
