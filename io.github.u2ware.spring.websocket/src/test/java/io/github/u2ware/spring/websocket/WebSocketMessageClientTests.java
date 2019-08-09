package io.github.u2ware.spring.websocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketMessageClientTests {

	protected Log logger = LogFactory.getLog(getClass());

    @LocalServerPort
    private int port;

    
    @Test
    public void contextLoads() throws Exception {

    	String CONNECTION_PATH = "ws://localhost:"+port+"/"+WebSocketMessageBrokerConfiguration.WS_CONNECTION;
    	String SUBSCRIPTIONS_PATH = WebSocketMessageBrokerConfiguration.WS_SUBSCRIPTIONS+"/xxx";
    	String BROADCASTING_PATH = WebSocketMessageBrokerConfiguration.WS_BROADCASTING+"/xxx";
    	
    	WebSocketMessageClient client = new WebSocketMessageClient();
    	client.connect(CONNECTION_PATH, (connection)->{
    		
    		connection.subscriptions(SUBSCRIPTIONS_PATH, (message)->{
    			Throwable tx = null;
                try {
                    Assert.assertEquals("Hello, Spring!", message.getContent());
                } catch (Throwable t) {
                	tx = t;
                }
                connection.disconnect(tx);
    		});
    		
    		connection.broadcasting(BROADCASTING_PATH, ()->{
            	WebSocketMessage message = new WebSocketMessage();
            	message.setContent("Hello, Spring!");
    			return message;
    		});
    	});
    }
}

/*
2019-08-09 14:27:42.654  INFO 3656 --- [           main] o.s.m.s.b.SimpleBrokerMessageHandler     : Starting...
2019-08-09 14:27:42.654  INFO 3656 --- [           main] o.s.m.s.b.SimpleBrokerMessageHandler     : BrokerAvailabilityEvent[available=true, SimpleBrokerMessageHandler [DefaultSubscriptionRegistry[cache[0 destination(s)], registry[0 sessions]]]]
2019-08-09 14:27:42.654  INFO 3656 --- [           main] o.s.m.s.b.SimpleBrokerMessageHandler     : Started.
2019-08-09 14:27:42.687  INFO 3656 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 65476 (http) with context path ''
2019-08-09 14:27:42.687  INFO 3656 --- [           main] c.i.hiclass.chat.WebSocketMessageTests   : Started WebSocketMessageTests in 2.624 seconds (JVM running for 3.236)
2019-08-09 14:27:42.952  INFO 3656 --- [o-auto-1-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-08-09 14:27:42.952  INFO 3656 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-08-09 14:27:42.967  INFO 3656 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 15 ms
2019-08-09 14:27:43.077  INFO 3656 --- [o-auto-1-exec-3] c.i.h.chat.WebSocketMessageEndpoint      : SessionConnectEvent: SessionConnectEvent[GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT, stompCommand=CONNECT, nativeHeaders={heart-beat=[0,0], accept-version=[1.1,1.2]}, simpSessionAttributes={}, simpHeartbeat=[J@25268fa7, simpSessionId=b41741fe9e044b10a4c4f62d72e266bd}]]
2019-08-09 14:27:43.077  INFO 3656 --- [tboundChannel-1] c.i.h.chat.WebSocketMessageEndpoint      : SessionConnectedEvent: SessionConnectedEvent[GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT_ACK, simpConnectMessage=GenericMessage [payload=byte[0], headers={simpMessageType=CONNECT, stompCommand=CONNECT, nativeHeaders={heart-beat=[0,0], accept-version=[1.1,1.2]}, simpSessionAttributes={}, simpHeartbeat=[J@25268fa7, simpSessionId=b41741fe9e044b10a4c4f62d72e266bd}], simpSessionId=b41741fe9e044b10a4c4f62d72e266bd}]]
2019-08-09 14:27:43.077  INFO 3656 --- [lient-AsyncIO-1] c.i.hiclass.chat.WebSocketMessageTests   : afterConnected
2019-08-09 14:27:43.092  INFO 3656 --- [o-auto-1-exec-4] c.i.h.chat.WebSocketMessageEndpoint      : SessionSubscribeEvent: SessionSubscribeEvent[GenericMessage [payload=byte[0], headers={simpMessageType=SUBSCRIBE, stompCommand=SUBSCRIBE, nativeHeaders={destination=[/topic/hello], id=[0]}, simpSessionAttributes={}, simpHeartbeat=[J@4b5c4d37, simpSubscriptionId=0, simpSessionId=b41741fe9e044b10a4c4f62d72e266bd, simpDestination=/topic/hello}]]
2019-08-09 14:27:43.155  INFO 3656 --- [nboundChannel-7] c.i.h.chat.WebSocketMessageEndpoint      : message: hello
2019-08-09 14:27:43.155  INFO 3656 --- [nboundChannel-7] c.i.h.chat.WebSocketMessageEndpoint      : message: hello
2019-08-09 14:27:43.155  INFO 3656 --- [nboundChannel-7] c.i.h.chat.WebSocketMessageEndpoint      : message: hello
2019-08-09 14:27:43.155  INFO 3656 --- [nboundChannel-7] c.i.h.chat.WebSocketMessageEndpoint      : message: hello
2019-08-09 14:27:43.155  INFO 3656 --- [nboundChannel-7] c.i.h.chat.WebSocketMessageEndpoint      : /topic/hello [id=f1912227-11b7-4d07-bba9-334792d6a945, room=hello, sender=null, contentType=null, content=Hello, Spring!, api={}]
2019-08-09 14:27:43.170  INFO 3656 --- [nboundChannel-7] c.i.h.chat.WebSocketMessageEndpoint      : http://devapi.hi-class.io:19081/chatis not found.
2019-08-09 14:27:43.170  INFO 3656 --- [lient-AsyncIO-1] c.i.hiclass.chat.WebSocketMessageTests   : getPayloadType
2019-08-09 14:27:43.170  INFO 3656 --- [lient-AsyncIO-1] c.i.hiclass.chat.WebSocketMessageTests   : handleFrame
2019-08-09 14:27:43.170  INFO 3656 --- [o-auto-1-exec-6] c.i.h.chat.WebSocketMessageEndpoint      : SessionDisconnectEvent: SessionDisconnectEvent[sessionId=b41741fe9e044b10a4c4f62d72e266bd, CloseStatus[code=1000, reason=null]]
2019-08-09 14:27:43.170  INFO 3656 --- [o-auto-1-exec-6] c.i.h.chat.WebSocketMessageEndpoint      : /topic/hello [id=97c0f84c-aac5-433d-adec-9cd6003bb01c, room=hello, sender=null, contentType=DISCONNECT, content=Hello, Spring!, api={}]
2019-08-09 14:27:43.186  INFO 3656 --- [o-auto-1-exec-6] c.i.h.chat.WebSocketMessageEndpoint      : http://devapi.hi-class.io:19081/chatis not found.
2019-08-09 14:27:43.186  INFO 3656 --- [       Thread-3] o.s.m.s.b.SimpleBrokerMessageHandler     : Stopping...
2019-08-09 14:27:43.186  INFO 3656 --- [       Thread-3] o.s.m.s.b.SimpleBrokerMessageHandler     : BrokerAvailabilityEvent[available=false, SimpleBrokerMessageHandler [DefaultSubscriptionRegistry[cache[0 destination(s)], registry[0 sessions]]]]
2019-08-09 14:27:43.186  INFO 3656 --- [       Thread-3] o.s.m.s.b.SimpleBrokerMessageHandler     : Stopped.
*/
