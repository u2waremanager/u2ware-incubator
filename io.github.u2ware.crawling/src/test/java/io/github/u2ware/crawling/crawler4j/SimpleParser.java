package io.github.u2ware.crawling.crawler4j;

public interface SimpleParser {

    public void onStarted(String url);
    
    public void onCrawled(String url, String contents, int index);
    
    public void onFinished(String url);
	
}
