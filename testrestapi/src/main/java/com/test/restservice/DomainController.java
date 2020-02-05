package com.test.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DomainController {


    @GetMapping("/domains/check")
    public Object check(SearchDto searchDto) {
        // todo start point for implementation
        return null;
    }
}
