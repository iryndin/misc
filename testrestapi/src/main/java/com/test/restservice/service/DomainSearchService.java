package com.test.restservice.service;

import com.test.restservice.repo.DomainRepository;
import com.test.restservice.web.dto.DomainInfoDto;
import com.test.restservice.model.Domain;
import com.test.restservice.model.Tld;
import com.test.restservice.repo.TldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DomainSearchService {

    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private TldRepository tldRepository;

    @Transactional(readOnly = true)
    public List<DomainInfoDto> search(String domainName) {
        String domain = normalize(domainName);
        Map<String, Domain> domains = domainRepository.findByName(domain)
                .stream()
                .collect(Collectors.toMap(Domain::getTld, Function.identity()));
        List<Tld> tlds = tldRepository.findAllByOrderByPriceAsc();

        List<DomainInfoDto> result = new ArrayList<>(tlds.size());

        for (Tld tld : tlds) {
            boolean available = !domains.containsKey(tld.getName());
            DomainInfoDto di = DomainInfoDto.builder()
                    .domain(domain + "." + tld.getName())
                    .tld(tld.getName())
                    .available(available)
                    .price(tld.getPrice())
                    .build();
            result.add(di);
        }
        return result;
    }

    private String normalize(String domainName) {
        return domainName.toLowerCase();
    }
}
