package com.test.restservice.web;

import com.test.restservice.service.DomainSearchService;
import com.test.restservice.web.dto.DomainInfoDto;
import com.test.restservice.web.dto.SearchDto;
import com.test.restservice.web.error.InvalidDomainNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DomainController {

    @Autowired
    private DomainSearchService domainSearchService;

    @GetMapping("/domains/check")
    public List<DomainInfoDto> check(@Valid SearchDto search, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidDomainNameException(search);
        }
        return domainSearchService.search(search.getSearch());
    }
}
