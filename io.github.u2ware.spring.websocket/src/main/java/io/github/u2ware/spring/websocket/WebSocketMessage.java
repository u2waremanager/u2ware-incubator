package io.github.u2ware.spring.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebSocketMessage {

    public static final String WS_URL = "/ws";
    public static final String WS_PUBLISH_URL = "/queue/";
    public static final String WS_SUBSCRIBE_URL = "/topic/";
    public static final String CLOSED_TYPE = "LEAVE";
	
	private UUID id;
	private String room;
	private String sender;
	private String contentType;
	private String content;
	private Map<String,Object> api = new HashMap<>();
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<String, Object> getApi() {
		return api;
	}
	public void setApi(Map<String, Object> api) {
		this.api = api;
	}
	@Override
	public String toString() {
		return "[id=" + id + ", room=" + room + ", sender=" + sender + ", contentType=" + contentType
				+ ", content=" + content + ", api=" + api + "]";
	}
}