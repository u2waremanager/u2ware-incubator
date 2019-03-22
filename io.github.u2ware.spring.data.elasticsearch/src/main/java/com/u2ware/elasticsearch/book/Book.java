package com.u2ware.elasticsearch.book;

import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Document(indexName = "#{bookDocument.getIndex()}", type = "#{bookDocument.getType()}")
public @Data class Book {

    @Id
    private String id = UUID.randomUUID().toString();

    private String title;
    private String author;
    private String releaseDate = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
    
    
    public Book() {
    	
    }
    public Book(String title) {
    	this();
    	this.title = title;
    }
}