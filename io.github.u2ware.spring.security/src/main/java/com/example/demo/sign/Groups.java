package com.example.demo.sign;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class Groups {

    @Id 
    @GeneratedValue
    private Long id;

    private String groupName;
}
