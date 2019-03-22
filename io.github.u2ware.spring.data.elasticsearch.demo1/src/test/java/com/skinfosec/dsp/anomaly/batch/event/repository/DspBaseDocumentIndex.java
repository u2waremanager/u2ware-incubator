package com.skinfosec.dsp.anomaly.batch.event.repository;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public @Data class DspBaseDocumentIndex {

	private String indexName = "";
	private String type = "";
	
}
