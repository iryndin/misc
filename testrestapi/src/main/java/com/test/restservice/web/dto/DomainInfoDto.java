package com.test.restservice.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DomainInfoDto {
    private String domain;
    private String tld;
    private boolean available;
    private double price;
}
