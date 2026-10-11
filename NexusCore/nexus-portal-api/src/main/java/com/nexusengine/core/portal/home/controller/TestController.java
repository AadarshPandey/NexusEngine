package com.nexusengine.core.portal.home.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.portal.home.service.HomeService;


@RestController
@RequestMapping("/portal/home")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);

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
        } catch (Exception e) {
            log.error("Test SOE endpoint failed", e);
            return "Failed: " + e.getMessage();
        }
    }
}
