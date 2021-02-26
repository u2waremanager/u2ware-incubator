package io.github.u2ware.sample.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_account")
@EntityListeners(AuditingEntityListener.class)
public @Data class UserAccount {

	@Id 
	private UUID id;
	
	public String username;
	public String email;
	public String address;
	public String tel;
	public @Column(name = "principal_name", updatable = false) String principalName;
	
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_account_authorities", joinColumns=@JoinColumn(name="username"))
    private Collection<UserAccountAuthority> authorities;

    @Embeddable
    public static @Data @AllArgsConstructor @NoArgsConstructor class UserAccountAuthority {
        private String authority;
    }
    
    
	@Embedded @AttributeOverrides({
	@AttributeOverride( name = "currentUser", column = @Column(name = "created_user", updatable = false)),
	@AttributeOverride( name = "currentAddress", column = @Column(name = "created_address", updatable = false))})
	private @JsonProperty(access = Access.READ_ONLY) @CreatedBy AuditingEntity created;
	private @JsonProperty(access = Access.READ_ONLY) @Column(name = "created_timestamp", updatable = false) @CreatedDate Long createdTimestamp;
	@Embedded @AttributeOverrides({
	@AttributeOverride( name = "currentUser", column = @Column(name = "updated_user")),
	@AttributeOverride( name = "currentAddress", column = @Column(name = "updated_address"))})
	private @JsonProperty(access = Access.READ_ONLY) @LastModifiedBy AuditingEntity updated;
	private @JsonProperty(access = Access.READ_ONLY) @Column(name = "updated_timestamp") @LastModifiedDate Long updatedTimestamp;

	@Transient @JsonIgnore
    public void setRoles(String... roles) {
        Set<UserAccountAuthority> authorities = new HashSet<UserAccountAuthority>();
        for (String role : roles) {
        	UserAccountAuthority a = new UserAccountAuthority();
            a.setAuthority(role);
            authorities.add(a);
        }
        this.setAuthorities(authorities);
    }

	@Transient @JsonIgnore
    public String[] getRoles() {
        if (authorities == null)
            return new String[] {};
        String[] roles = new String[authorities.size()];
        int i = 0;
        for (UserAccountAuthority authority : authorities) {
            roles[i] = authority.getAuthority();
            i++;
        }
        return roles;
    }


	@Transient @JsonIgnore
    public boolean hasRoles(String... roles) {
        if (authorities == null)
            return false;
        for (UserAccountAuthority authority : authorities) {
            for (String role : roles) {
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

}
