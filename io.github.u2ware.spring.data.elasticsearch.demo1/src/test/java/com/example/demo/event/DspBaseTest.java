package com.example.demo.event;

import java.util.Iterator;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.example.demo.ApplicationTests;
import com.example.demo.event.DspBase;
import com.example.demo.event.DspBaseQueryBuilder;
import com.example.demo.rule.AnomRuleDmn;
import com.example.demo.rule.AnomRuleDmnRepository;

public class DspBaseTest extends ApplicationTests{

	@Autowired
    private AnomRuleDmnRepository anomRuleDmnRepository;

	@Autowired
    private ElasticsearchOperations operations;
	
	@Test
	public void contextLoads() throws Exception {

//		Long minTime = null;
//		Long maxTime = null;
		Long minTime = DateTime.parse("2019-02-21 14:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).getMillis();
		Long maxTime = DateTime.parse("2019-02-21 17:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).getMillis();
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
				resolveContent(queryResult);		
				resolveAggregations(queryResult);			
			}
		}
	}
	
	private void resolveContent(AggregatedPage<DspBase> page) {
		logger.info("resolveContent : "+page.getClass());
		logger.info("resolveContent : "+page.getTotalElements()+" row(s)");
		logger.info("resolveContent : "+page.getContent().size());
		Iterator<DspBase> it = page.iterator();
		int i=0;
		while(it.hasNext()) {
			it.next();
			i++;
		}
		logger.info("resolveContent : "+i);
	}

	private void resolveAggregations(AggregatedPage<DspBase> page) {
		resolveAggregations(page.getAggregations());
	}
	
	
	private void resolveAggregations(Aggregations aggregations) {
		logger.info("### Aggregations: "+aggregations);
		if(aggregations == null) return;
		for (Aggregation a : aggregations) {
			if(a instanceof Terms) {
				Terms t = (Terms)a;
				logger.info("### Terms: "+t+" "+t.getName()+" "+t.getType()+" "+t.getSumOfOtherDocCounts()+" "+t.getDocCountError());
				for(Bucket b : t.getBuckets()) {
					logger.info("### Bucket: "+b+" "+b.getKey()+"  "+b.getDocCount());
					resolveAggregations(b.getAggregations());
				}
			}else {
				logger.info("### Agg: "+a);
			}
		}
	}
}

