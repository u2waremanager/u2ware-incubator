package com.example.demo.report;

import java.sql.Timestamp;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OzBaseRepository extends PagingAndSortingRepository<OzBase, Long>{

	@Transactional
	public int deleteByReportDateBetween(Timestamp min, Timestamp max);


}
