package com.example.demo.core;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface SecurityPersistentLoginsRepository extends CrudRepository<SecurityPersistentLogins, String> {

	@Modifying
	@Transactional
	int deleteByUsername(@Param("username") String username);
	
}
