package io.github.u2ware.sample.x;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NimbusController implements InitializingBean  {

	private NimbusEncoder encoder;// = new NimbusJwtEncoder(JWKKeypairSet.getFile());
	//private NimbusJwtDecoder decoder;// = new NimbusJwtDecoder(JWKKeypairSet.getFile());
	
	@Override
	public void afterPropertiesSet() throws Exception {
		encoder = new NimbusEncoder(new ClassPathResource("JWKKeypairSet.json").getFile());
	}

	@PostMapping("/nimbus/jwks.json")
	public @ResponseBody Object createJwt(@RequestParam Map<String,Object> claims) throws Exception{
        return encoder.claims(claims);
    }
	
	@GetMapping("/nimbus/jwks.json")
	public @ResponseBody Map<String, Object> getKey() {
		return encoder.getJWKSet().toJSONObject(true);
	}
}
