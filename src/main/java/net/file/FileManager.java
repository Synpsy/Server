package net.file;

import java.io.File; 
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.plugin.log.PluginLogger;

public class FileManager {
 
    public static File CreateFile(String Path, String fileName, String fileType) {
        return new File(Path, fileName + "." + fileType);
    } 

    public static Boolean addDefault(File file, String fileQuery, String fileValue) {
        try {
            FileConfiguration Configuration = YamlConfiguration.loadConfiguration(file);

            Configuration.options().copyDefaults(true);
            Configuration.addDefault(fileQuery, fileValue);
            Configuration.save(file);
            return true;
        } catch (IOException e) {
            PluginLogger.log(FileManager.class, e, 25, 29);
        }
        return false;
    }

    public static Boolean addObject(File file, String fileQuery, String fileValue) {
        try {
            FileConfiguration Configuration = YamlConfiguration.loadConfiguration(file);

            Configuration.set(fileQuery, fileValue);
            Configuration.save(file);
            return true;
        } catch (IOException e) {
            PluginLogger.log(FileManager.class, e, 39, 42);
        }
        return false;
    }

    public static Object copyObject(File file, String fileQuery) {
        return YamlConfiguration.loadConfiguration(file).get(fileQuery);
    }

}