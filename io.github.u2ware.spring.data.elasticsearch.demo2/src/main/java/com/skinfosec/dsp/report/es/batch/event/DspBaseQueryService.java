package com.skinfosec.dsp.report.es.batch.event;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;


@Component
public class DspBaseQueryService {

	protected Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private ElasticsearchOperations operations;
	
	public void query(SearchQuery query) {
		query(query, null, false);
	}
	public void query(SearchQuery query, boolean showHits) {
		query(query, null, showHits);
	}
	public void query(SearchQuery query, DspBaseAggregationsHandler extractor) {
		query(query, extractor, false);
	}
	public void query(SearchQuery query, DspBaseAggregationsHandler extractor, boolean showHits) {
		AggregatedPage<DspBase> page =  (AggregatedPage<DspBase>)operations.queryForPage(query, DspBase.class);
		resolveContent(page, extractor, showHits);
	}
	
	private void resolveContent(AggregatedPage<DspBase> page, DspBaseAggregationsHandler extractor, boolean showHits) {
		logger.info("\t");
		logger.info("DspBase : "+page.getTotalElements()+" row(s)");
		if(showHits) {
			for(DspBase e : page) {
				logger.info("\t"+e);
			}
		}
		resolveAggregations(page.getAggregations(), 1, extractor, new DspBaseAggregations());
	}
	
	private void resolveAggregations(Aggregations aggregations, int depth, DspBaseAggregationsHandler handler, DspBaseAggregations buckets) {
		
		String tabs = IntStream.range(0, depth).mapToObj(i -> "\t").collect(Collectors.joining(""));

		for (Aggregation a : aggregations) {
			if(a instanceof Terms) {
				Terms t = (Terms)a;
				for(Terms.Bucket b : t.getBuckets()) {
					logger.info(tabs+"name="+t.getName()+", type="+t.getType()+", key="+b.getKey()+", docCount="+b.getDocCount());
					buckets.put(t.getName(), b);
					
					if(b.getAggregations().asList().stream().count() == 0 && handler != null) {
						try {
							handler.handle(buckets);
						}catch(Exception e) {
							logger.info(handler, e);
						}
					}
					resolveAggregations(b.getAggregations(), (depth+1), handler, buckets);
				}
				
			}else if(a instanceof Range){
				Range t = (Range)a;

				for(Range.Bucket b : t.getBuckets()) {
					logger.info(tabs+"name="+t.getName()+", type="+t.getType()+", key="+b.getKey()+", docCount="+b.getDocCount());
					buckets.put(t.getName(), b);
					
					if(b.getAggregations().asList().stream().count() == 0 && handler != null) {
						try {
							handler.handle(buckets);
						}catch(Exception e) {
							logger.info(handler, e);
						}
					}
					resolveAggregations(b.getAggregations(), (depth+1), handler, buckets);
				}
			}
		}
	}

//	private void resolveAggregations(Aggregations aggregations, int depth) {
//		logger.info("### Aggregations: "+aggregations);
//		if(aggregations == null) return;
//
//		for (Aggregation a : aggregations) {
//			String tabs = IntStream.range(0, depth).mapToObj(i -> "\t").collect(Collectors.joining(""));
//			MultiBucketsAggregation t = (MultiBucketsAggregation)a;
//			for(MultiBucketsAggregation.Bucket b : t.getBuckets()) {
//				logger.info(tabs+": name="+t.getName()+", type="+t.getType()+", key="+b.getKey()+", docCount="+b.getDocCount());
//				resolveAggregations(b.getAggregations(), (depth+1));
//			}
//		}
//	}
	
}
