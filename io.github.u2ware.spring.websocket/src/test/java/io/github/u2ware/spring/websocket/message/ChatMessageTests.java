package io.github.u2ware.spring.websocket.message;

import static io.github.u2ware.spring.websocket.ApplicationMockMvc.ApplicationResultActions.sizeMatch;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.u2ware.spring.websocket.ApplicationTests;


public class ChatMessageTests extends ApplicationTests{

	
	@Test
	public void contextLoads2() throws Exception{
		
		$.PUT("/chat/abcd").C("contentType", "JOIN").C("sender", "u1").is2xx();
		$.PUT("/chat/abcd").C("contentType", "JOIN").C("sender", "u2").is2xx();
		$.PUT("/chat/abcd").C("contentType", "JOIN").C("sender", "u3").is2xx();
		$.GET("/chat/abcd").P("sort", "timestamp").is2xx(sizeMatch(3));


		$.PUT("/chat/abcd").C("contentType", "DISCONNECT").C("sender", "u2").is2xx();
		$.GET("/chat/abcd").P("sort", "timestamp").is2xx(sizeMatch(4));
		
		
		$.PUT("/chat/abcd").C("contentType", "INVITE").C("sender", "u1").C("content", "u2,u3,u4,u5").is2xx();
		$.PUT("/chat/abcd").C("contentType", "INVITE").C("sender", "u1").C("content", "u2,u3,u4,u7").is2xx();
		$.PUT("/chat/abcd").C("contentType", "INVITE").C("sender", "u7").C("content", "u2,u3,u4,u7").is4xx();
		$.GET("/chat/abcd").P("sort", "timestamp").is2xx(sizeMatch(6));
		
		
		$.PUT("/chat/abcd").C("contentType", "LEAVE").C("sender", "u3").is2xx();
		$.PUT("/chat/abcd").C("contentType", "LEAVE").C("sender", "u4").is4xx();
		$.PUT("/chat/abcd").C("contentType", "LEAVE").C("sender", "u7").is4xx();
		$.GET("/chat/abcd").P("sort", "timestamp").is2xx(sizeMatch(7));
		
	}
	
	
	@Test
	public void contextLoads1() throws Exception{

		$.POST("/chat").C("sender", "u1").C("content", "u2,u3").is2xx("result");
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(1));

		$.PUT($.path("result.chatUri")).C("contentType", "JOIN").C("sender", "u7").is4xx();
		$.PUT($.path("result.chatUri")).C("contentType", "JOIN").C("sender", "u1").is2xx();
		$.PUT($.path("result.chatUri")).C("contentType", "JOIN").C("sender", "u1").is2xx();
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(3));
		
		$.PUT($.path("result.chatUri")).C("contentType", "INVITE").C("sender", "u1").C("content", "u2,u3,u4,u5").is2xx();
		$.PUT($.path("result.chatUri")).C("contentType", "INVITE").C("sender", "u1").C("content", "u2,u3,u4,u7").is2xx();
		$.PUT($.path("result.chatUri")).C("contentType", "INVITE").C("sender", "u2").C("content", "u2,u3,u4,u7").is4xx();
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(5));
		
		
		
		$.PUT($.path("result.chatUri")).C("contentType", "JOIN").C("sender", "u4").is2xx();
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(6));


		$.PUT($.path("result.chatUri")).C("contentType", "LEAVE").C("sender", "u4").is2xx();
		$.PUT($.path("result.chatUri")).C("contentType", "LEAVE").C("sender", "u4").is4xx();
		$.PUT($.path("result.chatUri")).C("contentType", "LEAVE").C("sender", "u7").is4xx();
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(7));
		
		
		$.PUT($.path("result.chatUri")).C("contentType", "JOIN").C("sender", "u2").is2xx();
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(8));
		
		$.PUT($.path("result.chatUri")).C("contentType", "CHAT").C("sender", "u1").C("content", "hello~").is2xx("m1");
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(9));
		
		
		UriComponents u = UriComponentsBuilder.fromPath($.path("m1._links.self.href")).build();
		String id = u.getPathSegments().get(u.getPathSegments().size() -1);
		logger.info(id);
		
		$.PUT($.path("result.chatUri")).C("contentType", "READ").C("sender", "u2").C("content", id).is2xx();
		$.PUT($.path("result.chatUri")).C("contentType", "READ").C("sender", "u7").C("content", id).is4xx();
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(9));
		
	
		$.PUT($.path("result.chatUri")).C("contentType", "LEAVE").C("sender", "u7").is4xx();
		$.PUT($.path("result.chatUri")).C("contentType", "LEAVE").C("sender", "u2").is2xx();
		$.PUT($.path("result.chatUri")).C("contentType", "LEAVE").C("sender", "u1").is2xx();
		$.GET($.path("result.chatUri")).P("sort", "timestamp").is2xx(sizeMatch(11));
	}
}
