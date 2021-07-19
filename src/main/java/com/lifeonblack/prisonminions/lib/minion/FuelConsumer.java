package com.lifeonblack.prisonminions.lib.minion;

import org.bukkit.Material;

public interface FuelConsumer {

    int getFuel();
    int consumePerTask();
    int minutesPerFuel();
    Material getFuelType();
    default boolean hasEnoughFuel() {
        return getFuel() > consumePerTask();
    }
    default void consumeFuel() {
        setFuel(getFuel() - consumePerTask());
    }
    void setFuel(int fuel);
}
