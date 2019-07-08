package com.example.demo.sign;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface AuthoritiesRepository  extends PagingAndSortingRepository<Authorities, Authorities.ID> {

}
