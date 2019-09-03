package io.github.u2ware.crawling.core;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class school_gyo6_net extends DefaultParser{

	@Override
	protected String resolveSeedAnchor(HtmlAnchor a, HtmlPage page){
		if("#contents".equals(a.getAttribute("href")) && a.hasAttribute("border")){
			return "http://school.gyo6.net/sanbuk/80119/board/44088/"+getJavascriptArgs(a.toString()).get(1);
		}
		return null;
	}
}