package com.lifeonblack.prisonminions.lib.minion;

import org.bukkit.Location;

import java.util.UUID;

public class MinionProperty {

    public final int level;
    public final long timeStamp, fuelStamp;
    private final String name;
    private final UUID owner;
    private final Location location;
    private final MinionEquipment minionEquipment;

    public MinionProperty(int level, long timeStamp, long fuelStamp, String name, UUID owner, Location location, MinionEquipment minionEquipment) {
        this.level = level;
        this.timeStamp = timeStamp;
        this.fuelStamp = fuelStamp;
        this.name = name;
        this.location = location;
        this.owner = owner;
        this.minionEquipment = minionEquipment;
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getFuelStamp() {
        return fuelStamp;
    }

    public MinionEquipment minionEquipment() {
        return minionEquipment;
    }
}
