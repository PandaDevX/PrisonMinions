package com.lifeonblack.prisonminions;

import com.lifeonblack.prisonminions.lib.minion.Appetite;
import com.lifeonblack.prisonminions.lib.minion.FuelConsumer;
import com.lifeonblack.prisonminions.lib.minion.Minion;
import com.lifeonblack.prisonminions.lib.minion.listener.Close;
import com.lifeonblack.prisonminions.lib.workload.WorkThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.Collection;

public final class PrisonMinions extends JavaPlugin implements Listener {

    private WorkThread workThread = null;
    private static PrisonMinions instance = null;

    @Override
    public void onEnable() {
        instance = this;
        this.workThread = new WorkThread();

        registerListener(new Close());

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
           for(Minion minion : Minion.getMinions()) {
               if(minion.getTimeStamp() <= System.currentTimeMillis()) {
                   if(minion instanceof FuelConsumer) {
                       FuelConsumer fuelConsumer = (FuelConsumer) minion;
                       if(fuelConsumer.hasEnoughFuel()) {
                           if(minion.getFuelStamp() <= System.currentTimeMillis()) {
                               fuelConsumer.consumeFuel();
                           }
                           minion.setFuelStamp(fuelConsumer.minutesPerFuel());
                       } else {
                           minion.setStop(true);
                           minion.getArmorStand().setCustomName(ChatColor.translateAlternateColorCodes('&', "&cEmpty Fuel!!!"));
                       }
                   }
                   if(minion instanceof Appetite) {
                       Appetite appetite = (Appetite) minion;
                       if(appetite.isHungry()) {
                           minion.setStop(true);
                           minion.getArmorStand().setCustomName(ChatColor.translateAlternateColorCodes('&', "&cHungry!!!"));
                       } else {
                           appetite.consumeFood();
                       }
                   }
                   workThread.addTask(minion);
                   Collection<ItemStack> remaining = minion.getGUI().updateInventory();
                   if(!remaining.isEmpty()) {
                       if(minion.getLinkedChest() != null) {
                           minion.getLinkedChest().getBlockInventory().addItem(remaining.toArray(new ItemStack[0]));
                       }
                   }
               }
           }
        }, 0, 3);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, workThread, 0, 1);
    }

    public static PrisonMinions getInstance() {
        return instance;
    }

    public void registerListener(Listener... listener) {
        Arrays.stream(listener).forEach(l -> getServer().getPluginManager().registerEvents(l, this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
