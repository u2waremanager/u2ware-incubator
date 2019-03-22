package com.skinfosec.dsp.anomaly.batch.event;

import static com.skinfosec.dsp.anomaly.batch.event.Constants.AGG_ANOM;
import static com.skinfosec.dsp.anomaly.batch.event.Constants.AGG_ASSET;
import static com.skinfosec.dsp.anomaly.batch.event.Constants.AGG_STATS;
import static com.skinfosec.dsp.anomaly.batch.event.Constants.AGG_UNIQ;
import static com.skinfosec.dsp.anomaly.batch.event.Constants.ANOM_OPR_CODE_UNIQUE;
import static com.skinfosec.dsp.anomaly.batch.event.Constants.BASE_NORMAL_EVENT;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.stats;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Component
public class DspBaseQueryBuilder {
	
	private Long tenantId;
	private Long domainId;
	private String targetFieldName;
	private String targetFieldOprtrCode;
	private String anomRuleGroupFieldName;
	private Long minTime;
	private Long maxTime;
	
	public DspBaseQueryBuilder tenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}
	public DspBaseQueryBuilder domainId(Long domainId) {
		this.domainId = domainId;
		return this;
	}
	public DspBaseQueryBuilder targetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
		return this;
	}
	public DspBaseQueryBuilder targetFieldOprtrCode(String targetFieldOprtrCode) {
		this.targetFieldOprtrCode = targetFieldOprtrCode;
		return this;
	}
	public DspBaseQueryBuilder anomRuleGroupFieldName(String anomRuleGroupFieldName) {
		this.anomRuleGroupFieldName = anomRuleGroupFieldName;
		return this;
	}
	public DspBaseQueryBuilder minTime(Long minTime) {
		this.minTime = minTime;
		return this;
	}
	public DspBaseQueryBuilder maxTime(Long maxTime) {
		this.maxTime = maxTime;
		return this;
	}
	
	public SearchQuery build() {
		Assert.notNull(tenantId, "'tenantId' must not be null");
		Assert.notNull(domainId, "'domainId' must not be null");
		Assert.notNull(targetFieldName, "'targetFieldName' must not be null");
		Assert.notNull(targetFieldOprtrCode, "'targetFieldOprtrCode' must not be null");
		Assert.notNull(anomRuleGroupFieldName, "'anomRuleGroupFieldName' must not be null");
		
		return new NativeSearchQueryBuilder()
			    .withIndices(extractIndex())
			    .withTypes(extractType())
			    .withQuery(extractQuery())
			    .addAggregation(extractAggregation())
			    .build();
	}

	/////////////////////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////////////////////
	private String extractIndex() {
		String index = "dsp_base_*"+tenantId+"*";
		if(minTime != null && maxTime != null) {
			index = index + "_"+new DateTime(minTime).toString("yyyyMMdd");
		}
		return index;
	}

	private String extractType() {
		return "logs";
	}
	
	private BoolQueryBuilder extractQuery() {
		
		BoolQueryBuilder bool = boolQuery();
		if(minTime != null && maxTime != null) {
			bool.must(rangeQuery("eventStartTime").gte(minTime).lte(maxTime));
		}
		bool.must(termQuery("eventType", BASE_NORMAL_EVENT));
		bool.must(termQuery("tenantId", tenantId));
		bool.must(termQuery("domainId", domainId));
		return bool;
	}
	
	private AbstractAggregationBuilder<?> extractAggregation() {
		
		String[] fields = StringUtils.commaDelimitedListToStringArray(anomRuleGroupFieldName);
		StringBuilder groupFields = new StringBuilder();
		groupFields.append("[");
		for(String field : fields) {
			if(groupFields.length() > 1) {
				groupFields.append(",");
			}
			groupFields.append("doc."+field+".value");
		}
		groupFields.append("].join(\",\")");
		

		Script script = new Script(groupFields.toString());
		TermsAggregationBuilder subAggregation = terms(AGG_ANOM).script(script).size(10000);

		// Target Field Aggregation 설정
		if(ANOM_OPR_CODE_UNIQUE.equals(targetFieldOprtrCode) ){
			// Unique Count : UniqueValue Field Accumulation 분석
			AggregationBuilder aggUniqValue = terms(AGG_UNIQ).field(targetFieldName).size(10000);
			subAggregation = subAggregation.subAggregation(aggUniqValue);
		} else {
			// Matrix : operatedValue Field Accumulation Matrix 분석
			StatsAggregationBuilder aggStats = stats(AGG_STATS).field(targetFieldName);
			subAggregation = subAggregation.subAggregation(aggStats);
		}
		
		TermsAggregationBuilder aggregation = terms(AGG_ASSET).field("assetsId").size(10000);
		aggregation.subAggregation(subAggregation);
		
		return aggregation;
	}
	
}
