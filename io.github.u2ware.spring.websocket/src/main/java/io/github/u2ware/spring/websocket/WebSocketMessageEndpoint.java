package io.github.u2ware.spring.websocket;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Controller
public class WebSocketMessageEndpoint {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;
	
    @MessageMapping("/{room}")
    public void message(
    		@DestinationVariable String room, 
    		@Payload WebSocketMessage payload, 
    		SimpMessageHeaderAccessor header) {
    	
        payload.setRoom(room);    
        this.messageStore(header, payload);
        this.messageSend(payload);
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        //this.print("SessionDisconnectEvent", event);
    	
        WebSocketMessage payload = this.messageLoad(StompHeaderAccessor.wrap(event.getMessage()));
        if(payload != null && payload.isConnected()) {
        	payload.setConnected(false);
            this.messageSend(payload);
        }
    }
    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
//        this.print("SessionUnsubscribeEvent", event);
    }
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
//        this.print("SessionSubscribeEvent", event);
    }
    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
//        this.print("SessionConnectedEvent", event);
    }
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
//        this.print("SessionConnectEvent", event);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////////////
    protected void print(String state, AbstractSubProtocolEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    	logger.info(state+"1 : "+event);
    	logger.info(state+"2 : "+event.getClass());
        logger.info(state+"3 : "+event.getMessage());
        logger.info(state+"4 : "+event.getSource());
        logger.info(state+"5 : "+event.getUser());
        logger.info(state+"6 : "+event.getTimestamp());
        logger.info(state+"7 : "+headerAccessor);
        //GenericMessage<WebSocketMessage> message = (GenericMessage)event.getMessage();
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////////////
    protected void messageStore(SimpMessageHeaderAccessor header, WebSocketMessage payload) {
    	String key = WebSocketMessage.class.getName();
        header.getSessionAttributes().put(key, payload);
        logger.info("message store: "+payload);
    }
    
    protected WebSocketMessage messageLoad(SimpMessageHeaderAccessor header) {
    	WebSocketMessage payload = null;
    	String key = WebSocketMessage.class.getName();
        Map<String, Object> attrs = header.getSessionAttributes();
        if(attrs != null && attrs.containsKey(key)) {
        	payload = (WebSocketMessage) attrs.get(key);
            logger.info("message load: "+payload);
        }
        return payload;
    }
    
    protected void messageSend(WebSocketMessage payload) {
    	String destination = WebSocketMessage.WS_SUBSCRIBE_URL + payload.getRoom();
        logger.info("message send: "+payload);
        simpMessageSendingOperations.convertAndSend(destination, payload);
    }
}
