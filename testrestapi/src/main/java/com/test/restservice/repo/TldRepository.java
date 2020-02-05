package com.test.restservice.repo;

import com.test.restservice.model.Tld;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TldRepository extends AppBaseRepository<Tld, Long> {
    List<Tld> findAllByOrderByPriceAsc();
}
