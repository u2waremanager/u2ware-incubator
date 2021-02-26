package io.github.u2ware.sample.userNotifies;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import io.github.u2ware.sample.core.UserNotify;


@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface UserNotifyRepository extends PagingAndSortingRepository<UserNotify, UUID>, JpaSpecificationExecutor<UserNotify>{

	
}
