package com.example.demo.members;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Entity
public @Data @Builder class Member {

    public @Tolerate Member(){}

    @Id
    @GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private Integer count;

}