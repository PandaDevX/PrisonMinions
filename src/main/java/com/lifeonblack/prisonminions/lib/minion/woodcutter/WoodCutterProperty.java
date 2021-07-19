package com.lifeonblack.prisonminions.lib.minion.woodcutter;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class WoodCutterProperty {

    private int treeHealth, treeMaxHealth, boneMeal,oakSapling, darkOakSapling, jungleSapling, spruceSapling, acaciaSapling, bambooSapling, birchSapling, fuel;
    public WoodCutterProperty(int treeHealth,int treeMaxHealth, int boneMeal, int oakSapling,int darkOakSapling,int jungleSapling,int spruceSapling,int acaciaSapling,int bambooSapling,int birchSapling, int fuel) {
        this.treeHealth = treeHealth;
        this.treeMaxHealth = treeMaxHealth;
        this.boneMeal = boneMeal;
        this.oakSapling = oakSapling;
        this.darkOakSapling = darkOakSapling;
        this.jungleSapling = jungleSapling;
        this.spruceSapling = spruceSapling;
        this.acaciaSapling = acaciaSapling;
        this.bambooSapling = bambooSapling;
        this.birchSapling = birchSapling;
        this.fuel = fuel;
    }

    public int getSapling(Material material) {
        if(material == Material.ACACIA_SAPLING) {
            return this.acaciaSapling;
        }
        if(material == Material.OAK_SAPLING) {
            return this.oakSapling;
        }
        if(material == Material.BAMBOO_SAPLING) {
            return this.bambooSapling;
        }
        if(material == Material.BIRCH_SAPLING) {
            return this.birchSapling;
        }
        if(material == Material.DARK_OAK_SAPLING) {
            return this.darkOakSapling;
        }
        if(material == Material.JUNGLE_SAPLING) {
            return this.jungleSapling;
        }
        return this.spruceSapling;
    }

    public Material getSapling() {
        for(Material sapling : saplings()) {
            if(getSapling(sapling) > 0) {
                return sapling;
            }
        }
        return Material.AIR;
    }

    public int getTreeMaxHealth() {
        return treeMaxHealth;
    }

    public void addSapling(Material material, int amount) {
        if(material == Material.ACACIA_SAPLING) {
            this.acaciaSapling += amount;
        }
        if(material == Material.OAK_SAPLING) {
            this.oakSapling += amount;
        }
        if(material == Material.BAMBOO_SAPLING) {
            this.bambooSapling += amount;
        }
        if(material == Material.BIRCH_SAPLING) {
            this.birchSapling += amount;
        }
        if(material == Material.DARK_OAK_SAPLING) {
            this.darkOakSapling += amount;
        }
        if(material == Material.JUNGLE_SAPLING) {
            this.jungleSapling += amount;
        }
        this.spruceSapling += amount;
    }

    public void takeSapling(Material material) {
        if(material == Material.ACACIA_SAPLING) {
            this.acaciaSapling -= 1;
        }
        if(material == Material.OAK_SAPLING) {
            this.oakSapling -= 1;
        }
        if(material == Material.BAMBOO_SAPLING) {
            this.bambooSapling -= 1;
        }
        if(material == Material.BIRCH_SAPLING) {
            this.birchSapling -= 1;
        }
        if(material == Material.DARK_OAK_SAPLING) {
            this.darkOakSapling -= 1;
        }
        if(material == Material.JUNGLE_SAPLING) {
            this.jungleSapling -= 1;
        }
        this.spruceSapling -= 1;
    }

    private List<Material> saplings() {
        return Arrays.asList(Material.ACACIA_SAPLING, Material.OAK_SAPLING, Material.BAMBOO_SAPLING, Material.BIRCH_SAPLING, Material.DARK_OAK_SAPLING, Material.JUNGLE_SAPLING, Material.SPRUCE_SAPLING);
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getBoneMeal() {
        return boneMeal;
    }

    public boolean hasBoneMeal() {
        return boneMeal != 0;
    }

    public void takeBoneMeal() {
        this.boneMeal -= 1;
    }

    public void addBoneMeal(int amount) {
        this.boneMeal += amount;
    }

    public int getTreeHealth() {
        return treeHealth;
    }

    public boolean damageTree() {
        if(getTreeHealth() == 0) {
            this.treeHealth = treeMaxHealth;
            return false;
        }
        this.treeHealth -= 1;
        return true;
    }

    public void setTreeHealth(int health) {
        this.treeHealth = health;
    }

    public void setTreeMaxHealth(int health) {
        this.treeMaxHealth = health;
    }

}
