package com.skinfosec.dsp.anomaly.batch.rule.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.skinfosec.dsp.anomaly.batch.rule.Dmn;

public interface DmnRepository extends PagingAndSortingRepository<Dmn, Long>{

}
