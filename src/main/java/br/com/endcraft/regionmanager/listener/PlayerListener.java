package br.com.endcraft.regionmanager.listener;

import br.com.endcraft.regionmanager.RegionManager;
import br.com.endcraft.regionmanager.location.WorldGuardManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Set;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerEnterArea(PlayerMoveEvent e) {
        Location exitLocation = e.getFrom();
        Location enterLocation = e.getTo();

        if(enterLocation == null) return;

        String regionExit = WorldGuardManager.getRegionByLocation(exitLocation);
        String regionEnter = WorldGuardManager.getRegionByLocation(enterLocation);

        if(regionExit == null && regionEnter == null) {
            return;
        }

        if(regionExit == null || !regionExit.equals(regionEnter)) {
          if(!isValidToPlayerAccessRegion(e.getPlayer(), regionEnter, enterLocation.getWorld().getName())) {
              e.setCancelled(true);
          }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        Location enterLocation = e.getTo();
        Location fromLocation = e.getFrom();
        if(enterLocation.getWorld().getName().equals(fromLocation.getWorld().getName())) {
            return;
        }

        String regionEnter = WorldGuardManager.getRegionByLocation(enterLocation);

        if(regionEnter == null) {
            if(!isValidToPlayerAccessRegion(e.getPlayer(), "__global__", enterLocation.getWorld().getName())) {
                e.setCancelled(true);
            }
            return;
        }

        if(!isValidToPlayerAccessRegion(e.getPlayer(), regionEnter, enterLocation.getWorld().getName())) {
            e.setCancelled(true);
        }
    }

    private boolean isValidToPlayerAccessRegion(Player player, String regionEnter, String enterWorld) {
        Set<Long> items = RegionManager.getDatabase().getBlockedItemsByRegion(regionEnter, enterWorld);
        if(items.isEmpty()) return true;

        if(items.stream().anyMatch(itemId -> hasItem(player, itemId))) {
            player.sendMessage(ChatColor.RED + "Você não pode acessar esse local por estar com um item não permitido aqui.");
            return false;
        }
        return true;
    }

    private boolean hasItem(Player player, Long itemId) {
        PlayerInventory inventory = player.getInventory();
        if(iterableItemStack(inventory.getContents(), itemId)) {
            return true;
        }

        if(iterableItemStack(inventory.getArmorContents(), itemId)) {
            return true;
        }

        return false;
    }

    private boolean iterableItemStack(ItemStack[] items, Long findItemId) {
        for (ItemStack item : items) {
            Long id = (long) item.getType().getId();
            if(id.equals(findItemId)) return true;
        }
        return false;
    }
}
