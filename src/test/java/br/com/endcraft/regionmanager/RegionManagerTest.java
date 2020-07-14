package br.com.endcraft.regionmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerJoinEvent.class)
public class RegionManagerTest {

    @Test
    public void shouldPluginLoad() {
        PlayerJoinEvent mockEvent = mock(PlayerJoinEvent.class);
        Player mockPlayer = mock(Player.class);

        when(mockPlayer.getName()).thenReturn("JonasXPX");

//        when(mockEvent.getPlayer()).thenReturn(mockPlayer.getPlayer());
//
        assertEquals("JonasXPX", mockPlayer.getName());
    }
}
