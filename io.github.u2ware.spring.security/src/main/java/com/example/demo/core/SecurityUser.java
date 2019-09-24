package com.example.demo.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
public @Data class SecurityUser implements UserDetails {

	private static final long serialVersionUID = -7573038738600269114L;
	
	@Id @GeneratedValue(generator="UUID")  @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator") 
	private UUID id;

    private String username;

	@JsonIgnore 
	private String password;
	
    private boolean enabled = true;
    
    public void setPassword(String password) {
    	this.password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
    }
    
    @JsonIgnore @Transient 
	public boolean isAccountNonExpired() {
		return true;
	}

    @JsonIgnore @Transient 
    public boolean isAccountNonLocked() {
		return true;
	}

    @JsonIgnore @Transient 
	public boolean isCredentialsNonExpired() {
		return true;
	}
    
	@JsonIgnore 
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return securityAuthorities;
	}

	public void setRoles(String... roles) {
		Set<SecurityAuthorities> authorities = new HashSet<SecurityAuthorities>();
		for (String role : roles) {
			SecurityAuthorities.ID id = new SecurityAuthorities.ID();
			id.setSecurityUsers(this);
			id.setAuthority("ROLE_"+role);
			
			SecurityAuthorities a = new SecurityAuthorities();
			a.setId(id);
			
			authorities.add(a);
		}
		this.setSecurityAuthorities(authorities);
	}

	public String[] getRoles() {
		if (securityAuthorities == null)
			return new String[] {};
		String[] roles = new String[securityAuthorities.size()];
		int i = 0;
		for (SecurityAuthorities authority : securityAuthorities) {
			roles[i] = authority.getId().getAuthority();
			i++;
		}
		return roles;
	}

	
	public boolean hasRoles(String... roles) {
		if (securityAuthorities == null)
			return false;
		for (SecurityAuthorities authority : securityAuthorities) {
			for (String role : roles) {
				if (authority.getId().getAuthority().equals(role)) {
					return true;
				}
			}
		}
		return false;
	}
    
	@JsonIgnore @OneToMany(mappedBy = "id.securityUsers", fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
    private Set<SecurityAuthorities> securityAuthorities = new HashSet<>();
    
	
    @SuppressWarnings("serial")
	@Entity @Table(name="security_authorities")
    public static @Data class SecurityAuthorities implements GrantedAuthority{

		@Override
		public String getAuthority() {
			return getId().getAuthority();
		}
    	
		@JsonIgnore @EmbeddedId
    	private ID id;
    	
    	@Embeddable
    	public @Data static class ID implements Serializable{
    		
    		@JsonIgnore @ManyToOne @JoinColumn(name="username")
    		private SecurityUser securityUsers;
            private String authority;
            
			@Override
			public String toString() {
				return authority;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				ID other = (ID) obj;
				if (authority == null) {
					if (other.authority != null)
						return false;
				} else if (!authority.equals(other.authority))
					return false;
				return true;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((authority == null) ? 0 : authority.hashCode());
				return result;
			}

        }
    	
		@Override
		public String toString() {
			return getAuthority();
		}

    }


}