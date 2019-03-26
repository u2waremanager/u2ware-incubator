package com.example.demo.rule.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.rule.Tenant;

public interface TenantRepository extends PagingAndSortingRepository<Tenant, Long>{

}
