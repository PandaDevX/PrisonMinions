package com.lifeonblack.prisonminions.lib.minion;

import org.bukkit.Location;

public class MinionLocation {

    private final int hash;
    private final Location location;
    public MinionLocation(Location location) {

        this.location = new Location(location.getWorld(), (int)location.getX(), (int)location.getY(), (int)location.getZ(), location.getYaw(), location.getPitch());

        this.hash = location.hashCode();
    }

    public Location getLocation() {
        return location;
    }


    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof MinionLocation)) {
            return false;
        }
        MinionLocation minionLocation = (MinionLocation) o;
        return getLocation().equals(minionLocation.getLocation());
    }
}
