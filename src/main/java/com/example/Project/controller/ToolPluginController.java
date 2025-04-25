package com.example.Project.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.Project.security.ToolAccessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.plugin.PluginManager;
import com.example.Project.shared.util.StringHelper;
import com.example.plugin.IToolPlugin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/plugin")
public class ToolPluginController {

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private ToolAccessManager toolAccessManager;

    @Autowired
    private HomeController homeController;

    @GetMapping("/{categoryName}/{toolName}/**")
    public String showPluginUI(@PathVariable String categoryName,
                               @PathVariable String toolName,
                               Model model,
                               HttpSession session) {
        IToolPlugin plugin = pluginManager.getPlugin(toolName);
        System.out.println(toolName);
        System.out.println(plugin);
        if (plugin != null) {
            String username = (String) session.getAttribute("username");
            Integer userId = (Integer) session.getAttribute("userId");

            if (username != null) model.addAttribute("username", username);
            if (userId != null) model.addAttribute("userId", userId);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String endpoint = "/" + categoryName + "/" + toolName;

            if (!toolAccessManager.canAccessTool(endpoint, auth)) {
                model.addAttribute("showPremiumPopup", true);
                model.addAttribute("price", "$10"); // giá để upgrade to premium
                return homeController.show(model, session);
            }

            model.addAttribute("title", StringHelper.toNormalCase(plugin.getName()));
            model.addAttribute("body", "tools-ui/" + plugin.getName());

            return "layout";
        }
        return "redirect:/";
    }

    @RestController
    @RequestMapping("/api")
    public class ToolPluginApiController {

        @RequestMapping("/{categoryName}/{toolName}/**")
        public Map<String, Object> handlePluginRequest(
                @PathVariable String toolName,
                HttpServletRequest request) {

            IToolPlugin plugin = pluginManager.getPlugin(toolName);
            if (plugin != null) {
                Map<String, Object> params = new HashMap<>();

                // Xử lý các tham số GET
                request.getParameterMap().forEach((key, values) -> {
                    if (values.length > 0) {
                        params.put(key, values[0]);
                    }
                });

                return plugin.handle(params);
            }

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Plugin not found");
            return errorResponse;
        }
    }
}