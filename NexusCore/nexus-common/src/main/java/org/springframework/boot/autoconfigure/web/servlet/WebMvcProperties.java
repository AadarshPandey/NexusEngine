package org.springframework.boot.autoconfigure.web.servlet;

public class WebMvcProperties {
    public String getServletPath() { return "/"; }
    public String getWebjarsPathPattern() { return "/webjars/**"; }
    public String getStaticPathPattern() { return "/**"; }
}
