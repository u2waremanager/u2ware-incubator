package com.example.demo.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class SecurityEvents  {

    @Id 
    @GeneratedValue
    private Long seq;

    private String username;
    private String action;
    private Long timestamp;

}