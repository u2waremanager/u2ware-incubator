package com.skinfosec.dsp.report.es.batch.event;

public interface DspBaseAggregationsHandler{

	public void handle(DspBaseAggregations aggregations) throws Exception;
	
}
