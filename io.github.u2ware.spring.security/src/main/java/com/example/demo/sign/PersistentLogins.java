package com.example.demo.sign;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * 
 * @author u2waremanager@gmail.com
 *
 */
@Entity
public @Data class PersistentLogins {

	@Id
	private String series;

	private String username;
	private String tokenValue;
	private Long tokenDate;

}
