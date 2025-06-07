package com.multitenant.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class TenantInterceptor implements HandlerInterceptor {
    private EventContext eventContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId == null || !tenantId.matches("^[12]$")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Tenant ID. Only '1' or '2' allowed.");
            return false;
        }
        EventContext.setTenantId(tenantId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        EventContext.clear();
    }

    public TenantInterceptor(@Autowired EventContext aEventContext) {
        this.eventContext = aEventContext;
    }

}
