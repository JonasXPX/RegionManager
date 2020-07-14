package br.com.endcraft.regionmanager;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class RegionManager extends JavaPlugin {

    private static WorldGuardPlugin worldGuardPlugin;
    private static Set<Region> regions;

    @Override
    public void onEnable() {
        worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        loadConfiguration();
    }

    private void loadConfiguration() {
        FileConfiguration config = getConfig();
        config.contains("region");
        defineDefaultConfig();
    }



    public static WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }
}
