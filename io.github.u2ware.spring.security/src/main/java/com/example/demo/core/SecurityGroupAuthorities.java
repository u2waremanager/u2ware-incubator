package com.example.demo.core;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Data;

@Entity
public @Data class SecurityGroupAuthorities {

	@EmbeddedId
	private ID id;
	
	@Embeddable
    @SuppressWarnings("serial")
	public @Data static class ID implements Serializable{
		private SecurityGroups groupId;
	    private String authority;
    }


}
