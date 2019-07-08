package com.example.demo.sign;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Data;

@Entity
public @Data class Authorities {

	@EmbeddedId
	private ID id;
	
	@Embeddable
    @SuppressWarnings("serial")
	public @Data static class ID implements Serializable{
        private String username;
        private String authority;
    }
}