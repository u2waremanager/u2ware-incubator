package com.example.demo.schedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.example.demo.event.DateTimeRange;
import com.example.demo.event.DspBaseQueryBuilder;
import com.example.demo.event.DspBaseQueryService;
import com.example.demo.report.OzCountry;
import com.example.demo.report.OzCountryRepository;

@Component
public class TotalNumberOfEventsByCountrySchedule extends DateTimeRangeEventSchedule {
	
	@Autowired(required=false)
    private DspBaseQueryService service;
	
	@Autowired(required=false)
    private OzCountryRepository repository;
	
	public int batch(DateTimeRangeEvent range) {
		
		logger.info("=========================================================");
		logger.info(ClassUtils.getShortName(getClass())+" Started.");
		SearchQuery query = new DspBaseQueryBuilder()
				.queryRange("eventEndTime", range.getDateTimeRange())
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationTerms("sourceCountrycode")
				.aggregationRange("eventEndTime", DateTimeRange.everyHourOfDay(range.getFromDateTime()))
				.build();
		
		final List<OzCountry> entities = new ArrayList<>();
		service.query(query, aggregations -> {
			OzCountry e = new OzCountry();
			e.setDomainId(aggregations.getKeyAsLong("domainId"));
			e.setAssetsId(aggregations.getKeyAsLong("assetsId"));
			e.setSourceCountryCode(aggregations.getKeyAsString("sourceCountrycode"));

			e.setSourceCountryCodeSum(aggregations.getDocCount("eventEndTime"));

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
