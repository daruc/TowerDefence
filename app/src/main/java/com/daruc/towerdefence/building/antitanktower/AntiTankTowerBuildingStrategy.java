package com.daruc.towerdefence.building.antitanktower;

import android.graphics.PointF;

import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
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
    public void build(MapView mapView, MapPoint mapPoint) {
        GroundType groundType = mapView.getGameMap().getGround(mapPoint);
        Building building = mapView.getGameMap().getBuilding(mapPoint);

        if (groundType == GroundType.GRASS && building == null) {
            AntiTankTower antiTankTower =
                    new AntiTankTower(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));

            mapView.getGameMap().setBuilding(mapPoint, antiTankTower);
            mapView.setGold(mapView.getGold() - AntiTankTower.COST);
        }
    }
}
