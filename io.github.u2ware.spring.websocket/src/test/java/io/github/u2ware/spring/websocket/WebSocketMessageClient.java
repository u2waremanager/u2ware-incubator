package io.github.u2ware.spring.websocket;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketMessageClient {
	
	protected Log logger = LogFactory.getLog(getClass());

	public interface Connector{
		public void onConnected(Connection session);
	}

	public interface Connection{
		            
		public void subscriptions(String uri, Subcreiber subcreiber);
		public void broadcasting(String uri, Publisher publisher);
		public void disconnect(Throwable t);
		public void disconnect();
		
		public interface Subcreiber{
			public void receive(WebSocketMessage message);
		}
		public interface Publisher{
			public WebSocketMessage send();
		}
	}
	
	
    private final SockJsClient sockJsClient;
    private final WebSocketStompClient stompClient;
    private final CountDownLatch latch ;
    private final AtomicReference<Throwable> failure ;

	public WebSocketMessageClient() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        
        
        this.latch = new CountDownLatch(1);
        this.failure = new AtomicReference<>();
	}
	
	public void connect(String uri, Connector connector) throws Exception{
		
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        StompSessionHandler handler = new StompSessionHandlerAdapter() {
        	
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
            	logger.info("handleFrame");
                failure.set(new Exception(headers.toString()));
            }

            @Override
            public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            	logger.info("handleException");
                failure.set(ex);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable ex) {
            	logger.info("handleTransportError");
                failure.set(ex);
            }
            
            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
            	logger.info("afterConnected");

            	connector.onConnected(new Connection() {
					@Override
					public void subscriptions(String uri, Subcreiber subcreiber) {
						session.subscribe(uri, new StompFrameHandler() {
							@Override
							public Type getPayloadType(StompHeaders headers) {
								return WebSocketMessage.class;
							}
							@Override
							public void handleFrame(StompHeaders headers, Object payload) {
								WebSocketMessage message = (WebSocketMessage) payload;
								subcreiber.receive(message);
							}
						});
					}

					@Override
					public void broadcasting(String uri, Publisher publisher) {
			            try {
			                session.send(uri, publisher.send());
			            } catch (Throwable t) {
			                failure.set(t);
			                latch.countDown();
			            }

					}

					@Override
					public void disconnect() {
						this.disconnect(null);
					}

					@Override
					public void disconnect(Throwable t) {
						if(t != null) {
							failure.set(t);
						}
	                    session.disconnect();
	                    latch.countDown();
					}
            	});
            }
        };

        this.stompClient.connect(uri, headers, handler);
        
        if (latch.await(3, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                throw new AssertionError("", failure.get());
            }
        }else {
            throw new AssertionError("Message not received");
        }
	}
	
}
