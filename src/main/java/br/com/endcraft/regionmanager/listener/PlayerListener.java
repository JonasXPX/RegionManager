package br.com.endcraft.regionmanager.listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerEnterArea(PlayerMoveEvent e) {
        Location enterLocation = e.getTo();
    }
}
