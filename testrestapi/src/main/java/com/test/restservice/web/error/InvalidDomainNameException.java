package com.test.restservice.web.error;

import com.test.restservice.web.dto.SearchDto;

public class InvalidDomainNameException extends BaseAppException {

    private final SearchDto searchDDto;

    public InvalidDomainNameException(SearchDto searchDDto) {
        this.searchDDto = searchDDto;
    }

    public SearchDto getSearchDDto() {
        return searchDDto;
    }
}
