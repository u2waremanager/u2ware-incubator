package com.skinfosec.dsp.report.es.batch.schedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.skinfosec.dsp.report.es.batch.event.DateTimeRange;
import com.skinfosec.dsp.report.es.batch.event.DspBaseQueryBuilder;
import com.skinfosec.dsp.report.es.batch.event.DspBaseQueryService;
import com.skinfosec.dsp.report.es.batch.report.OzBase;
import com.skinfosec.dsp.report.es.batch.report.OzBaseRepository;

@Component
public class TotalNumberOfEventsBaseSchedule extends DateTimeRangeEventSchedule {
	
	@Autowired(required=false)
    private DspBaseQueryService service;
	
	@Autowired(required=false)
    private OzBaseRepository repository;
	
	public int batch(DateTimeRangeEvent range) {
		
		logger.info("=========================================================");
		logger.info(ClassUtils.getClassFileName(getClass())+" Started.");
		SearchQuery query = new DspBaseQueryBuilder()
				.queryRange("eventEndTime", range.getDateTimeRange())
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationRange("eventEndTime", DateTimeRange.everyHourOfDay(range.getFromDateTime()))
				.build();
		
		final List<OzBase> entities = new ArrayList<>();
		service.query(query, aggregations -> {
			OzBase e = new OzBase();
			e.setDomainId(aggregations.getKeyAsLong("domainId"));
			e.setAssetsId(aggregations.getKeyAsLong("assetsId"));

			e.setBaseSum(aggregations.getDocCount("eventEndTime"));

			e.setReportDate(aggregations.getFromAsTimestamp("eventEndTime"));
			e.setRegDate(new Timestamp(System.currentTimeMillis()));
			entities.add(e);
		});

		repository.deleteByReportDateBetween(new Timestamp(range.getFromMillis()-1), new Timestamp(range.getToMillis()+1));
		repository.saveAll(entities);
		logger.info(ClassUtils.getClassFileName(getClass())+" Finished. "+entities.size()+" row(s) saved. "+range+"\n");
		
		return entities.size();
	}	
	
}
