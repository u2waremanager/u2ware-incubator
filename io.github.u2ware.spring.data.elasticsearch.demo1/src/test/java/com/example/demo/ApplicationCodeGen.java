package com.example.demo;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@SuppressWarnings("rawtypes")
public class ApplicationCodeGen extends ApplicationTests{

	@Autowired(required=false)
    private ElasticsearchRestTemplate elasticsearchTemplate;

	
	private String indexName = "dsp_base_2_1_20190130";
	//private String indexName = "dsp_analyzed_2_1_20190114";
	//private String indexName = "dsp_alarm_1_7_20190117";
	
	private String indexType = "logs";
	
	@Test
	public void contextLoads() throws Exception {
		Map mappings = elasticsearchTemplate.getMapping(indexName, indexType);
		logger.info("mappings: "+mappings);
		
		Map properties =  map(map(map(map(mappings, indexName), "mappings"), indexType), "properties");
		logger.info("properties: "+properties);
		printHeader();
		for(Object key : properties.keySet()) {
			printProperty(key, (Map)properties.get(key));
		}
		printFooter();
	}
	
	private Map map(Object src, Object key) {
		return (Map)((Map)src).get(key);
	}
	
	private void printHeader() {
	}	
	private void printProperty(Object key, Map properties) {
		
		try {
			String type = properties.get("type").toString();
			if("keyword".equals(type)) {
				System.err.print("\t@Field(type=FieldType.Keyword)");
				System.err.println("\tprivate String "+key+";");
			}else if("integer".equals(type)) {
				System.err.print("\t@Field(type=FieldType.Integer)");
				System.err.println("\tprivate Integer "+key+";");
			}else if("date".equals(type)) {
				System.err.print("\t@Field(type=FieldType.Date)");
				System.err.println("\t\tprivate Long "+key+";");
			}else if("ip".equals(type)) {
				System.err.print("\t@Field(type=FieldType.Ip)");
				System.err.println("\t\tprivate String "+key+";");
			}else if("long".equals(type)) {
				System.err.print("\t@Field(type=FieldType.Long)");
				System.err.println("\t\tprivate Long "+key+";");
			}else if("float".equals(type)) {
				System.err.print("\t@Field(type=FieldType.Float)");
				System.err.println("\tprivate Float "+key+";");
			}else if("text".equals(type)) {
				System.err.print("\t@Field(type=FieldType.Text)");
				System.err.println("\t\tprivate String "+key+";");
			}else {
				System.out.println("\tprivate "+type+" "+key);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	

	private void printFooter() {
	
	}	
}