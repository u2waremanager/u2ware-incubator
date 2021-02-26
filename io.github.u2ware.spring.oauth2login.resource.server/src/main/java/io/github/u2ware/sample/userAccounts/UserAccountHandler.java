package io.github.u2ware.sample.userAccounts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import io.github.u2ware.sample.core.UserAccount;

@RepositoryEventHandler
@Component
public class UserAccountHandler {

	protected Log logger = LogFactory.getLog(getClass());

	private @Autowired UserAccountTokenAuditing userAccountTokenAware;
	
    @HandleBeforeCreate @HandleBeforeSave @HandleBeforeDelete
    public void handleBeforeCreate(UserAccount entity) throws Exception{
    	
		logger.info("handleBeforeCreate: "+entity);
		
    	if(userAccountTokenAware.hasRole("ROLE_ADMIN")) {

    		
    	}else {
    		if(entity.getId() != null && ! userAccountTokenAware.getCurrentPrincipalUuid().equals(entity.getId())) 
    			throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED);
    		
    		entity.setId(userAccountTokenAware.getCurrentPrincipalUuid());
        	entity.setPrincipalName(userAccountTokenAware.getCurrentPrincipalName());
        	entity.setAuthorities(userAccountTokenAware.getCurrentUserAccountAuthorities());
    	}
    }
}
