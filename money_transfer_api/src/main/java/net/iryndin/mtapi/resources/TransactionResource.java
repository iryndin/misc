package net.iryndin.mtapi.resources;

import net.iryndin.mtapi.api.ApiResponse;
import net.iryndin.mtapi.api.ApiResponseOK;
import net.iryndin.mtapi.api.TransferRequest;
import net.iryndin.mtapi.api.TransferResponse;
import net.iryndin.mtapi.api.TxResponse;

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

/**
 * @author iryndin
 * @since 11/04/17
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @GET
    @Path("/transfer/{txId}")
    public ApiResponse getTx(@PathParam("txId") Long txId) {
        return new ApiResponseOK<>(new TxResponse(
                11L, 22L, 33L, 7777L, new Date(), "example tx", 1
        ));
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/transfer/{creditAccountId}/{debitAccountId}")
    public ApiResponse transfer(
            @PathParam("creditAccountId") Long creditAccountId,
            @PathParam("debitAccountId") Long debitAccountId,
            TransferRequest txData) {
        System.out.println(txData);
        return new ApiResponseOK<>(new TransferResponse(11L));
    }
}
