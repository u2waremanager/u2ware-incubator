package io.github.u2ware.crawling.core;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.u2ware.crawling.core.Content.Type;

public class DefaultParser implements Parser<HtmlPage> {

	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void onInit(String rootSeed, String subSeed) {
		onInitWithDiff(rootSeed, subSeed);
	}

	@Override
	public void onStart(Collection<Content> contents, String seed, HtmlPage source) {
		onStartWithDiff(contents, seed, source);
	}

	@Override
	public void onVisit(Collection<Content> contents, String anchor, HtmlPage source) {
		onVisitWithDiff(contents, anchor, source);
	}

	@Override
	public void onFinish(Collection<Content> contents, String seed) {
		onFinishWithDiff(contents, seed);
	}
	@Override
	public void onDestroy(String rootSeed, String subSeed) {
		
	}


	//////////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////////
	private String subSeed;
	private Collection<Collection<Content>> differents= new ArrayList<>();
	private HashSet<Content> similar;
	
	public void onInitWithDiff(String rootSeed, String subSeed) {
		this.subSeed = subSeed;
	}

	protected void onStartWithDiff(Collection<Content> contents, String seed, HtmlPage source) {
		
		AtomicInteger seedCount = new AtomicInteger(0);
		
		Content c1 = new Content();
		c1.setGroup(0);
		c1.setType(Type.SEED);
		c1.setIdentify(seedCount.get());
		c1.setContent(seed);
		contents.add(c1);
		
		source.getAnchors().forEach(a->{
			String anchor = resolveSeedAnchor(a, source);
			if(StringUtils.hasLength(anchor)) {
				Content c2 = new Content();
				c2.setGroup(0);
				c2.setType(Type.SEED);
				c2.setIdentify(seedCount.addAndGet(1));
				c2.setContent(anchor);
				
				contents.add(c2);
			}
		});
	}

	
	
	protected void onVisitWithDiff(Collection<Content> contents, String anchor, HtmlPage source) {
		List<Content> different = new ArrayList<>();
		
		appendText(different, source.getDocumentElement(), source);
		
		source.getDocumentElement().querySelectorAll("a").forEach(n->{
			appendFile(different, n, source);
		});
		
		
		differents.add(different);
		if(similar == null) {
			similar = new HashSet<Content>(different);
		}else {
			similar.retainAll(different);
		}
	}
	
	protected void appendText(Collection<Content> different, DomNode parent, HtmlPage source) {
		
		parent.getChildNodes().forEach(n->{
			if(! n.hasChildNodes() && n.getNodeType() == 3) {
				appendText(different, n);

			}else {
				if("a".equalsIgnoreCase(n.getNodeName())) { 
					
				}else if("td".equalsIgnoreCase(n.getNodeName())) {
					appendText(different, n);
					
				}else if("th".equalsIgnoreCase(n.getNodeName())) {
					appendText(different, n);
					
				}else if("img".equalsIgnoreCase(n.getNodeName())) {
					
				}else if("script".equalsIgnoreCase(n.getNodeName())) {
					
				}else {
					appendText(different, n, source);
				}
			}
		});
	}
	
	protected void appendText(Collection<Content> different, DomNode node) {
		String text = StringUtils.trimWhitespace(node.asText());
		Content c3 = new Content();
		c3.setType(Type.TEXT);
		c3.setIdentify(text);
		different.add(c3);
	}
	
	protected void appendFile(Collection<Content> different, DomNode node, HtmlPage source) {
		
		HtmlAnchor ele = (HtmlAnchor)node;
		String text = StringUtils.trimWhitespace(node.asText());
		String href = this.resolveFileAnchor(ele, source);
		
		if(StringUtils.hasLength(href)) {
			Content c3 = new Content();
			c3.setType(Type.FILE);
			c3.setIdentify(text);
			c3.setContent(href);
			different.add(c3);
		}
	}
	
	
	public void onFinishWithDiff(Collection<Content> contents, String seed) {

		differents.forEach(different->{ });

		AtomicInteger pageIndex = new AtomicInteger(0);
		
		differents.forEach(different->{

			//Index..
			pageIndex.addAndGet(1);
			AtomicInteger textIndex = new AtomicInteger(0);
			
			//Remove duplicate..
			different.removeAll(similar);

			//Add Files..
			different.forEach(content->{
				if(Type.FILE.equals(content.getType())) {
					Content c = new Content();
					c.setGroup(pageIndex.get());
					c.setType(Type.FILE);
					c.setIdentify(content.getContent().toString());
					c.setContent(content.getIdentify());
					
					contents.add(c);
				}
			});
			
			//Add Text..
			different.forEach(content->{
				
				if(Type.TEXT.equals(content.getType())) {
					
					//has file check
					boolean add = true;

					for(Content d : contents) {

						if(d.getGroup().equals(pageIndex.get()) 
						&& Type.FILE.equals(d.getType()) 
						&& content.getIdentify().toString().startsWith(d.getContent().toString())) {
						
							logger.info(d.getContent()+" => "+content.getIdentify());
							add = false;
							break;
						}
					}
				
					if(add) {
						Content c = new Content();
						c.setGroup(pageIndex.get());
						c.setType(Type.TEXT);
						c.setIdentify(textIndex.getAndAdd(1));
						c.setContent(content.getIdentify());
						
						contents.add(c);
					}
				}
			});
		});
		
	}
	

	
	//////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////
	protected String resolveSeedAnchor(HtmlAnchor a, HtmlPage page){
		String href = getHrefAttribute(a, page);
		if(StringUtils.startsWithIgnoreCase(href, subSeed)){
			return href;
		}
		return null;
	}
	protected String resolveFileAnchor(HtmlAnchor a, HtmlPage page){
		String href = getHrefAttribute(a, page);
		if(! StringUtils.startsWithIgnoreCase(href, subSeed)){
			return href;
		}
		return null;
	}
	
	
	//////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////
	protected String getHrefAttribute(HtmlAnchor a, HtmlPage page){
		try {
			if(StringUtils.startsWithIgnoreCase(a.getHrefAttribute(), "mailto:")){
				return "";
			}
			if(a.asText().equals(a.getHrefAttribute())){
				return "";
			}
			return HtmlAnchor.getTargetUrl(a.getHrefAttribute(), page).toString();
		} catch (MalformedURLException e) {
			return "";
		}
	}
	
	
	protected List<String> getJavascriptArgs(String text){
		List<String> args = new ArrayList<>();
		Pattern ptn = Pattern.compile("[']([a-z-A-Z-\\s0-9]*)"); 
		Matcher matcher = ptn.matcher(text); 
		while(matcher.find()) {
			String m = matcher.group(1);
			if(StringUtils.hasText(m)) {
				args.add(m);
			}
		}
		return args;
	}
//	protected String getDownloadFilename(String text){
//		int i = text.indexOf('.');
//		if(i > 1 ) {
//			int j = text.indexOf(' ', i);
//			if(j > 1 ) {
//				return text.substring(0, j);
//			}
//		}
//		return text;
//	}

}