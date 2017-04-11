package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class ApiResponseError extends ApiResponse {
    public static final int ERROR_NO_ACCOUNT = 111;
    public static final int ERROR_NO_TX = 112;
    public static final int ERROR_WRONG_ACCOUNT_IDS = 113;
    public static final int ERROR_WRONG_TX_AMOUNT = 114;
    public static final int ERROR_INSUFFICIENT_BALANCE = 115;
    public static final int ERROR_GENERAL = 500;

    private String errorMessage;
    private Integer errorCode;

    public ApiResponseError() {
    }

    public ApiResponseError(String errorMessage, Integer errorCode) {
        super(STATUS_FAIL);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    @JsonProperty
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty
    public Integer getErrorCode() {
        return errorCode;
    }
}
