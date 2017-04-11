package net.iryndin.mtapi.api;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class ApiResponseError extends ApiResponse {
    private String errorMessage;
    private Integer errorCode;

    public ApiResponseError() {
    }

    public ApiResponseError(String errorMessage, Integer errorCode) {
        super(STATUS_FAIL);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
