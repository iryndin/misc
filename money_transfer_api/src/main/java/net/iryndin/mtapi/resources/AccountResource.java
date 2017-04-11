package net.iryndin.mtapi.resources;

import net.iryndin.mtapi.api.ApiResponse;
import net.iryndin.mtapi.api.ApiResponseError;
import net.iryndin.mtapi.api.ApiResponseOK;
import net.iryndin.mtapi.api.BalanceResponse;

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

    @GET
    public ApiResponse getBalance(@PathParam("accountId") int accountId) {
        //return new ApiResponseOK<>(new BalanceResponse(222));
        return new ApiResponseError("bzzzz",accountId);
    }
}
