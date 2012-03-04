package me.hqSparx.MachineGuard;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;


public class MGListener extends PlayerListener {

public static MachineGuard plugin;
public WorldGuardPlugin wg;

	public MGListener(MachineGuard instance) {
		plugin = instance;
	}
	
	@Override
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
	}
	
	
	
	
	
}
