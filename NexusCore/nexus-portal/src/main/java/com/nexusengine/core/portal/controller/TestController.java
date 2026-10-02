package com.nexusengine.core.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.portal.service.HomeService;
import java.io.PrintWriter;
import java.io.StringWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/portal/home")
public class TestController {
    private final HomeService homeService;
    private final ObjectMapper objectMapper;

    public TestController(HomeService homeService, ObjectMapper objectMapper) {
        this.homeService = homeService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/testSOE")
    public String testSoe() {
        try {
            Object result = homeService.recommendProductList(8, 1);
            return objectMapper.writeValueAsString(result);
        } catch (Throwable t) {
            try {
                java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("/tmp/soe.log"));
                t.printStackTrace(pw);
                pw.close();
            } catch (Exception e) {}
            return "Failed, check /tmp/soe.log";
        }
    }
}
