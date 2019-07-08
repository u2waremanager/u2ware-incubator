package com.example.demo.sign;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface UsersRepository  extends PagingAndSortingRepository<Users, UUID>, JpaSpecificationExecutor<Users> {

}
