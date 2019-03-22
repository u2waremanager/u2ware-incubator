package com.u2ware.elasticsearch.book;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BookDocument {

	public final static String INDEX = "test-index-book";
	public final static String TYPE = "book";
	
	private String index = "";
	private String type = "";
	
	public String getIndex() {
		return INDEX + (StringUtils.isEmpty(index) ? "" : "-"+index);
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return TYPE+type;
	}
	public void setType(String type) {
		this.type = type;
	}
}