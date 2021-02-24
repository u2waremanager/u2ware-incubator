package io.github.u2ware.sample;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsClaimService {
	
	Map<String, Object> convertUserDetails(UserDetails userDetails);

}
