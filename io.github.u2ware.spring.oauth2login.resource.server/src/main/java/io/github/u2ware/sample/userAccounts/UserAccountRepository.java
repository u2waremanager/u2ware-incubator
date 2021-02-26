package io.github.u2ware.sample.userAccounts;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import io.github.u2ware.sample.core.UserAccount;

public interface UserAccountRepository extends PagingAndSortingRepository<UserAccount, UUID>, JpaSpecificationExecutor<UserAccount>{

	UserAccount findOneByPrincipalName(String principalName);
	
}
