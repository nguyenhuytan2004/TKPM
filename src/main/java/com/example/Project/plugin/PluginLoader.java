package com.example.Project.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.stereotype.Component;

import com.example.plugin.IToolPlugin;

@Component
public class PluginLoader {

    public List<IToolPlugin> loadPlugins(String pluginsPath) {
        List<IToolPlugin> plugins = new ArrayList<>();
        File pluginDir = new File(pluginsPath);

        if (!pluginDir.exists() || !pluginDir.isDirectory()) {
            pluginDir.mkdirs();
            return plugins;
        }

        File[] jarFiles = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null) {
            System.err.println("No jar files found in the plugin directory: " + pluginDir.getAbsolutePath());
            return plugins;
        }

        for (File file : jarFiles) {
            try (JarFile jarFile = new JarFile(file)) {
                URLClassLoader classLoader = URLClassLoader.newInstance(
                        new URL[] { file.toURI().toURL() },
                        getClass().getClassLoader());

                Enumeration<JarEntry> entries = jarFile.entries();
                boolean flag1 = false, flag2 = false;
                while (entries.hasMoreElements() && (!flag1 || !flag2)) {
                    JarEntry entry = entries.nextElement();

                    // Extract .hbs UI template
                    if (entry.getName().startsWith("templates/") && !entry.isDirectory()
                            && entry.getName().endsWith(".hbs")) {
                        try (InputStream input = jarFile.getInputStream(entry)) {
                            String fileName = entry.getName().replace("templates/", "");
                            File targetFile = new File("src/main/resources/templates/tools-ui/" + fileName);
                            targetFile.getParentFile().mkdirs();
                            Files.copy(input, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            flag1 = true;

                            System.out.println("Extracted template: " + targetFile.getPath());
                        } catch (IOException ioEx) {
                            System.err.println(
                                    "Failed to extract template: " + entry.getName() + " - " + ioEx.getMessage());
                        }
                    }

                    // Load plugin class
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName().replace("/", ".").replace(".class", "");
                        try {
                            Class<?> clazz = classLoader.loadClass(className);

                            System.out.println("Đang kiểm tra class: " + className);
                            System.out.println("ClassLoader của plugin: " + clazz.getClassLoader());
                            System.out.println("ClassLoader của IToolPlugin: " + IToolPlugin.class.getClassLoader());
                            System.out.println("Có assignable không: " + IToolPlugin.class.isAssignableFrom(clazz));

                            if (IToolPlugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                                IToolPlugin plugin = (IToolPlugin) clazz.getDeclaredConstructor().newInstance();
                                plugins.add(plugin);

                                flag2 = true;

                                System.out.println("Loaded plugin: " + className);
                            }
                        } catch (Exception e) {
                            System.err.println("Error loading class: " + className + ": " + e.getMessage());
                        }
                    }
                }
                classLoader.close();
            } catch (Exception e) {
                System.err.println("Error loading plugin from " + file.getName() + ": " + e.getMessage());
            }
        }

        return plugins;
    }

}