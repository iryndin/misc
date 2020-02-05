package com.test.restservice.web.error;

public class BaseErrorDto {

    private String error;

    public BaseErrorDto() {
    }

    public BaseErrorDto(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
