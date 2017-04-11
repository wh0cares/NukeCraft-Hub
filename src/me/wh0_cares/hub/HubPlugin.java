package me.wh0_cares.hub;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HubPlugin extends JavaPlugin implements Listener {
	
    public static Inventory HubInv;
	Location loc1, loc2;
	
	@Override
    public void onEnable() {
		HubInv = Bukkit.createInventory(null, 45, "Server Menu");
        getServer().getPluginManager().registerEvents(this, this);
        createConfig();
    }
	
    @Override
    public void onDisable() {
    }
    
    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Creating config!");
                saveDefaultConfig();
            } else {
                getLogger().info("Loading config!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = new Location(player.getWorld(), 0.5, 81, 0.5);
        player.teleport(location);
        player.getInventory().clear();
        ItemStack compassItem = new ItemStack(Material.COMPASS);
        ItemMeta compassItemMeta= compassItem.getItemMeta();
        compassItemMeta.setDisplayName(ChatColor.AQUA + "Server Menu" + ChatColor.WHITE + " (Click)");
        compassItem.setItemMeta(compassItemMeta);
        player.getInventory().setItem(4, compassItem);
        player.updateInventory();
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        int id = event.getBlock().getTypeId();
        if(id == 8 || id == 9) {
            event.setCancelled(true);
        }
    }
    
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onInteract(PlayerInteractEvent event){
    	Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK){
        	if(!player.isOp()){
            	event.setCancelled(true);
        	}else if(player.getItemInHand().getTypeId() == Material.DIAMOND_AXE.getId() && player.isOp()){
        			if(event.getAction() == Action.LEFT_CLICK_BLOCK){
        				loc1 = event.getClickedBlock().getLocation();
        			}else if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
        				loc2 = event.getClickedBlock().getLocation();
        			}
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
	
	@EventHandler
    public void onMove(PlayerMoveEvent event){
		if(getConfig().getConfigurationSection("hub.portals") != null){
			
			
			Player player = event.getPlayer();
	    
			
            for(String key : getConfig().getConfigurationSection("hub.portals").getKeys(false)){
                Location l1 = new Location(getServer().getWorld(getConfig().getString("hub.portals."+key+".loc1.world")), getConfig().getInt("hub.portals."+key+".loc1.x"), getConfig().getInt("hub.portals."+key+".loc1.y"), getConfig().getInt("hub.portals."+key+".loc1.z"));
                Location l2 = new Location(getServer().getWorld(getConfig().getString("hub.portals."+key+".loc2.world")), getConfig().getInt("hub.portals."+key+".loc2.x"), getConfig().getInt("hub.portals."+key+".loc2.y"), getConfig().getInt("hub.portals."+key+".loc2.z"));
                if(Boolean.valueOf(isPlayerInPortal(player.getLocation(), l1, l2, key)[0])){
//				    Bukkit.broadcastMessage(ChatColor.GREEN + "YES" + key);
	    	    }else{
//				    Bukkit.broadcastMessage(ChatColor.RED + "NO");
	    	    }           
            }
        }
    }
	
	public String[] isPlayerInPortal(Location location, Location location1, Location location2, String portal) {
        boolean x = location.getX() > Math.min(location1.getX(), location2.getX()) && location.getX() < Math.max(location1.getX(), location2.getX());
        boolean y = location.getY() > Math.min(location1.getY(), location2.getY()) && location.getY() < Math.max(location1.getY(), location2.getY());
        boolean z = location.getZ() > Math.min(location1.getZ(), location2.getZ()) && location.getZ() < Math.max(location1.getZ(), location2.getZ());
        return new String[] {String.valueOf(x && y && z), portal};
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
		if(!event.getWhoClicked().isOp() && event.getClick() != ClickType.CREATIVE){
			event.setCancelled(true);
		}
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if(sender instanceof Player){
			player = ((Player) sender).getPlayer();
		}
		if(player.isOp()){
			if (cmd.getName().equalsIgnoreCase("portal")) {
				if (args.length > 0){
					if(loc1 != null && loc2 != null){
    					getConfig().set("hub.portals."+args[0]+".loc1.world", loc1.getWorld().getName().toString());
    					getConfig().set("hub.portals."+args[0]+".loc1.x", loc1.getBlockX());
    					getConfig().set("hub.portals."+args[0]+".loc1.y", loc1.getBlockY());
    					getConfig().set("hub.portals."+args[0]+".loc1.z", loc1.getBlockZ());
    					getConfig().set("hub.portals."+args[0]+".loc2.world", loc2.getWorld().getName().toString());
    					getConfig().set("hub.portals."+args[0]+".loc2.x", loc2.getBlockX());
    					getConfig().set("hub.portals."+args[0]+".loc2.y", loc2.getBlockY());
    					getConfig().set("hub.portals."+args[0]+".loc2.z", loc2.getBlockZ());
    					saveConfig();
    					loc1 = null;
    					loc2 = null;
    				}
                }else{
                	ItemStack axeItem = new ItemStack(Material.DIAMOND_AXE);
                    ItemMeta axeItemMeta= axeItem.getItemMeta();
                    axeItemMeta.setDisplayName(ChatColor.AQUA + "Portal Tool");
                    axeItem.setItemMeta(axeItemMeta);
    		        player.getInventory().setItem(8, axeItem);
                }
				return true;
			}
		}
		return false; 
	}
	
}
