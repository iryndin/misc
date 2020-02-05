package com.test.restservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AppBaseRepository<T, ID> extends CrudRepository<T, ID>, JpaRepository<T, ID> {
}
