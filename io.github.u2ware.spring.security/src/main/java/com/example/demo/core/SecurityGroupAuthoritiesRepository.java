package com.example.demo.core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface SecurityGroupAuthoritiesRepository extends CrudRepository<SecurityGroupAuthorities, Long> {

    
	
}
