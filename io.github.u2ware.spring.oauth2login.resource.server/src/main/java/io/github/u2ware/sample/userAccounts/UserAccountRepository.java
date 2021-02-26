package io.github.u2ware.sample.userAccounts;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.github.u2ware.sample.core.UserAccount;

public interface UserAccountRepository extends PagingAndSortingRepository<UserAccount, UUID>, JpaSpecificationExecutor<UserAccount>{

	
	@RestResource(exported = false)
	void deleteById(UUID id) ;
	
	
	UserAccount findOneByPrincipalName(String principalName);
	
}
