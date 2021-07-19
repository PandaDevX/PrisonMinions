package com.lifeonblack.prisonminions.lib.minion.woodcutter;

import com.lifeonblack.prisonminions.PrisonMinions;
import com.lifeonblack.prisonminions.lib.inventory.GUI;
import com.lifeonblack.prisonminions.lib.item.ItemBuilder;
import com.lifeonblack.prisonminions.lib.minion.FuelConsumer;
import com.lifeonblack.prisonminions.lib.minion.Minion;
import com.lifeonblack.prisonminions.lib.minion.MinionProperty;
import com.lifeonblack.prisonminions.lib.minion.animation.Animation;
import com.lifeonblack.prisonminions.lib.minion.animation.Callback;
import com.lifeonblack.prisonminions.lib.minion.animation.SimpleAnimation;
import com.lifeonblack.prisonminions.lib.minion.animation.XSound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.*;
import java.util.stream.Collectors;

public class WoodCutter extends Minion implements FuelConsumer {

    private final GUI gui;
    private final WoodCutterProperty woodCutterProperty;
    private final MinionProperty minionProperty;
    private final EulerAngle originalArmPose;
    public WoodCutter(MinionProperty minionProperty, WoodCutterProperty woodCutterProperty) {
        super(minionProperty.getLocation(), minionProperty.getName(), minionProperty.getOwner(), minionProperty.getLevel(), minionProperty.getFuelStamp(), minionProperty.getTimeStamp(),
                minionProperty.minionEquipment());
        this.gui = new GUI("Woodcutter Minion", 6, this) {
            @Override
            public void onLoad() {
                Inventory inventory = getInventory();
                for(int i = 0; i < inventory.getSize(); i++) {
                    inventory.setItem(i, ItemBuilder.build(Material.BLACK_STAINED_GLASS_PANE, " "));
                }
                inventory.setItem(3, ItemBuilder.build(Material.BONE_MEAL, "&aBone Meal Slot"));
                inventory.setItem(4, ItemBuilder.build(getArmorStand().getEquipment().getHelmet(), "&eWoodCutter Minion"));
                inventory.setItem(5, ItemBuilder.build(Material.OAK_SAPLING, "&aSapling Slot"));
                inventory.setItem(10, ItemBuilder.build(Material.LIME_STAINED_GLASS_PANE, "&aMinion Skin Slot",
                        "&7You can insert a Minion Skin", "&7here to change the appearance of", "&7your minion"));
                inventory.setItem(28, ItemBuilder.build(Material.ENDER_CHEST, "&aAttach a Chest", "&7Only available for max level."));
                inventory.setItem(46, ItemBuilder.build(getFuelType(), "&aFuel", "&7You can place beacon here to add fuel", "&71 fuel is equivalent to 30 minutes."));
                inventory.setItem(48, ItemBuilder.build(Material.CHEST, "&eTake All", "&7Click this to take all the items."));
                inventory.setItem(53, ItemBuilder.build(Material.BEDROCK, "&eTake Minion", "&7Click this to take your minion."));
                for(int i = 0; i < (getLevel() * 2); i++) {
                    inventory.setItem(getStorageSlot()[i], null);
                    if(i >= getStorageSlot().length) break;
                }
                if((getLevel() * 2) >= getStorageSlot().length) return;
                for(int i = (getLevel() * 2); i < getStorageSlot().length; i++) {
                    inventory.setItem(getStorageSlot()[i], ItemBuilder.build(Material.BARRIER, "&cBlocked"));
                }
            }


            @Override
            public void onClick(Player player, InventoryClickEvent e) {
                switch(e.getSlot()) {
                    case 3:
                        ItemStack item = player.getItemOnCursor();
                        if(item.getType() == Material.BONE_MEAL) {
                            player.setItemOnCursor(null);
                            getProperty().addBoneMeal(item.getAmount());
                            setStop(false);
                        }
                        e.setCancelled(true);
                        break;
                    case 4:
                        e.setCancelled(true);
                        break;
                    case 5:
                        if(player.getItemOnCursor().getType().name().endsWith("SAPLING") && !player.getItemOnCursor().getType().name().contains("POTTED")) {
                            getProperty().addSapling(player.getItemOnCursor().getType(), player.getItemOnCursor().getAmount());
                            player.setItemOnCursor(null);
                            setStop(false);
                        }
                        e.setCancelled(true);
                        break;
                    case 10:
                        if(player.getItemOnCursor().getType() == Material.PLAYER_HEAD) {
                            getArmorStand().getEquipment().setHelmet(player.getItemOnCursor());
                            player.setItemOnCursor(null);
                        }
                        e.setCancelled(true);
                        break;
                    case 28:
                        if(getLevel() != getMaxLevel()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis feature is locked."));
                            e.setCancelled(true);
                            break;
                        }
                        // TODO attach chest
                        break;
                    case 46:
                        ItemStack cursor = player.getItemOnCursor();
                        if(cursor.getType() == getFuelType()) {
                            setFuel(getFuel() + cursor.getAmount());
                            setStop(hasEnoughFuel());
                        }
                        e.setCancelled(true);
                        break;
                    case 48:
                        Arrays.stream(getStorageSlot()).filter(i -> getInventory().getItem(i) != null)
                                .filter(i -> !(getInventory().getItem(i).hasItemMeta() && getInventory().getItem(i).getItemMeta().hasDisplayName()
                                 && getInventory().getItem(i).getItemMeta().getDisplayName().equals(ChatColor.RED + "Blocked")))
                                .forEach(i -> {
                            if(player.getInventory().addItem(getInventory().getItem(i)).isEmpty()) {
                                getInventory().setItem(i, null);
                            }
                        });
                        player.updateInventory();
                        e.setCancelled(true);
                        break;
                    case 53:
                        // TODO give it to player
                        break;
                }
                if(criteriaMet(e.getCurrentItem(), true, true, false)) {
                    if(e.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) {
                            e.setCancelled(true);
                            return;
                        }
                        return;
                    }
                    if(e.getCurrentItem().getType() == Material.BARRIER) {
                        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Blocked")) {
                            e.setCancelled(true);
                            return;
                        }
                        return;
                    }
                    return;
                }
            }

