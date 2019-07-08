package com.example.demo.signin;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalAuditor implements AuditorAware<Principal> {

    protected Log logger = LogFactory.getLog(getClass());

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Principal getPrincipal() {
        try {
            return (Principal) getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<Principal> getCurrentAuditor() {
        return Optional.ofNullable(getPrincipal());
    }
}
