package com.skinfosec.dsp.anomaly.batch.rule.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.skinfosec.dsp.anomaly.batch.rule.Rule;

public interface RuleRepository extends PagingAndSortingRepository<Rule, Long>{

 	
	
}
