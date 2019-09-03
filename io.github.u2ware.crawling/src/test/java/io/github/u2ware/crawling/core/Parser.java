package io.github.u2ware.crawling.core;

import java.util.Collection;

public interface Parser<T> {

	public void onInit(String rootSeed, String subSeed);
	
	public void onStart(Collection<Content> contents, String seed, T source);

	public void onVisit(Collection<Content> contents, String anchor, T source);
	
	public void onFinish(Collection<Content> contents, String seed);
	
	public void onDestroy(String rootSeed, String subSeed);

}
