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
        ApplicableRegionSet applicableRegions = globalRegionManager.get(location.getWorld()).getApplicableRegions(location);
        return getRegionFromApplicableRegions(applicableRegions);
    }


    private static String getRegionFromApplicableRegions(ApplicableRegionSet protectedRegions) {
        return getRegionByMostPriority(protectedRegions);
    }


    private static String getRegionByMostPriority(ApplicableRegionSet applicableRegions) {
        ProtectedRegion region = null;
        Iterator<ProtectedRegion> regions = applicableRegions.iterator();
        while(regions.hasNext()) {
            ProtectedRegion protectedRegion = regions.next();
            if(region == null) region = protectedRegion;
            if(protectedRegion.getPriority() > region.getPriority()) {
                region = protectedRegion;
            }
        }
        if(region == null) return null;
        return region.getId();
    }
}
