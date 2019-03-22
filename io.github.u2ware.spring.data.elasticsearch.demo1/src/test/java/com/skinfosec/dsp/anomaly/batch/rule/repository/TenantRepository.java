package com.skinfosec.dsp.anomaly.batch.rule.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.skinfosec.dsp.anomaly.batch.rule.Tenant;

public interface TenantRepository extends PagingAndSortingRepository<Tenant, Long>{

}
