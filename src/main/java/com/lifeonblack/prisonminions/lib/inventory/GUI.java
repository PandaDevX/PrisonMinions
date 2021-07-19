package com.lifeonblack.prisonminions.lib.inventory;

import com.lifeonblack.prisonminions.PrisonMinions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public abstract class GUI implements Listener {

    private final static HashMap<InventoryHolder, Inventory> inventoryMap = new HashMap<>();


    private final Inventory inventory;
    private final InventoryHolder holder;
    private final String name;
    private final List<ItemStack> cacheItem;
    public GUI(String name, int rows, InventoryHolder holder) {
        this.holder = holder;
        this.inventory = Bukkit.createInventory(holder, rows * 9, ChatColor.translateAlternateColorCodes('&', name));
        this.name = name;
        onLoad();
        PrisonMinions.getInstance().getServer().getPluginManager().registerEvents(this, PrisonMinions.getInstance());
        this.cacheItem = new ArrayList<>();
    }

    public abstract void onLoad();
    public abstract void onClick(Player player, InventoryClickEvent e);
    public abstract void onDynamic();

    @EventHandler
    public void onClickE(InventoryClickEvent e) {
        if(!(e.getView().getTitle().equals(name))) return;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory() instanceof PlayerInventory) return;
        onClick((Player) e.getWhoClicked(), e);
    }

    public Inventory getInventory() {
        if(inventoryMap.containsKey(holder)) {
            return inventoryMap.get(holder);
        }
        return inventory;
    }


    public void addItem(ItemStack... itemStack) {
        cacheItem.addAll(Arrays.asList(itemStack));
    }

    public boolean needUpdate() {
        return !cacheItem.isEmpty();
    }

    public Collection<ItemStack> updateInventory() {
        if(!needUpdate()) {
            return Collections.emptyList();
        }
        HashMap<Integer, ItemStack> addedItems = getInventory().addItem(cacheItem.toArray(new ItemStack[0]));
        cacheItem.clear();
        return addedItems.values();
    }

    public void save(Inventory inventory, InventoryHolder holder) {
        inventoryMap.put(holder, inventory);
    }

    protected boolean criteriaMet(ItemStack itemStack, boolean notNull, boolean hasDisplayName, boolean hasLore) {
        if(notNull) {
            return itemStack != null;
        }
        if(hasDisplayName) {
            return itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
        }
        if(hasLore) {
            return itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore();
        }
        return false;
    }


}
