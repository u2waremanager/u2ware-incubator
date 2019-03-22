package com.skinfosec.dsp.report.es.batch.report;

import java.sql.Timestamp;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OzReasonRepository extends PagingAndSortingRepository<OzReason, Long>{


	@Transactional
	public int deleteByReportDateBetween(Timestamp min, Timestamp max);

}
