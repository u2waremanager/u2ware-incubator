package com.example.demo.event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.example.demo.ApplicationTests;
import com.example.demo.event.DspBase;
import com.example.demo.event.DspBaseQueryBuilder;
import com.example.demo.event.TimeSeriesEventQueryBuilder;
import com.example.demo.rule.AnomRuleDmn;
import com.example.demo.rule.AnomRuleDmnRepository;

public class TimeSeriesEventTest extends ApplicationTests{

	@Autowired
    private AnomRuleDmnRepository anomRuleDmnRepository;

	@Autowired
    private ElasticsearchOperations operations;

	@Test
	public void contextLoads() throws Exception {
		
		List<IndexQuery> quries = new ArrayList<>();
		
		Long minTime = null;
		Long maxTime = null;
//		Long minTime = 1549617356909l;
//		Long maxTime = 1549617457000l;
		logger.info("=========================================================");
		logger.info("minTime="+minTime+", maxTime="+maxTime);
		logger.info("\n");

		Iterable<AnomRuleDmn> rules = anomRuleDmnRepository.findAll();
		for(AnomRuleDmn rule : rules) {

			logger.info("\n");
			logger.info(rule);

			///////////////////////////////////////
			//
			///////////////////////////////////////
			Long tenantId = rule.getId().getTenant().getTenantNo();
			Long domainId = rule.getId().getDmn().getDmnNo();
			String targetFieldName = rule.getId().getRule().getTargetFieldName();
			String targetFieldOprtrCode = rule.getId().getRule().getTargetFieldOprtrCode();
			String anomRuleGroupFieldName = rule.getId().getRule().getFields().iterator().next().getAnomRuleGroupFieldName();
			
			SearchQuery query = new DspBaseQueryBuilder()
					.tenantId(tenantId)
					.domainId(domainId)
					.targetFieldName(targetFieldName)
					.targetFieldOprtrCode(targetFieldOprtrCode)
					.anomRuleGroupFieldName(anomRuleGroupFieldName)
					.minTime(minTime)
					.maxTime(maxTime)
					.build();
			Long count = operations.count(query, DspBase.class);
			logger.info("Count DspBase: "+count+" row(s)");
			
			if(count > 0 ) {
				
				query.setPageable(PageRequest.of(0, count.intValue()));
				
				AggregatedPage<DspBase> queryResult =  (AggregatedPage<DspBase>)operations.queryForPage(query, DspBase.class);
				logger.info("Search DspBase: "+queryResult.getTotalElements()+" row(s)");
				
				///////////////////////////////////////
				//
				///////////////////////////////////////
				String tenantName = rule.getId().getTenant().getTenantName();
				String domainName = rule.getId().getDmn().getDmnName();
				List<Object> baseEventIds = queryResult.stream().map(s -> s.getEventId()).collect(Collectors.toList());
				Aggregations aggregations = queryResult.getAggregations();
				
				List<IndexQuery> indexQuries = new TimeSeriesEventQueryBuilder()
						.tenantId(tenantId)
						.tenantName(tenantName)
						.domainId(domainId)
						.domainName(domainName)
						.targetFieldName(targetFieldName)
						.targetFieldOprtrCode(targetFieldOprtrCode)
						.anomRuleGroupFieldName(anomRuleGroupFieldName)
						.minTime(minTime)
						.baseEventIds(baseEventIds)
						.aggregations(aggregations)
						.build();
				for(IndexQuery indexQuery : indexQuries) {
					logger.info("Create : "+indexQuery.getObject());
				}

				
				quries.addAll(indexQuries);
			}
			logger.info("\n");
		}

		if(! quries.isEmpty()) {
			operations.bulkIndex(quries);
			logger.info("Save : "+quries.get(0).getIndexName()+" "+quries.size()+" row(s)");
			logger.info("\n");
		}
	}
}

