package me.wh0_cares.hub;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HubPlugin extends JavaPlugin implements Listener {
		
	@Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }
	
    @Override
    public void onDisable() {
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = new Location(player.getWorld(), 0.5, 81, 0.5);
        player.teleport(location);
    }
    
	@EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK){
        	event.setCancelled(true);
        }
    }
}
