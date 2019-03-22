package com.u2ware.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.support.ExtendedElasticsearchRepositoryFactoryBean;



@SpringBootApplication
//@EnableElasticsearchRepositories
//@EnableElasticsearchRepositories(elasticsearchTemplateRef="elasticsearchOperations")
//@EnableElasticsearchRepositories(elasticsearchTemplateRef="elasticsearchOperations", repositoryFactoryBeanClass=ExtendedElasticsearchRepositoryFactoryBean.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/////////////////////////////////////////////////////////////
	//spring.data.elasticsearch.cluster-nodes=127.0.0.1:9300
	/////////////////////////////////////////////////////////////
//	@Autowired
//	private Client transportClient;
//
//	@Autowired
//	private ElasticsearchConverter converter;
//	
//	
//	@Bean
//	public ElasticsearchTemplate elasticsearchOperations1() {
//		return new ElasticsearchTemplate1(transportClient, converter);
//	}
	
	/////////////////////////////////////////////////////////////
	//spring.elasticsearch.rest.urls=http://127.0.0.1:9200
	/////////////////////////////////////////////////////////////
//	@Autowired
//	private RestHighLevelClient restClient;
//	
//	@Autowired
//	private ElasticsearchConverter converter;
//
//	@Bean
//	public ElasticsearchOperations elasticsearchOperations() {
//		return new ElasticsearchRestTemplate(restClient, converter);
//	}
}