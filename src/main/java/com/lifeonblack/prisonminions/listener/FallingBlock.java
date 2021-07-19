package com.lifeonblack.prisonminions.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class FallingBlock implements Listener {

    @EventHandler
    public void onChange(EntityChangeBlockEvent e) {
        if(e.getEntityType() == EntityType.FALLING_BLOCK) {
            e.setCancelled(true);
        }
    }
}
