package com.example.demo.juso;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "juso", url="${feign.client.config.juso.url}")
public interface JusoClient {


    @Value("${feign.client.config.juso.resultType:}")
    public String resultType = "json";

    @Value("${feign.client.config.juso.confmKey:}")
    public String confmKey = "TESTJUSOGOKR";

    // public String resultType = "json";
    // public String confmKey = "TESTJUSOGOKR";
    

    @PostMapping(value="")
    // @PostMapping(value="/addrlink/addrLinkApiJsonp.do")
    public String addrLinkApiJsonp(
        @RequestParam(value="keyword")String  keyword,
        @RequestParam(value="resultType")String  resultType,
        @RequestParam(value="confmKey")String  confmKey
    );
}