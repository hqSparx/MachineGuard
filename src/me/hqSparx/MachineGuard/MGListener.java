package me.hqSparx.MachineGuard;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class MGListener implements Listener {

public static MachineGuard plugin;
public WorldGuardPlugin wg;

	public MGListener(MachineGuard instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent event)
	{
		if(!event.hasBlock())return;
	int id = event.getClickedBlock().getTypeId();
	
	if(plugin.isBlocked(id)) {
		if(!wg.canBuild(event.getPlayer(), event.getClickedBlock().getLocation())) {
		event.getPlayer().sendMessage(plugin.msg);
		event.setCancelled(true);
			}
		}
	if(id == 137 && event.getClickedBlock().getData() == (byte)3){
		event.getPlayer().sendMessage("Tymczasowo wylaczone");
		event.setCancelled(true);	
	}
	
	
	}
	

	
	
	
	
}
