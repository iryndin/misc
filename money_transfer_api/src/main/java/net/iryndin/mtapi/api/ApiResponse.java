package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class ApiResponse {
    public static String STATUS_OK = "OK";
    public static String STATUS_FAIL = "FAIL";

    private String status;

    public ApiResponse() {
    }

    public ApiResponse(String status) {
        this.status = status;
    }

    @JsonProperty
    public String getStatus() {
        return status;
    }
}
