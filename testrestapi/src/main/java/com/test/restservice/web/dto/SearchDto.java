package com.test.restservice.web.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class SearchDto {

    /**
     * Validate domain name.
     * Regexp taken from here: https://stackoverflow.com/a/30007882
     */
    @Pattern(regexp = "[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String search;
}
