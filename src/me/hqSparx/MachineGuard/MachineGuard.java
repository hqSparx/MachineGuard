package me.hqSparx.MachineGuard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import me.hqSparx.MachineGuard.MachineGuard;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;




public class MachineGuard extends JavaPlugin {
	public static MachineGuard plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final MGListener listener = new MGListener(this);
	
	public static Integer[] ic = {218,227,233,243,246,250};
	public static Integer[] bc = {152,155,157,161,165,166};
	public static Integer[] rp = {137,138,251};
	public static List<Integer> blocked = new ArrayList<Integer>(1024);
	public static List<Integer> custom = new ArrayList<Integer>(1024);
	Boolean blockic = true;
	Boolean blockbc = true;
	Boolean blockrp = true;
	Boolean lever = true;
	String msg = "";

public void onDisable() {
	PluginDescriptionFile pdfFile = this.getDescription();
	this.logger.info(pdfFile.getName() + " is now disabled.");
	}

public void onEnable() {
	PluginManager pm = getServer().getPluginManager();
	pm.registerEvent(Event.Type.PLAYER_INTERACT, this.listener, Event.Priority.Normal, this);
	Plugin worldGuard = pm.getPlugin("WorldGuard");
	listener.wg = (WorldGuardPlugin)worldGuard; 
	try {
		loadIds();
	} catch (IOException e) {}
	try {
		loadConfiguration();
	} catch (IOException e) {
		logger.info("[MachineGuard] Can't load config!");
		}
	updateLists();
}

public void onReload() {
	try {
		loadIds();
	} catch (IOException e) {}
	try {
		loadConfiguration();
	} catch (IOException e) {
		logger.info("[MachineGuard] Can't load config!");
		}
	updateLists();
}

public void loadConfiguration() throws IOException {
	File cfgFile = new File(this.getDataFolder() + "/config.yml");
	YamlConfiguration config = YamlConfiguration.loadConfiguration(cfgFile);
	config.addDefault("deny-ic-interaction", true);
	config.addDefault("deny-bc-interaction", true);
	config.addDefault("deny-rp-interaction", true);
	config.addDefault("deny-lever", true);
	config.addDefault("msg", "&cSorry, I can't let you do that.");
	config.options().copyDefaults(true);
    config.save(cfgFile);
	lever = config.getBoolean("deny-lever");
    blockic = config.getBoolean("deny-ic-interaction");
    blockbc = config.getBoolean("deny-bc-interaction");
    blockrp = config.getBoolean("deny-rp-interaction");
    msg = config.getString("msg"); 
    msg = msg.replaceAll("&([0-9a-f])", "\u00A7$1");
 }

public void updateLists() {
	blocked.clear();
	if(blockic) blocked.addAll(Arrays.asList(ic));
	if(blockbc) blocked.addAll(Arrays.asList(bc));
	if(blockrp) blocked.addAll(Arrays.asList(rp));
	if(lever) blocked.add(69);
	blocked.addAll(custom);
}

public boolean isBlocked(int id) {
	return blocked.contains(id);
}

public void loadIds() throws IOException {
	File idsFile = new File(this.getDataFolder() + "/customids.txt");
	if(!idsFile.exists()) idsFile.createNewFile();

	try {
		BufferedReader input =  new BufferedReader(new FileReader(idsFile));
    	try {
	        String line;
	        while ((line = input.readLine()) != null) {
	        	line = line.trim();
		        if (line.length() > 0) {
		        	custom.add(Integer.parseInt(line));
		        }
		     }
    	 } finally {
    		 input.close();
    	 }
    } catch (Exception e) { logger.info("[MachineGuard] Can't load customids.txt!"); }

}




}