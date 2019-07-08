package com.example.demo.sign;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class GroupMembers {

    @Id 
    @GeneratedValue
    private Long id;

    private Long groupId;
    private String username;

}
