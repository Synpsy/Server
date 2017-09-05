package net.plugin.log;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.bukkit.Bukkit;

import net.file.FileManager;
 
public class PluginLogger {

	private static String DataFolder; 
	
    public static void loadClass(String DataFolder) {
        PluginLogger.DataFolder = DataFolder; fileLogger();
    }

    public static void log(Class<?> Class, String Message) {
        FileManager.addObject(fileLogger(), timeStamp(), Message);
        Bukkit.getConsoleSender().sendMessage(timeStamp() + " ("+ PluginLogger.class.getSimpleName()+") logged: " + Message);
    }

    public static void log(Class<?> Class, Throwable throwable, Integer Line) {
        FileManager.addObject(fileLogger(), timeStamp(), throwable.getClass().getSimpleName() + " [Line: "+Line+"]");
        Bukkit.getConsoleSender().sendMessage(timeStamp() + " ("+ PluginLogger.class.getSimpleName()+") logged: " + throwable.getClass().getSimpleName() + " [Line: "+Line+"]");
    }

    public static void log(Class<?> Class, Throwable throwable, Integer fromLine, Integer toLine) {
        FileManager.addObject(fileLogger(), timeStamp(), throwable.getClass().getSimpleName() + " [Line: "+fromLine+"-"+toLine+"]");
        Bukkit.getConsoleSender().sendMessage(timeStamp() + " ("+ PluginLogger.class.getSimpleName()+") logged: " + throwable.getClass().getSimpleName() + " [Line: "+fromLine+"-"+toLine+"]");
    }

    /*  */

    private static String timeStamp() {
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        return ts.toString();
    }


    /*  */

    private static File fileLogger() {
        return FileManager.CreateFile("plugins/" + DataFolder + "/Logs", "LogFile", "yml");
    }

}
