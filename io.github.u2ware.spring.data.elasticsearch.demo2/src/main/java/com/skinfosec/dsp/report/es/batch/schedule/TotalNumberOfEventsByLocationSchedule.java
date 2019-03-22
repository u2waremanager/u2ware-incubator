package com.skinfosec.dsp.report.es.batch.schedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.skinfosec.dsp.report.es.batch.event.DspBaseQueryService;
import com.skinfosec.dsp.report.es.batch.report.OzLocation;
import com.skinfosec.dsp.report.es.batch.report.OzLocationRepository;
import com.skinfosec.dsp.report.es.batch.event.DateTimeRange;
import com.skinfosec.dsp.report.es.batch.event.DspBaseQueryBuilder;

@Component
public class TotalNumberOfEventsByLocationSchedule extends DateTimeRangeEventSchedule {
	
	@Autowired(required=false)
    private DspBaseQueryService service;
	
	@Autowired(required=false)
    private OzLocationRepository repository;
	
	public int batch(DateTimeRangeEvent range) {
		
		logger.info("=========================================================");
		logger.info(ClassUtils.getShortName(getClass())+" Started.");
		SearchQuery query = new DspBaseQueryBuilder()
				.queryRange("eventEndTime", range.getDateTimeRange())
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationTerms("deviceLocationMap")
				.aggregationRange("eventEndTime", DateTimeRange.everyHourOfDay(range.getFromDateTime()))
				.build();
		
		final List<OzLocation> entities = new ArrayList<>();
		service.query(query, aggregations -> {
			OzLocation e = new OzLocation();
			e.setDomainId(aggregations.getKeyAsLong("domainId"));
			e.setAssetsId(aggregations.getKeyAsLong("assetsId"));
			e.setDeviceLocationMap(aggregations.getKeyAsString("deviceLocationMap"));
			
			e.setDeviceLocationMapSum(aggregations.getDocCount("eventEndTime"));

			e.setReportDate(aggregations.getFromAsTimestamp("eventEndTime"));
			e.setRegDate(new Timestamp(System.currentTimeMillis()));
			entities.add(e);
		});

		repository.deleteByReportDateBetween(new Timestamp(range.getFromMillis()-1), new Timestamp(range.getToMillis()+1));
		repository.saveAll(entities);
		logger.info(ClassUtils.getShortName(getClass())+" Finished. "+entities.size()+" row(s) saved. "+range+"\n");
		
		return entities.size();
	}	
}
