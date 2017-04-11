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
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    @UnitOfWork
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
                return new ApiResponseError("end_ts shoud be greater than start_ts", ApiResponseError.ERROR_INCORRECT_END_DATE);
            }
            crit.add(Restrictions.le("createDate", new Date(endTs)));
        }

        crit.setMaxResults(1000);
        crit.addOrder(Order.desc("createDate"));

        List<TransactionEntity> list = transactionDao.list(crit);

        return new ApiResponseOK<>(list.stream().map(txConverter).collect(Collectors.toList()));
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
