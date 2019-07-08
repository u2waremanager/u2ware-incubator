package com.example.demo.sign;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class GroupAuthorities {

    @Id 
    @GeneratedValue
    private Long seq;

    private Long groupId;
    private String authority;

}
