package io.github.u2ware.sample.userAccounts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import io.github.u2ware.sample.core.UserAccount;

@RepositoryEventHandler
@Component
public class UserAccountHandler {

	protected Log logger = LogFactory.getLog(getClass());

	
	private @Autowired UserAccountTokenAuditing userAccountTokenAware;
//	private @Autowired UserAccountRepository userAccountRepository;
	
	
    @HandleBeforeCreate //@HandleBeforeSave
    public void handleBeforeCreate(UserAccount entity) {
    	
    	logger.info("!!!!!!!!!!!");
    	logger.info("!!!!!!!!!!!");
    	logger.info("!!!!!!!!!!!");
    	logger.info("!!!!!!!!!!!");
    	logger.info("!!!!!!!!!!!");
    	
    	
    	
    	UserAccount exists = userAccountTokenAware.getCurrentUserAccount();//.findOneByOauth2Name(principalAware.getCurrentPrincipalName());
    	logger.info(exists);
    	logger.info(exists);
    	logger.info(exists);
    	
    	if(exists == null) { 
    		logger.info("handleBeforeCreate: create by self user");
        	//insert principalAware UserAccount
    		entity.setId(userAccountTokenAware.getCurrentPrincipalUuid());
        	entity.setPrincipalName(userAccountTokenAware.getCurrentPrincipalName());
        	entity.setAuthorities(userAccountTokenAware.getCurrentUserAccountAuthorities());
        	
    	}else {
			if(exists.hasRoles("ROLE_ADMIN")) {
	    		logger.info("handleBeforeCreate: modify by admin ");
	        	//insert or update UserAccount .. 
	        	entity.setCreated(exists.getCreated());
	    		
			}else {
	    		logger.info("handleBeforeCreate: modify by self user");
	        	//update principalAware UserAccount
	        	entity.setCreated(exists.getCreated());
	    		entity.setId(exists.getId());
	        	entity.setPrincipalName(userAccountTokenAware.getCurrentPrincipalName());
	        	entity.setAuthorities(userAccountTokenAware.getCurrentUserAccountAuthorities());
			}
    	}
		logger.info("handleBeforeCreate: "+entity);
    }
}
