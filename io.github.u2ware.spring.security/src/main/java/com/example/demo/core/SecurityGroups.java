package com.example.demo.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class SecurityGroups {

    @Id 
    @GeneratedValue
    private Long id;

    private String groupName;
}
