package com.u2ware.elasticsearch.book;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BookRepository extends ElasticsearchRepository<Book,String>{

	
}
