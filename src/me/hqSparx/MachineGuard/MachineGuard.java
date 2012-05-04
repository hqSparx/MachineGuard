package me.hqSparx.MachineGuard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;




public class MachineGuard extends JavaPlugin {
	public static MachineGuard plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final MGListener listener = new MGListener(this);
	public static List<Integer> blocked = new ArrayList<Integer>(1024);
	private List<Integer> iclist = new ArrayList<Integer>(1024);
	private List<Integer> bclist = new ArrayList<Integer>(1024);
	private List<Integer> rplist = new ArrayList<Integer>(1024);
	private List<Integer> customlist = new ArrayList<Integer>(1024);
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
		pm.registerEvents(listener, this);
		Plugin worldGuard = pm.getPlugin("WorldGuard");
		listener.wg = (WorldGuardPlugin)worldGuard; 
		try {
			loadConfiguration();
			updateLists();
		} catch (IOException e) {
			logger.info("[MachineGuard] Can't load config!");
		}
		
	}

	public void onReload() {
		try {
			loadConfiguration();
			updateLists();
		} catch (IOException e) {
			logger.info("[MachineGuard] Can't load config!");
		}
	}

	public void loadConfiguration() throws IOException {
		File cfgFile = new File(this.getDataFolder() + "/config.yml");
		Integer[] temp;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(cfgFile);
		config.addDefault("deny-ic-interaction", true);
		temp = new Integer[] {218, 227, 233, 243, 246, 250};
		config.addDefault("ic-list", Arrays.asList(temp));
		config.addDefault("deny-bc-interaction", true);
		temp = new Integer[] {152, 155, 157, 161, 165, 166};
		config.addDefault("bc-list", Arrays.asList(temp));
		config.addDefault("deny-rp-interaction", true);
		temp = new Integer[] {137, 138, 251};
		config.addDefault("rp-list", Arrays.asList(temp));
		temp = new Integer[] {};
		config.addDefault("custom-list", Arrays.asList(temp));
		config.addDefault("deny-lever", true);
		config.addDefault("msg", "&cSorry, I can't let you do that.");
		config.options().copyDefaults(true);
		config.save(cfgFile);
		lever = config.getBoolean("deny-lever");
		blockic = config.getBoolean("deny-ic-interaction");
		iclist = config.getIntegerList("ic-list");
		blockbc = config.getBoolean("deny-bc-interaction");
		bclist = config.getIntegerList("bc-list");
		blockrp = config.getBoolean("deny-rp-interaction");
		rplist = config.getIntegerList("rp-list");
		customlist = config.getIntegerList("custom-list");
		msg = config.getString("msg"); 
		msg = msg.replaceAll("&([0-9a-f])", "\u00A7$1");
	}

	public void updateLists() {
		blocked.clear();
		if(blockic) blocked.addAll(iclist);
		if(blockbc) blocked.addAll(bclist);
		if(blockrp) blocked.addAll(rplist);
		if(lever) blocked.add(69);
		blocked.addAll(customlist);
	}

	public boolean isBlocked(int id) {
		return blocked.contains(String.valueOf(id));
	}
}