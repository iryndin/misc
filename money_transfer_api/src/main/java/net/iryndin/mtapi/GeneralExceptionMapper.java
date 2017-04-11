package net.iryndin.mtapi;

import net.iryndin.mtapi.api.ApiResponseError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Catch all thrown exceptions and convert them to ApiResponseError
 * @author iryndin
 * @since 11/04/17
 */
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiResponseError("Internal server error: " + e.getMessage(), ApiResponseError.ERROR_GENERAL))
                .build();
    }
}
