package com.lifeonblack.prisonminions.lib.minion;


public interface Appetite {

    int getFoodLevel();
    int consumePerTask();
    default boolean isHungry() {
        return consumePerTask() > getFoodLevel();
    }
    default void consumeFood() {
        setFoodLevel(getFoodLevel() - consumePerTask());
    }
    default void feed() {
        setFoodLevel(getFoodLevel() + 1);
    }
    void setFoodLevel(int foodLevel);
}
