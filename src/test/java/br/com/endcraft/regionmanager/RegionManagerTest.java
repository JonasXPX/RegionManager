package br.com.endcraft.regionmanager;

import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerJoinEvent.class)
public class RegionManagerTest {



    @Test
    public void shouldCreateTablesInsertNewRegionAndDelete() {
        SQLiteManager sqLiteManager = new SQLiteManager(System.getProperty("user.dir"));
        sqLiteManager.setItemIdBlocked(1L, "test", "testworld");
        Set<Long> blocked = sqLiteManager.getBlockedItemsByRegion("test", "testworld");
        Long blockedItem = blocked.iterator().next();
        assertEquals(1, blockedItem);
        assertDoesNotThrow(() -> sqLiteManager.removeBlockedItemFromRegion(1L, "test", "testworld"));
    }

    @Test
    public void shouldTestValue() {
        Long itemId = 99999L;
        long compareId = 99999L;
        assertEquals(itemId, compareId);
    }

}
