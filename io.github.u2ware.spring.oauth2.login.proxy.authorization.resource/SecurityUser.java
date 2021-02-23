package io.github.u2ware.sample.secured;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="security_users")
public @Data @SuppressWarnings("serial") class SecurityUser implements UserDetails {

    @Id
    private String username;

    private @Column(name="enabled") boolean enabled = true;
    private @Column(name="accountNonExpired") boolean accountNonExpired = true;
    private @Column(name="accountNonLocked") boolean accountNonLocked = true;
    private @Column(name="credentialsNonExpired") boolean credentialsNonExpired = true;
    private @Column(name="password") @JsonIgnore String password;

    @ElementCollection
    @CollectionTable(name="security_users_authorities", joinColumns=@JoinColumn(name="username"))
    private Collection<Authority> authorities;

    @Embeddable
    public @Data static class Authority implements GrantedAuthority{
        private String authority;
    }

    ////////////////////////////////////
    //
    ///////////////////////////////////
    public void setPassword(String password) {
        this.password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
    }

    public void setRoles(String... roles) {
        Set<Authority> authorities = new HashSet<Authority>();
        for (String role : roles) {
            Authority a = new Authority();
            a.setAuthority("ROLE_"+role);
            authorities.add(a);
        }
        this.setAuthorities(authorities);
    }

    public String[] getRoles() {
        if (authorities == null)
            return new String[] {};
        String[] roles = new String[authorities.size()];
        int i = 0;
        for (Authority authority : authorities) {
            roles[i] = authority.getAuthority();
            i++;
        }
        return roles;
    }


    public boolean hasRoles(String... roles) {
        if (authorities == null)
            return false;
        for (Authority authority : authorities) {
            for (String role : roles) {
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}