package com.example.demo.userEvent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class UserEvent  {

    @Id 
    @GeneratedValue
    private Long seq;

    private String username;
    private Action action;
    private Long timestamp;

    public enum Action{
        LOGON,
        LOGOFF,
        FAILURE,
    }
}