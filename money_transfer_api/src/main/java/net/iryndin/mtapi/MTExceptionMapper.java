package net.iryndin.mtapi;

import net.iryndin.mtapi.api.ApiResponseError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author iryndin
 * @since 17/04/17
 */
public class MTExceptionMapper implements ExceptionMapper<MTException> {

    @Override
    public Response toResponse(MTException e) {
        return Response.status(Response.Status.OK)
                .entity(new ApiResponseError(e.getMessage(), e.getErrorCode()))
                .build();
    }
}
