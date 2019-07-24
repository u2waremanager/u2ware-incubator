package io.github.u2ware.spring.websocket;

public class WebSocketMessage {

    public static final String WS_URL = "/ws";
    public static final String WS_PUBLISH_URL = "/queue/";
    public static final String WS_SUBSCRIBE_URL = "/topic/";
	
	private String room;
	private String sender;
	private String type;
	private String content;
	private Object escape;

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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Object getEscape() {
		return escape;
	}
	public void setEscape(Object escape) {
		this.escape = escape;
	}
	@Override
	public String toString() {
		return "WebSocketMessage [room=" + room + ", sender=" + sender + ", type=" + type + ", escape=" + escape + "]";
	}
}