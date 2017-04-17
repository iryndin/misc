package net.iryndin.mtapi.resources;

import io.dropwizard.hibernate.UnitOfWork;
import net.iryndin.mtapi.MTException;
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
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author iryndin
 * @since 11/04/17
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private AccountDao accountDao;
    private TransactionDao transactionDao;

    static final Function<TransactionEntity, TxResponse> txConverter = tx -> new TxResponse(
            tx.getId(), tx.getCreditAccount().getId(), tx.getDebitAccount().getId(),
            tx.getAmount(), tx.getCreateDate(), tx.getDescription(), tx.getType()
    );

    public TransactionResource(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.IGNORE)
    @Path("/transfer/{txId}")
    public ApiResponse getTx(@PathParam("txId") Long txId) {
        TransactionEntity tx = transactionDao.findById(txId)
                .orElseThrow(() -> new MTException("No transaction with this ID", ApiResponseError.ERROR_NO_TX));
        return new ApiResponseOK<>(new TxResponse(tx.getId(),
                    tx.getCreditAccount().getId(), tx.getDebitAccount().getId(),
                    tx.getAmount(), tx.getCreateDate(),
                    tx.getDescription(), tx.getType()));
    }

    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.IGNORE)
    @Path("/transfers")
    public ApiResponse getTxs(
            @QueryParam("credit_account_id") Long creditAccountId,
            @QueryParam("debit_account_id") Long debitAccountId,
            @QueryParam("type") Integer type,
            @QueryParam("start_ts") Long startTs,
            @QueryParam("end_ts") Long endTs) {

        Criteria crit = transactionDao.createCriteria();
        if (creditAccountId != null) {
            crit.add(Restrictions.eq("creditAccount.id", creditAccountId));
        }
        if (debitAccountId != null) {
            crit.add(Restrictions.eq("debitAccount.id", debitAccountId));
        }
        if (type != null) {
            crit.add(Restrictions.eq("type", type));
        }
        if (startTs != null) {
            crit.add(Restrictions.ge("createDate", new Date(startTs)));
        }
        if (endTs != null) {
            if (startTs != null && endTs <= startTs) {
                return new ApiResponseError("end_ts should be greater than start_ts", ApiResponseError.ERROR_INCORRECT_END_DATE);
            }
            crit.add(Restrictions.le("createDate", new Date(endTs)));
        }

        crit.setMaxResults(1000);
        crit.addOrder(Order.desc("createDate"));

        List<TransactionEntity> list = transactionDao.list(crit);

        return new ApiResponseOK<>(list.stream().map(txConverter).collect(Collectors.toList()));
    }

    @POST
    @UnitOfWork(cacheMode = CacheMode.IGNORE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/transfer/{creditAccountId}/{debitAccountId}")
    public ApiResponse transfer(
            @PathParam("creditAccountId") Long creditAccountId,
            @PathParam("debitAccountId") Long debitAccountId,
            TransferRequest txData) {
        if (creditAccountId.equals(debitAccountId)) {
            return new ApiResponseError("Credit account ID should not be equal to debit account ID",
                    ApiResponseError.ERROR_WRONG_ACCOUNT_IDS);
        }

        AccountEntity creditAcc = accountDao.findById(creditAccountId)
                .orElseThrow(() -> new MTException("Credit account ID wrong", ApiResponseError.ERROR_NO_ACCOUNT));

        AccountEntity debitAcc = accountDao.findById(debitAccountId)
                .orElseThrow(() -> new MTException("Debit account ID wrong", ApiResponseError.ERROR_NO_ACCOUNT));

        if (txData.getAmount() <= 0) {
            return new ApiResponseError("Zero or negative transaction amount",
                    ApiResponseError.ERROR_WRONG_TX_AMOUNT);
        }

        if (creditAcc.getBalance() < txData.getAmount()) {
            return new ApiResponseError("Insufficient balance on credit account",
                    ApiResponseError.ERROR_INSUFFICIENT_BALANCE);
        }

        Date now = new Date();
        creditAcc.incrBalance(-txData.getAmount(), now);
        debitAcc.incrBalance(-txData.getAmount(), now);

        TransactionEntity tx = new TransactionEntity(null,
                txData.getAmount(), now, txData.getDescription(), txData.getType(),
                creditAcc, debitAcc);

        accountDao.createOrUpdate(creditAcc);
        accountDao.createOrUpdate(debitAcc);
        tx = transactionDao.createOrUpdate(tx);

        return new ApiResponseOK<>(new TransferResponse(tx.getId()));
    }
}
