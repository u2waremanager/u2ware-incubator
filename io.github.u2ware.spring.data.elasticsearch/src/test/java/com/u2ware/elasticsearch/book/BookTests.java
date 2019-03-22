package com.u2ware.elasticsearch.book;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;

import com.u2ware.elasticsearch.ApplicationTests;

public class BookTests extends ApplicationTests{

//	@Autowired(required=false)
//    private BookIndex bookIndex;

	@Autowired(required=false)
    private BookRepository repository;

	@Autowired(required=false)
	private ElasticsearchTemplate template;
	
	@Autowired(required=false)
	private ElasticsearchRestTemplate restTemplate;

	
	@Autowired(required=false)
	private RestHighLevelClient client;
	
	
	@Autowired(required=false)
	private ElasticsearchOperations[] operations;
	
	@Autowired
	private ElasticsearchConverter elasticsearchConverter;

	@Autowired
	private BookDocument bookDocument;

	@Test
	public void contextLoads() throws Exception {
		
		logger.info("1. "+client);
		logger.info("2. "+operations);
		logger.info("3. "+operations.length);
		logger.info("4. "+template);
		logger.info("5. "+restTemplate);
		logger.info("6. "+repository);
		//_cat/indices?v
		//foo/_mapping
		//foo/_search
		
//
//		ElasticsearchPersistentEntity e1 = elasticsearchConverter.getMappingContext().getRequiredPersistentEntity(Book.class);
//		logger.info("# "+e1);
//		logger.info("# "+e1.getIndexName());
//		ElasticsearchPersistentEntity e2 = elasticsearchConverter.getMappingContext().getRequiredPersistentEntity(Book.class);
//		logger.info("# "+e2);
//		logger.info("# "+e2.getIndexName());
		
		
		/////////////////////////////////////////////
		//
		////////////////////////////////////////////
		bookDocument.setIndex("01");
		
		long c1 = repository.count();
		logger.info(c1);
		repository.save(new Book("bb"));
		//repository.refresh();

		long c2 = repository.count();
		logger.info(c2);
		
		Assert.assertEquals(c1 + 1, c2);
	}
	
}
