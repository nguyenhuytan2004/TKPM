package com.example.Project.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Project.model.Tool;
import com.example.Project.plugin.PluginManager;
import com.example.Project.service.ICategoryService;
import com.example.Project.service.IToolService;
import com.example.Project.shared.util.StringHelper;

@Controller
@RequestMapping("")
public class ToolController {
    @Autowired
    private IToolService _toolService;

    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private PluginManager pluginManager;

    @RestController
    @RequestMapping("/api")
    public class ToolApiController {
        @GetMapping("/tools")
        public List<Tool> searchTool(@RequestParam String hintText) {
            return _toolService.getToolsByName(hintText);
        }

        @PutMapping("/tool/{id}/premium")
        public boolean updateToolPremiumStatus(@PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
            Tool tool = _toolService.getToolById(id);
            if (tool != null) {
                tool.setPremium(requestBody.get("isPremium"));
                _toolService.updateTool(tool);
                return true;
            }
            return false;
        }

        @PutMapping("/tool/{id}/active")
        public boolean updateToolActiveStatus(@PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
            Tool tool = _toolService.getToolById(id);
            if (tool != null) {
                tool.setActive(requestBody.get("isActive"));
                _toolService.updateTool(tool);
                return true;
            }
            return false;
        }

        @PostMapping("/tool")
        public boolean addTool(
                @RequestParam("name") String name,
                @RequestParam("description") String description,
                @RequestParam("endpoint") String endpoint,
                @RequestParam("category_id") Integer categoryId,
                @RequestParam("is_active") Boolean isActive,
                @RequestParam("is_premium") Boolean isPremium,
                @RequestParam(value = "jar_file", required = false) MultipartFile jarFile) {
            try {
                Tool existingTool = _toolService.getToolByName(name);
                if (existingTool != null) {
                    return false;
                }

                Tool tool = new Tool();
                tool.setName(name);
                tool.setDescription(description);
                tool.setEndpoint(endpoint);
                tool.setCategory(_categoryService.getCategoryById(categoryId));
                tool.setActive(isActive);
                tool.setPremium(isPremium);

                if (jarFile != null && !jarFile.isEmpty()) {
                    String pluginsDir = new File("plugins").getAbsolutePath();
                    File dir = new File(pluginsDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File destFile = new File(dir, jarFile.getOriginalFilename());
                    jarFile.transferTo(destFile);

                    // Kích hoạt tải lại plugin
                    pluginManager.loadPlugins();
                    _toolService.updateTool(tool);
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        @GetMapping("/tool/{id}")
        public Tool getToolById(@PathVariable int id) {
            return _toolService.getToolById(id);
        }

        @PutMapping("/tool/{id}")
        public boolean editTool(@PathVariable int id, @RequestBody Map<String, Object> requestBody) {
            try {
                Tool tool = _toolService.getToolById(id);
                if (tool != null) {
                    tool.setName((String) requestBody.get("name"));
                    tool.setDescription((String) requestBody.get("description"));
                    tool.setEndpoint((String) requestBody.get("endpoint"));
                    tool.setCategory(_categoryService.getCategoryById((Integer) requestBody.get("category_id")));
                    tool.setActive((Boolean) requestBody.get("is_active"));
                    tool.setPremium((Boolean) requestBody.get("is_premium"));
                    _toolService.updateTool(tool);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            return false;
        }

        @DeleteMapping("/tool/{id}")
        public boolean deleteTool(@PathVariable int id) {
            try {
                Tool tool = _toolService.getToolById(id);
                if (tool != null) {
                    pluginManager.deletePlugin(StringHelper.toKebabCase(tool.getName()));
                    _toolService.deleteTool(tool);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            return false;
        }

        @GetMapping("/tools-per-category")
        public Map<Integer, List<Tool>> getToolsPerCategory() {
            return _toolService.getToolsPerCategory();
        }
    }
}
