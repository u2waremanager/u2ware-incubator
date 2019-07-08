package io.github.u2ware.sample;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NimbusJwtDecoderTest {

    private Log logger = LogFactory.getLog(getClass());
	
	@Test
	public void contextLoads() throws Exception{


	    ///////////////////////////////////////////
	    //  Encode JWT2
	    ///////////////////////////////////////////
	    logger.info("---------------------------------------------");
	    logger.info("---------------------------------------------");
	    logger.info("---------------------------------------------");
	    logger.info("---------------------------------------------");
	    String token3 = 
"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpZCI6MTA4MTYxMzM0NCwicHJvcGVydGllcyI6eyJuaWNrbmFtZSI6IuydtOq0keyLnSIsInByb2ZpbGVfaW1hZ2UiOiJodHRwOlwvXC9rLmtha2FvY2RuLm5ldFwvZG5cL0xYZDVmXC9idHF1Nk5rQ2dmNFwvRGpSV29NdVRwOUpVRFNWa29GQlMzS1wvcHJvZmlsZV82NDB4NjQwcy5qcGciLCJ0aHVtYm5haWxfaW1hZ2UiOiJodHRwOlwvXC9rLmtha2FvY2RuLm5ldFwvZG5cL0xYZDVmXC9idHF1Nk5rQ2dmNFwvRGpSV29NdVRwOUpVRFNWa29GQlMzS1wvcHJvZmlsZV8xMTB4MTEwYy5qcGcifX0.KcnxpcNx1fb0IECoNWZhLS_MwU-Q6wLlDjCB2jA_VXtRY5jgQw94NZiJNQ-qxnFA8TLLLJkjAtTRqeqfHbKek1r4ToxITwDvW4mrUjOn80MbTZ68MIjwARazOaT0JPtRVck46ATq4elIM_soMHuVSDtaYHhat3AU0p-oeGBgkb9de2OUETG18WU9Fu4kHGZGTuZIkVc8VKelFJvsytSDA4OM2WhZcLzQ-gr7hu7GgDfYDnnB5gW95-ATXHIUX6ViDwdZTpJPsXkmjyhQE65PO-_KIRDZkTTN8HmhKFa80rcqzRCmrYgrvETS2r4W_FjBAaJd2Af9tiXKA_rfCMJzAw"
	    		;
	    Jwt jwt3 = new NimbusJwtDecoder(new URL("http://localhost:9091/token/jwks.json")).decode(token3);
	    logger.info(jwt3);
	    logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jwt3));


    //DefaultJWTProcessor f;
	}
}
