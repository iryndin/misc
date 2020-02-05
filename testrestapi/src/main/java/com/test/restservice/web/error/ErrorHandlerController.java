package com.test.restservice.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(InvalidDomainNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseErrorDto handleInvalidDomainNameException(InvalidDomainNameException ex) {
        return new BaseErrorDto("Invalid domain name: " + ex.getSearchDDto().getSearch());
    }
}
