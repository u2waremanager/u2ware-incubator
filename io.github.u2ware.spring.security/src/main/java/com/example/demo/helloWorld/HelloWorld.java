package com.example.demo.helloWorld;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class HelloWorld{

    @Id @GeneratedValue
    private Long id;
    private String name;

}

