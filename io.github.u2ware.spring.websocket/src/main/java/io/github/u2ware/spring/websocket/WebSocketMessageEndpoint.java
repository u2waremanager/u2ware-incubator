package io.github.u2ware.spring.websocket;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.web.socket.messaging.StompSubProtocolHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.jayway.jsonpath.JsonPath;

@Controller
public class WebSocketMessageEndpoint {

    private final Log logger = LogFactory.getLog(getClass());

    private @Autowired SimpMessageSendingOperations simpMessageSendingOperations;
	private RestTemplate restTemplate = new RestTemplate();
	
	@Value("${com.iscreammedia.hiclass.chat.WebSocketMessageEndpoint.api.method:PUT}")
	private String apiMethod;
	
	@Value("${com.iscreammedia.hiclass.chat.WebSocketMessageEndpoint.api.uri:http://localhost:8080/chat/}")
	private String apiUri;
   
    
    @MessageMapping("/{room}")
    public void message(
    		@DestinationVariable String room, 
//    		Message<WebSocketMessage> message,
//    		StompHeaderAccessor stompHeaderAccessor,
    		@Payload WebSocketMessage payload, 
    		SimpMessageHeaderAccessor header) {
    	
    	payload.setId(UUID.randomUUID());
    	payload.setRoom(room);
    	
        this.messageStore(payload, header);
        this.messageSend(payload);
    }
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
    	logger.info("SessionConnectEvent: "+ event);
    	StompSubProtocolHandler source = (StompSubProtocolHandler)event.getSource();
    	logger.info("SessionConnectEvent: "+ source);
    	SimpMessageHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
    	logger.info("SessionConnectEvent: "+ header);
    	logger.info("SessionConnectEvent: "+ header.getDestination());
    	logger.info("SessionConnectEvent: "+ header.getSessionId());
    }
    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
    	logger.info("SessionConnectedEvent: "+ event);
//    	StompSubProtocolHandler source = (StompSubProtocolHandler)event.getSource();
//    	logger.info("SessionConnectedEvent: "+ source);
//    	SimpMessageHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
//    	logger.info("SessionConnectedEvent: "+ header);
//    	logger.info("SessionConnectedEvent: "+ header.getDestination());
//    	logger.info("SessionConnectedEvent: "+ header.getSessionId());
    }
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
    	logger.info("SessionSubscribeEvent: "+ event);
//    	StompSubProtocolHandler source = (StompSubProtocolHandler)event.getSource();
//    	logger.info("SessionSubscribeEvent: "+ source);
//    	SimpMessageHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
//    	logger.info("SessionSubscribeEvent: "+ header);
//    	logger.info("SessionSubscribeEvent: "+ header.getDestination());
//    	logger.info("SessionSubscribeEvent: "+ header.getSessionId());
    }
    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
    	logger.info("SessionUnsubscribeEvent: "+ event);
//    	StompSubProtocolHandler source = (StompSubProtocolHandler)event.getSource();
//    	logger.info("SessionUnsubscribeEvent: "+ source);
//    	SimpMessageHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
//    	logger.info("SessionUnsubscribeEvent: "+ header);
//    	logger.info("SessionUnsubscribeEvent: "+ header.getDestination());
//    	logger.info("SessionUnsubscribeEvent: "+ header.getSessionId());
    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    	logger.info("SessionDisconnectEvent: "+ event);
//    	StompSubProtocolHandler source = (StompSubProtocolHandler)event.getSource();
//    	logger.info("SessionDisconnectEvent: "+ source);
    	SimpMessageHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
//    	logger.info("SessionDisconnectEvent: "+ header);
//    	logger.info("SessionDisconnectEvent: "+ header.getDestination());
//    	logger.info("SessionDisconnectEvent: "+ header.getSessionId());

    	WebSocketMessage payload = this.messageLoad(header);
        if(payload != null) {
        	payload.setId(UUID.randomUUID());
        	payload.setContentType(header.getMessageType().toString());
            this.messageSend(payload);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////////////
    protected void messageStore(WebSocketMessage payload, SimpMessageHeaderAccessor header) {
    	String key = getClass().getName();
        header.getSessionAttributes().put(key, payload);
        //logger.info("message store: "+payload);
    }
    
	protected WebSocketMessage messageLoad(SimpMessageHeaderAccessor header) {
    	WebSocketMessage payload = null;
    	String key = getClass().getName();
        Map<String, Object> attrs = header.getSessionAttributes();
        if(attrs != null && attrs.containsKey(key)) {
        	payload = (WebSocketMessage) attrs.get(key);
            //logger.info("message load: "+payload);
        }
        return payload;
    }
    
    protected void messageSend(WebSocketMessage payload) {
    	String destination = WebSocketMessage.WS_SUBSCRIBE_URL + payload.getRoom();
        logger.info(destination+" "+payload);

        try {
        	HttpMethod method = HttpMethod.resolve(apiMethod);
        	URI uri = UriComponentsBuilder.fromUriString(apiUri).pathSegment(payload.getRoom()).build().toUri();
        			
        	RequestEntity<?> request = RequestEntity.method(method, uri).body(payload);
        	String response = restTemplate.exchange(request, String.class).getBody();
            Map<String,Object> api = JsonPath.parse(response).read("$");
            payload.setApi(api);
        }catch(Exception e) {
            logger.info(apiUri + "is not found.");
        }
        
        simpMessageSendingOperations.convertAndSend(destination, payload);
    }
}
