package com.lifeonblack.prisonminions.lib.minion.animation;

import com.lifeonblack.prisonminions.lib.minion.woodcutter.WoodCutterProperty;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

public interface Callback {

    void breakTree(Set<Block> tree, Set<Player> players, WoodCutterProperty property);
}
