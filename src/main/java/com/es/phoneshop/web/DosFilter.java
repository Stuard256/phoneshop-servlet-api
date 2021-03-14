package com.es.phoneshop.web;

import com.es.phoneshop.security.DefaultDosProtectionService;
import com.es.phoneshop.security.DosProtectionService;
import org.jetbrains.annotations.Async;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DosFilter implements Filter {
    private DosProtectionService service;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        service = DefaultDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(service.isAllowed(request.getRemoteAddr())){
            chain.doFilter(request,response);
        }
        else{
            ((HttpServletResponse)response).setStatus(429);
        }
    }

    @Override
    public void destroy() {

    }
}
