package io.github.u2ware.spring.websocket.message;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Entity
public @Data class ChatMessage {

	private @Id UUID id;
	private String room;
	private String sender;
	private String contentType;
	private String content;
	private Long timestamp;
	private boolean last;
	
	@Convert(converter = AttributeSetConverter.class)
	@JsonProperty(access = Access.READ_ONLY)
    private Set<String> invitedMembers; 
	
	@Convert(converter = AttributeSetConverter.class)
	@JsonProperty(access = Access.READ_ONLY)
    private Set<String> joinedMembers; 

	@Convert(converter = AttributeSetConverter.class)
	@JsonProperty(access = Access.READ_ONLY)
    private Set<String> connectedMembers; 
	
	
	@Convert(converter = AttributeSetConverter.class)
	@JsonProperty(access = Access.READ_ONLY)
    private Set<String> readMembers; 

	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	public String getChatUri() {
		return "/chat/"+room;
	}
	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	public String getWebsocketUri() {
		return "/ws";
	}
	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	public String getSubscribeUri() {
		return "/queue/"+room;
	}
	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	public String getPublishUri() {
		return "/publish/"+room;
	}
	

	//////////////////////////////////////////
	//
	/////////////////////////////////////////
	public static class AttributeSetConverter implements AttributeConverter<Set<?>, String> {
		 
	    private final Log logger = LogFactory.getLog(getClass());

		private ObjectMapper objectMapper = new ObjectMapper();
		
	    @Override
	    public String convertToDatabaseColumn(Set<?> customerInfo) {
	 
	        String customerInfoJson = null;
	        try {
	            customerInfoJson = objectMapper.writeValueAsString(customerInfo);
	        } catch (final JsonProcessingException e) {
	            logger.error("JSON writing error", e);
	        }
	 
	        return customerInfoJson;
	    }
	 
	    @SuppressWarnings("unchecked")
		@Override
	    public Set<Object> convertToEntityAttribute(String customerInfoJSON) {
	 
	    	Set<Object> customerInfo = null;
	        try {
	            customerInfo = objectMapper.readValue(customerInfoJSON, Set.class);
	        } catch (final IOException e) {
	            logger.error("JSON reading error", e);
	        }
	 
	        return customerInfo;
	    }
	}	
}









