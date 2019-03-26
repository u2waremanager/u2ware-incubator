package com.example.demo.event;

import static com.example.demo.event.DateTimeRange.continuousHourOfDay;
import static com.example.demo.event.DateTimeRange.edgeHourOfDay;
import static com.example.demo.event.DateTimeRange.everyHourOfDay;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.example.demo.ApplicationTests;
import com.example.demo.event.DspBaseQueryBuilder;
import com.example.demo.event.DspBaseQueryService;

public class DspBaseQueryServiceTest extends ApplicationTests{

	private @Autowired DspBaseQueryService operations;
	
	@Test
	public void contextLoads() throws Exception {

		//////////////////////////////////////
		// Handler 
		///////////////////////////////////////
		logger.info("#############################################");
		SearchQuery q11 = new DspBaseQueryBuilder()
				.aggregationTerms("domainId").build();
		operations.query(q11);
		operations.query(q11,  buckets -> { logger.info(buckets);});
		
		
		logger.info("#############################################");
		SearchQuery q12 = new DspBaseQueryBuilder()
				.queryRange("eventStartTime", edgeHourOfDay(parse("2019-02-21")))
				.aggregationTerms("domainId")
				.build();
		operations.query(q12,  buckets -> { logger.info(buckets);});
		
		//////////////////////////////////////////////////////////////////////
		// queryFormattedRange VS queryRange , eventStartTime VS eventEndTime
		/////////////////////////////////////////////////////////////////////
		DateTime oneday = DateTime.parse("2019-02-01", DateTimeFormat.forPattern("yyyy-MM-dd"));
		for(int i=0 ; i < 30; i++) {
			logger.info("#############################################");
			logger.info(oneday);
			
			SearchQuery q21 = new DspBaseQueryBuilder().queryFormattedRange("eventStartTime", edgeHourOfDay(oneday))
					.aggregationTerms("domainId").aggregationTerms("assetsId").aggregationTerms("eventReason").build();
			operations.query(q21);  //Not Working

			SearchQuery q22 = new DspBaseQueryBuilder().queryRange("eventStartTime", edgeHourOfDay(oneday))
					.aggregationTerms("domainId").aggregationTerms("assetsId").aggregationTerms("eventReason").build();
			operations.query(q22);  // Working

			
			SearchQuery q23 = new DspBaseQueryBuilder().queryFormattedRange("eventEndTime", edgeHourOfDay(oneday))
					.aggregationTerms("domainId").aggregationTerms("assetsId").aggregationTerms("eventReason").build();
			operations.query(q23);  // Working. Same Result

			SearchQuery q24 = new DspBaseQueryBuilder().queryRange("eventEndTime", edgeHourOfDay(oneday))
					.aggregationTerms("domainId").aggregationTerms("assetsId").aggregationTerms("eventReason").build();
			operations.query(q24);  // Working. Same Result
			
			oneday = oneday.plusDays(1);
		}
		
		//////////////////////////////////////
		// Base
		//////////////////////////////////////
		logger.info("#############################################");
		SearchQuery q31 = new DspBaseQueryBuilder()
				.aggregationTerms("domainId").aggregationTerms("assetsId")
				.aggregationRange("eventStartTime", continuousHourOfDay(parse("2019-02-21"))).build();
		operations.query(q31);  //Not Working

		SearchQuery q32 = new DspBaseQueryBuilder()
				.aggregationTerms("domainId").aggregationTerms("assetsId")
				.aggregationRange("eventEndTime", continuousHourOfDay(parse("2019-02-21"))).build();
		operations.query(q32);  //Working
		
		
		SearchQuery q33 = new DspBaseQueryBuilder().queryRange("eventStartTime", edgeHourOfDay(parse("2019-02-21")))
				.aggregationTerms("domainId").aggregationTerms("assetsId")
				.aggregationRange("eventStartTime", continuousHourOfDay(parse("2019-02-21"))).build();
		operations.query(q33);  //Not Working

		SearchQuery q34 = new DspBaseQueryBuilder().queryRange("eventEndTime", edgeHourOfDay(parse("2019-02-21")))
				.aggregationTerms("domainId").aggregationTerms("assetsId")
				.aggregationRange("eventEndTime", continuousHourOfDay(parse("2019-02-21"))).build();
		operations.query(q34);  //Working
		

		//////////////////////////////////////
		// eventReason
		//////////////////////////////////////
		SearchQuery q41 = new DspBaseQueryBuilder()
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationTerms("eventReason")
				.build();
		operations.query(q41);  //

		SearchQuery q42 = new DspBaseQueryBuilder()
				.queryRange("eventEndTime", edgeHourOfDay(parse("2019-02-21")))
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationTerms("eventReason")
				.build();
		operations.query(q42);  //
		

		SearchQuery q43 = new DspBaseQueryBuilder()
				.queryRange("eventEndTime", edgeHourOfDay(parse("2019-02-21")))
				.aggregationRange("eventEndTime", everyHourOfDay(parse("2019-02-21")))
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationTerms("eventReason")
				.build();
		operations.query(q43);  //
		
		SearchQuery q45 = new DspBaseQueryBuilder()
				.queryRange("eventEndTime", edgeHourOfDay(parse("2019-02-21")))
				.aggregationTerms("domainId")
				.aggregationTerms("assetsId")
				.aggregationTerms("eventReason")
				.aggregationRange("eventEndTime", everyHourOfDay(parse("2019-02-21")))
				.build();
		operations.query(q45);  //
		
	}
}