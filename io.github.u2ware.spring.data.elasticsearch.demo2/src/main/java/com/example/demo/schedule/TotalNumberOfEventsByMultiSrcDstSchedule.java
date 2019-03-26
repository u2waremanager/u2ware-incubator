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
import com.example.demo.report.OzMultiSrcDst;
import com.example.demo.report.OzMultiSrcDstRepository;

@Component
public class TotalNumberOfEventsByMultiSrcDstSchedule extends DateTimeRangeEventSchedule {
	
	@Autowired(required=false)
    private DspBaseQueryService service;
	
	@Autowired(required=false)
    private OzMultiSrcDstRepository repository;
	
	public int batch(DateTimeRangeEvent range) {
		
		logger.info("=========================================================");
		logger.info(ClassUtils.getShortName(getClass())+" Started.");
		SearchQuery query = new DspBaseQueryBuilder()
				.queryRange("eventEndTime", range.getDateTimeRange())
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationTerms("sourceAddress")
				.aggregationTerms("eventReason")
				.aggregationTerms("destinationCountrycode")
				.aggregationTerms("destinationAddress")
				.aggregationRange("eventEndTime", DateTimeRange.everyHourOfDay(range.getFromDateTime()))
				.build();

		final List<OzMultiSrcDst> entities = new ArrayList<>();
		service.query(query, aggregations -> {
			OzMultiSrcDst e = new OzMultiSrcDst();
			e.setDomainId(aggregations.getKeyAsLong("domainId"));
			e.setAssetsId(aggregations.getKeyAsLong("assetsId"));
			e.setSourceAddress(aggregations.getKeyAsString("sourceAddress"));
			e.setEventReason(aggregations.getKeyAsString("eventReason"));
			e.setDestinationCountryCode(aggregations.getKeyAsString("destinationCountrycode"));
			e.setDestinationAddress(aggregations.getKeyAsString("destinationAddress"));
			
			e.setDestinationAddressSum(aggregations.getDocCount("eventEndTime"));
			
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
