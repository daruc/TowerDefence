package com.daruc.towerdefence.building;


import com.daruc.towerdefence.building.powergenerator.PowerGenerator;

public interface PowerReceiver {
    boolean withPowerGenerator();
    void setPowerGenerator(PowerGenerator powerGenerator);
}
