package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class ApiResponseOK<T> extends ApiResponse {
    private T data;

    public ApiResponseOK() {
    }

    public ApiResponseOK(T data) {
        super(STATUS_OK);
        this.data = data;
    }

    @JsonProperty
    public T getData() {
        return data;
    }
}
