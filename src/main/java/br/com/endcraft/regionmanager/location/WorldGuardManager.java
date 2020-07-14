package br.com.endcraft.regionmanager.location;

import br.com.endcraft.regionmanager.RegionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.GlobalRegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;

import java.util.Iterator;

public class WorldGuardManager {

    public static String getRegionByLocation(Location location) {
        GlobalRegionManager globalRegionManager = RegionManager.getWorldGuardPlugin().getGlobalRegionManager();
        ApplicableRegionSet applicableRegions = globalRegionManager.get(location.getWorld())
                .getApplicableRegions(location);

    }


    public static String getRegionFromApplicableRegions(ApplicableRegionSet protectedRegions) {
        Iterator<ProtectedRegion> iterator = protectedRegions.iterator();
        while (iterator.hasNext()) {
            ProtectedRegion protectedRegion = iterator.next();
            String regionId = protectedRegion.getId();
        }
        return null;
    }
}
