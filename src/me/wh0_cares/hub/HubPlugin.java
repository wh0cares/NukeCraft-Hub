package me.wh0_cares.hub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HubPlugin extends JavaPlugin implements Listener {
	
    public static Inventory HubInv;

	@Override
    public void onEnable() {
		HubInv = Bukkit.createInventory(null, 45, "Server Menu");
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
        player.getInventory().clear();
        player.getInventory().setItem(4, new ItemStack(Material.COMPASS));
        player.updateInventory();
    }
    
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onInteract(PlayerInteractEvent event){
    	Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK){
        	if(!player.isOp()){
            	event.setCancelled(true);
        	}
        	if(player.getItemInHand().getTypeId() == Material.COMPASS.getId()){
        		event.setCancelled(true);
        		openHubInv(player);
        	}
        }else if (event.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand().getTypeId() == Material.COMPASS.getId() || event.getAction() == Action.LEFT_CLICK_AIR && player.getItemInHand().getTypeId() == Material.COMPASS.getId()){
    		openHubInv(player);
        }
    }

	@SuppressWarnings("deprecation")
	public void openHubInv(Player player) {
		player.openInventory(HubInv);
        HubInv.clear();
        ItemStack glassItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
        ItemMeta glassItemMeta= glassItem.getItemMeta();
        glassItemMeta.setDisplayName(" ");
        glassItem.setItemMeta(glassItemMeta);
        for(int x = 0 ; x < 45; x++){
            HubInv.setItem(x, glassItem);
        }
        ItemStack placeItem = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData());
        ItemMeta placeItemMeta = placeItem.getItemMeta();
        placeItemMeta.setDisplayName("Reddit Place");
        placeItem.setItemMeta(placeItemMeta);
        HubInv.setItem(13, placeItem);
	}
	
	@EventHandler
    public void onClickSlot(InventoryClickEvent event) {
		if(event.getView().getTopInventory().getName().equals("Server Menu")){
        	if(event.getSlot() == 13){
        		HumanEntity player = event.getWhoClicked();
        		Location location = new Location(player.getWorld(), 56.417, 67, 78.841);
                player.teleport(location);
        	}
        }
        //event.setCancelled(true);
    }
	
}
