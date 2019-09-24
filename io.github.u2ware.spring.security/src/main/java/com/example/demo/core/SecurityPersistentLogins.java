package com.example.demo.core;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * 
 * @author u2waremanager@gmail.com
 *
 */
@Entity
public @Data class SecurityPersistentLogins {

	@Id
	private String series;

	private String username;
	private String token;
	private Long lastUsed;

}