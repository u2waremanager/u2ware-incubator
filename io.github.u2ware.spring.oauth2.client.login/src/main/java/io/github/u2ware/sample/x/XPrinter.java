package io.github.u2ware.sample.x;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

public class XPrinter {

    protected static Log logger = LogFactory.getLog(XPrinter.class);

    public static void print(String flag, HttpServletRequest request){
        logger.info(flag + request.hashCode());
        logger.info(flag + request.getSession().hashCode());
        logger.info(flag + request.getSession().getAttribute("callback_uri"));
        logger.info(flag + request.getRequestURL());
        logger.info(flag + request.getParameter("callback_uri"));
        logger.info(flag + request.getParameter(OAuth2ParameterNames.STATE));
    }

}