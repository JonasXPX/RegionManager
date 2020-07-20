package br.com.endcraft.regionmanager;

import br.com.endcraft.regionmanager.listener.PlayerListener;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class RegionManager extends JavaPlugin {

    private static WorldGuardPlugin worldGuardPlugin;
    private static Set<Region> regions;
    private static SQLiteManager database;

    @Override
    public void onEnable() {
        worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        database = new SQLiteManager(getDataFolder().getPath());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("rmg").setExecutor(new RegionCommands());
    }

    public static WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public static SQLiteManager getDatabase() {
        return database;
    }
}
