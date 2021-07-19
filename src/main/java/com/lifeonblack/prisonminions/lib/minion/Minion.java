package com.lifeonblack.prisonminions.lib.minion;

import com.lifeonblack.prisonminions.lib.inventory.GUI;
import com.lifeonblack.prisonminions.lib.minion.animation.SimpleAnimation;
import com.lifeonblack.prisonminions.lib.minion.woodcutter.WoodCutter;
import com.lifeonblack.prisonminions.lib.minion.woodcutter.WoodCutterProperty;
import com.lifeonblack.prisonminions.lib.workload.Task;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public abstract class Minion implements Task, InventoryHolder {


    private static final HashMap<MinionLocation, Minion> minionsMap = new HashMap<>();


    public static boolean hasMinion(Location location) {
        MinionLocation minionLocation = new MinionLocation(location);
        return minionsMap.containsKey(minionLocation);
    }

    public static Collection<Minion> getMinions() {
        return minionsMap.values();
    }

    public static boolean createMinion(Location location, Player spawner) {
        if(hasMinion(location)) {
            return false;
        }
        // TODO not done
        MinionEquipment minionEquipment = new MinionEquipment("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWMzN2M5ZTU3YmRjMGFhOTI3ZWQyMTgzNThkZjNkYzkxZmE3ODI3OGUxZWRhZThiN2NmYjAwMDNkYTJhYzIyYyJ9fX0=", 1, 1, 1, new ItemStack(Material.AIR));
        MinionProperty minionProperty = new MinionProperty(1, 0, 0, "Test", spawner.getUniqueId(), location, minionEquipment);
        WoodCutterProperty woodCutterProperty = new WoodCutterProperty(2, 0, 100, 1, 1, 1, 1, 1, 1, 1, 100);
        Minion minion = new WoodCutter(minionProperty, woodCutterProperty);
        minion.onSpawn();
        minion.getGUI().onLoad();
        minionsMap.put(new MinionLocation(location), minion);
        return true;
    }

    private final ArmorStand armorStand;
    private final int hash;
    private int level;
    private final Location location;
    private final UUID owner;
    private long fuelStamp, timeStamp;
    private boolean isStopped = false;
    private Chest chest = null;
    private final MinionEquipment minionEquipment;
    private final String name;
    public Minion(Location location, String name, UUID owner, int level, long fuelStamp, long timeStamp, MinionEquipment minionEquipment) {
        this.armorStand = location.getWorld().spawn(location, ArmorStand.class);
        this.name = name;
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);
        armorStand.setGravity(true);
        this.owner = owner;
        this.location = armorStand.getLocation();
        this.hash = armorStand.getUniqueId().hashCode();
        this.level = level;
        this.fuelStamp = fuelStamp;
        this.timeStamp = timeStamp;
        this.minionEquipment = minionEquipment;

        armorStand.setArms(true);

        armorStand.getEquipment().setHelmet(minionEquipment.getHead());
        armorStand.getEquipment().setChestplate(minionEquipment.getChestPlate());
        armorStand.getEquipment().setLeggings(minionEquipment.getLeggings());
        armorStand.getEquipment().setBoots(minionEquipment.getBoots());
        armorStand.getEquipment().setItemInMainHand(minionEquipment.getMainHand());

    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public int[] getStorageSlot() {
        return new int[] {21,22,23,24,25,30,31,32,33,34,39,40,41,42,43};
    }

    public abstract void onSpawn();
    public abstract boolean canDestroy(Player player);
    public abstract GUI getGUI();
    public abstract int getMaxLevel();
    public abstract void performAnimation();

    public String getName() {
        return name;
    }

    public void setStop(boolean stop) {
        if(!stop) {
            getArmorStand().setCustomName(ChatColor.translateAlternateColorCodes('&', getName()));
        }
        this.isStopped = stop;
    }

    public void reloadHand() {
        armorStand.getEquipment().setItemInMainHand(minionEquipment.getMainHand());
    }

    public boolean isStopped() {
        return isStopped;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setFuelStamp(long min) {
        this.fuelStamp = ((min * 60) * 1000) + System.currentTimeMillis();
    }

    public long getFuelStamp() {
        return fuelStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setLinkedChest(Chest chest) {
        this.chest = chest;
    }

    public Chest getLinkedChest() {
        return chest;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp - (getLevel() * 1000L);
    }

    public UUID getUniqueId() {
        return armorStand.getUniqueId();
    }

    public UUID getOwner() {
        return owner;
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
        if(!(o instanceof Minion)) {
            return false;
        }
        return getUniqueId().equals(((Minion)o).getUniqueId());
    }

    public void kill() {
        armorStand.remove();
    }

    @Override
    public Inventory getInventory() {
        return getGUI().getInventory();
    }

    public void saveInventory(Inventory inventory) {
        getGUI().save(inventory, this);
    }

    public ItemStack getItem() {
        return null;
    }

    public Block getBlockInFront() {
        Vector dir = getLocation().getDirection();
        Location oneBlockAway = getLocation().clone().add(dir);
        oneBlockAway = oneBlockAway.clone().add(0, 1, 0);
        return oneBlockAway.getBlock();
    }

}
