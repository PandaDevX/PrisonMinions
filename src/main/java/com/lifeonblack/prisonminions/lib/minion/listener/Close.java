package com.lifeonblack.prisonminions.lib.minion.listener;

import com.lifeonblack.prisonminions.lib.minion.Minion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class Close implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(e.getInventory().getHolder() instanceof Minion) {
            Minion m = (Minion) e.getInventory().getHolder();
            m.saveInventory(e.getInventory());
        }
    }
}
