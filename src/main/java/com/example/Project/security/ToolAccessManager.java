package com.example.Project.security;


import com.example.Project.model.Tool;
import com.example.Project.service.IToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ToolAccessManager {

    @Autowired
    private IToolService toolService;

    public boolean canAccessTool(String endpoint, Authentication auth) {
        Tool tool = toolService.getToolByEndpoint(endpoint);
        if (tool == null) return false;

        if (!tool.getIsPremium()) {
            return true; // tool không premium → ai cũng dùng được
        }

        // tool premium → cần role phù hợp
        return auth.getAuthorities().stream().anyMatch(granted ->
                granted.getAuthority().equals("ROLE_PREMIUM") ||
                        granted.getAuthority().equals("ROLE_ADMIN")
        );
    }
}
