package com.example.demo.juso;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@BasePathAwareController
public class JusoController {


	protected Log logger = LogFactory.getLog(getClass());

    private @Autowired JusoClient jusoClient;
    private @Autowired ObjectMapper objectMapper;

    @GetMapping("/juso")
    @ResponseBody
    public Map<String,Object> findBy(@RequestParam("keyword") String keyword) throws Exception{
        String context = jusoClient.addrLinkApiJsonp(keyword, JusoClient.resultType, JusoClient.confmKey);
        context = context.substring(1, context.length()-1);
        return objectMapper.readValue(context, Map.class);
    }
}