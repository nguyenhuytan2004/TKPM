package com.example.Project.plugin;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.Project.shared.util.StringHelper;
import com.example.plugin.IToolPlugin;

import jakarta.annotation.PostConstruct;

@Service
public class PluginManager {

    @Autowired
    private PluginLoader pluginLoader;

    @Value("${plugins.directory:plugins}")
    private String pluginsDirectory;

    private final Map<String, IToolPlugin> activePlugins = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        loadPlugins();
    }

    // @Scheduled(fixedDelay = 10000) // Kiểm tra plugins mới mỗi 10 giây
    // public void checkForNewPlugins() {
    // System.out.println("Checking for new plugins...");

    // File dir = new File(pluginsDirectory);
    // if (!dir.exists() || !dir.isDirectory())
    // return;

    // File[] jarFiles = dir.listFiles((d, name) -> name.endsWith(".jar"));
    // if (jarFiles == null)
    // return;

    // boolean hasChanges = false;

    // for (File file : jarFiles) {
    // String filePath = file.getAbsolutePath();
    // long lastModified = file.lastModified();

    // if (!pluginLastModified.containsKey(filePath) ||
    // pluginLastModified.get(filePath) < lastModified) {
    // hasChanges = true;
    // pluginLastModified.put(filePath, lastModified);
    // }
    // }

    // if (hasChanges) {
    // loadPlugins();
    // }
    // }

    public synchronized void loadPlugins() {
        activePlugins.clear();

        // Tải plugins mới
        List<IToolPlugin> newPlugins = pluginLoader.loadPlugins(pluginsDirectory);

        for (IToolPlugin plugin : newPlugins) {
            String pluginName = plugin.getName();
            activePlugins.put(pluginName, plugin);
        }

        System.out.println("Loaded plugins: " + activePlugins.keySet());
    }

    public synchronized void deletePlugin(String toolName) {

        // Xóa plugin
        IToolPlugin plugin = activePlugins.remove(toolName);
        if (plugin != null) {
            File pluginFile = new File(pluginsDirectory, StringHelper.toPascalCase(toolName) + "Plugin-1.0.0.jar");
            if (pluginFile.exists() && pluginFile.isFile()) {
                if (!pluginFile.delete())
                    throw new RuntimeException("Failed to delete plugin file: " + pluginFile.getAbsolutePath());
            }
        }

        // Xóa template UI
        File templateFile = new File("src/main/resources/templates/tools-ui/" + toolName + ".hbs");
        if (templateFile.exists() && templateFile.isFile()) {
            if (!templateFile.delete())
                throw new RuntimeException("Failed to delete template file: " + templateFile.getAbsolutePath());
        }
    }

    public IToolPlugin getPlugin(String name) {
        return activePlugins.get(name);
    }

    public Map<String, IToolPlugin> getAllPlugins() {
        return activePlugins;
    }
}