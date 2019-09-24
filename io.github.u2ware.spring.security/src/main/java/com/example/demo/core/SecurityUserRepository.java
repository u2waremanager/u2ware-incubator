package com.example.demo.core;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

//@RepositoryRestResource(exported=false)
public interface SecurityUserRepository 
extends PagingAndSortingRepository<SecurityUser, UUID>, JpaSpecificationExecutor<SecurityUser> {

	SecurityUser findByUsername(@Param("username") String username);
}
