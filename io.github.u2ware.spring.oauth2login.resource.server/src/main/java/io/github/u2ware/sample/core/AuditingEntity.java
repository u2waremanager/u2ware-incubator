package io.github.u2ware.sample.core;

import java.util.UUID;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
public @Data class AuditingEntity {

	private UUID currentUser;
	private String currentAddress;

}
