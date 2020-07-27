package br.com.endcraft.regionmanager.listener;

import br.com.endcraft.regionmanager.RegionManager;
import br.com.endcraft.regionmanager.location.WorldGuardManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
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

        if(!isValidToPlayerAccessRegion(e.getPlayer(), "__global__", enterLocation.getWorld().getName())) {
            e.setCancelled(true);
        }

        String regionEnter = WorldGuardManager.getRegionByLocation(enterLocation);

        if(regionEnter == null) {
            return;
        }

        if(!isValidToPlayerAccessRegion(e.getPlayer(), regionEnter, enterLocation.getWorld().getName())) {
            e.setCancelled(true);
        }
    }

    private boolean isValidToPlayerAccessRegion(Player player, String regionEnter, String enterWorld) {
        Set<Long> items = RegionManager.getDatabase().getBlockedItemsByRegion(regionEnter, enterWorld);
        if(items.isEmpty()) return true;
        List<Long> blockedItems = new ArrayList<Long>();
        items.forEach(item -> {
            if(hasItem(player, item))
                blockedItems.add(item);
        });

        if(!blockedItems.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Impossivel acessar esse local. " + formatBlockedItems(blockedItems));
            return false;
        }
        return true;
    }

    public static boolean hasItem(Player player, Long itemId) {
        PlayerInventory inventory = player.getInventory();
        if(iterableItemStack(inventory.getContents(), itemId)) {
            return true;
        }

        if(iterableItemStack(inventory.getArmorContents(), itemId)) {
            return true;
        }

        return false;
    }

    private static boolean iterableItemStack(ItemStack[] items, Long findItemId) {
        for (ItemStack item : items) {
            if(item == null) continue;
            Long id = (long) item.getType().getId();
            if(id.equals(findItemId)) return true;
        }
        return false;
    }

    private String formatBlockedItems(List<Long> blockedItems) {
        StringBuilder stringBuilder = new StringBuilder();

        if(stringBuilder.length() > 1)
            stringBuilder.append("Itens bloqueados: ");
        else
            stringBuilder.append("Item bloqueado: ");

        blockedItems.forEach(itemId -> stringBuilder.append(itemId + " "));
        return stringBuilder.toString();
    }
}
