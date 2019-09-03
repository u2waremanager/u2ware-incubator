package io.github.u2ware.crawling.core;

public class Content {
	 
	public static enum Type{
		SEED,
		TEXT,
		FILE,
		IMAGE,
	}
	
	
	private Object group;
	private Type type;
	private Object identify;
	private Object content;

	public Object getGroup() {
		return group;
	}

	public void setGroup(Object group) {
		this.group = group;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Object getIdentify() {
		return identify;
	}

	public void setIdentify(Object identify) {
		this.identify = identify;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Content [group=" + group + ", type=" + type + ", identify=" + identify + ", content=" + content+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identify == null) ? 0 : identify.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Content other = (Content) obj;
		if (identify == null) {
			if (other.identify != null)
				return false;
		} else if (!identify.equals(other.identify))
			return false;
		return true;
	}
}
