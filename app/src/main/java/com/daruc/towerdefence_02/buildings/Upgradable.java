package com.daruc.towerdefence_02.buildings;

/**
 * Created by darek on 07.04.18.
 */

public interface Upgradable {
    boolean upgrade();
    int getLevel();
    int getMaxLevel();
}
