package com.daruc.towerdefence.building.antitanktower;

import android.graphics.PointF;

import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTower;

public class AntiTankTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return AntiTankTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, int mapX, int mapY) {
        GroundType groundType = mapView.getGameMap().getGround(mapX, mapY);
        Building building = mapView.getGameMap().getBuilding(mapX, mapY);

        if (groundType == GroundType.GRASS && building == null) {
            AntiTankTower antiTankTower =
                    new AntiTankTower(new PointF(mapX + 0.5f, mapY + 0.5f));

            mapView.getGameMap().setBuilding(mapX, mapY, antiTankTower);
            mapView.setGold(mapView.getGold() - AntiTankTower.COST);
        }
    }
}
