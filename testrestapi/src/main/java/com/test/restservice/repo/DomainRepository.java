package com.test.restservice.repo;

import com.test.restservice.model.Domain;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomainRepository extends AppBaseRepository<Domain, Long> {
    List<Domain> findByName(String name);
}
