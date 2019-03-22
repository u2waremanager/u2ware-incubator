package com.skinfosec.dsp.report.es.batch.report;

import java.sql.Timestamp;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OzLocationRepository extends PagingAndSortingRepository< OzLocation, Long>{

	@Transactional
	public int deleteByReportDateBetween(Timestamp min, Timestamp max);


}
