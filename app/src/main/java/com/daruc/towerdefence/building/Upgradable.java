package com.daruc.towerdefence.building;

/**
 * Created by darek on 07.04.18.
 */

public interface Upgradable {
    boolean upgrade();
    int getLevel();
    int getMaxLevel();
}
