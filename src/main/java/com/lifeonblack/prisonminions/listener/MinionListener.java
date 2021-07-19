package com.lifeonblack.prisonminions.listener;

import com.lifeonblack.prisonminions.lib.minion.Minion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Optional;

public class MinionListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractAtEntityEvent e) {
        Optional<Minion> minion = Minion.getMinions().stream().filter(m -> m.getUniqueId().equals(e.getRightClicked().getUniqueId())).findAny();
        minion.ifPresent(m -> {
            e.getPlayer().openInventory(m.getInventory());
            e.setCancelled(true);
        });
    }
}