            @Override
            public void onDynamic() {

            }
        };
        this.woodCutterProperty = woodCutterProperty;
        this.minionProperty = minionProperty;
        this.originalArmPose = getArmorStand().getRightArmPose();
    }

    @Override
    public void onSpawn() {

        setTimeStamp(15000 + System.currentTimeMillis());
        setFuelStamp(minutesPerFuel());
    }

    public MinionProperty getMinionProperty() {
        return minionProperty;
    }

    public WoodCutterProperty getProperty() {
        return woodCutterProperty;
    }

    public boolean emptyFuel() {
        return getFuel() == 0;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public void performAnimation() {
        if(isStopped()) return;
    }

    public void levelUp() {
        if(getLevel() == getMaxLevel()) {
            return;
        }
        Inventory inventory = getGUI().getInventory();
        for(int i = getLevel(); i < ((getLevel() + 1) * 2); i++) {
            inventory.setItem(getStorageSlot()[i], null);
            if(i >= getStorageSlot().length) break;
        }
        setLevel(getLevel() + 1);
    }

    @Override
    public boolean canDestroy(Player player) {
        if(player == null) {
            return false;
        }
        if(!player.hasPermission("prisonminion.admin") && !player.getUniqueId().equals(getOwner())) {
            return false;
        }
        HashMap<Integer, ItemStack> hashMap = player.getInventory().addItem(getItem());
        if(!hashMap.isEmpty()) {
            return false;
        }
        getArmorStand().remove();
        player.updateInventory();
        return true;
    }

    public Set<Block> getTree(List<Material> allowedMaterials) {
        if(getBlockInFront() == null || !getBlockInFront().getType().name().endsWith("LOG"))
            return Collections.emptySet();
        return getNearbyBlocks(getBlockInFront(), allowedMaterials, new HashSet<>());
    }

    public void applyBoneMeal() {
        getBlockInFront().applyBoneMeal(BlockFace.NORTH);
        Set<Player> players = getPlayersWithin(getBlockInFront().getLocation(), 20);
        if(players.isEmpty()) return;
        players.forEach(p -> p.spawnParticle(Particle.VILLAGER_HAPPY, getBlockInFront().getLocation(), 20));
    }

    public Set<Player> getPlayersWithin(Location baseLocation, int radius) {
        return Bukkit.getOnlinePlayers().stream().filter(p -> p.getLocation().distanceSquared(baseLocation) <= (radius * radius)).collect(Collectors.toSet());
    }

    private Set<Block> getNearbyBlocks(Block start, List<Material> allowedMaterials, HashSet<Block> blocks) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Block block = start.getLocation().clone().add(x, y, z).getBlock();
                    if (!blocks.contains(block) && allowedMaterials.contains(block.getType())) {
                        blocks.add(block);
                        blocks.addAll(getNearbyBlocks(block, allowedMaterials, blocks));
                    }
                }
            }
        }
        return blocks;
    }

    @Override
    public GUI getGUI() {
        return this.gui;
    }

    @Override
    public void performTask() {
        Callback callback = (tree, players, property) -> {
            if(tree.isEmpty()) return;
            for(Block block : tree) {
                FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
                fallingBlock.setDropItem(false);
                block.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
            }
            if(players.isEmpty()) return;
            players.forEach(p -> {
                for(int i = 0; i < 10; i++) {
                    XSound.BLOCK_BAMBOO_BREAK.playRepeatedly(PrisonMinions.getInstance(), p, 1.0f, 1.0f, 5, 5);
                }
            });
            property.setTreeHealth(property.getTreeMaxHealth());
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(PrisonMinions.getInstance(), () -> {
            setTimeStamp(15000 + System.currentTimeMillis());
            final EulerAngle getRightArmPose = originalArmPose;
            if(getBlockInFront().getType() == Material.AIR) {
                Material sapling = getProperty().getSapling();
                if(sapling != Material.AIR) {
                    Animation animation = new Animation() {
                        @Override
                        public void before() {
                            getMinionProperty().minionEquipment().setMainHand(new ItemStack(sapling));
                            reloadHand();
                        }

                        @Override
                        public void after() {
                            getMinionProperty().minionEquipment().setMainHand(null);
                            reloadHand();
                            getArmorStand().setRightArmPose(getRightArmPose);
                        }
                    };
                    new SimpleAnimation(this, animation).runTaskTimer(PrisonMinions.getInstance(), 0, 3);
                    getBlockInFront().setType(sapling);
                    getProperty().takeSapling(sapling);
                } else {
                    setStop(true);
                }
                return;
            }
            if(getBlockInFront().getType().name().contains("POTTED")) return;
            if(getBlockInFront().getType().name().endsWith("SAPLING")) {
                if(getProperty().hasBoneMeal()) {
                    Animation animation = new Animation() {
                        @Override
                        public void before() {
                            getMinionProperty().minionEquipment().setMainHand(new ItemStack(Material.BONE_MEAL));
                            reloadHand();
                        }

                        @Override
                        public void after() {
                            applyBoneMeal();
                            getProperty().takeBoneMeal();
                            getMinionProperty().minionEquipment().setMainHand(null);
                            reloadHand();
                            getArmorStand().setRightArmPose(getRightArmPose);
                        }
                    };
                    new SimpleAnimation(this, animation).runTaskTimer(PrisonMinions.getInstance(), 0, 3);

                } else {
                    setStop(true);
                }
            } else {
                Animation animation = new Animation() {
                    @Override
                    public void before() {
                        getMinionProperty().minionEquipment().setMainHand(new ItemStack(Material.DIAMOND_AXE));
                        reloadHand();
                    }

                    @Override
                    public void after() {
                        if(!getProperty().damageTree()) {
                            callback.breakTree(getTree(Arrays.asList(Material.ACACIA_LOG, Material.ACACIA_LEAVES)), getPlayersWithin(getBlockInFront().getLocation(), 20), getProperty());
                        }
                        getMinionProperty().minionEquipment().setMainHand(null);
                        reloadHand();
                        getArmorStand().setRightArmPose(getRightArmPose);
                    }
                };
                new SimpleAnimation(this, animation).runTaskTimer(PrisonMinions.getInstance(), 0, 3);
            }
        });
    }

    @Override
    public int getFuel() {
        return getProperty().getFuel();
    }

    @Override
    public int consumePerTask() {
        return 1;
    }

    @Override
    public int minutesPerFuel() {
        return 30;
    }

    @Override
    public Material getFuelType() {
        return Material.BEACON;
    }

    @Override
    public void setFuel(int fuel) {
        getProperty().setFuel(fuel);
    }
}
