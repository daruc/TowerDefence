package com.daruc.towerdefence.building.antitanktower;

import android.graphics.PointF;

import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class AntiTankTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return AntiTankTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GroundType groundType = mapView.getGameMap().getGround(mapPoint);
        Building building = mapView.getGameMap().getBuilding(mapPoint);

        if (isFreeTile(groundType, building)) {
            buildAntiTankTower(mapView, mapPoint);
        }
    }

    private boolean isFreeTile(GroundType groundType, Building building) {
        return groundType == GroundType.GRASS && building == null;
    }

    private void buildAntiTankTower(MapView mapView, MapPoint mapPoint) {
        AntiTankTower antiTankTower = newAntiTankTower(mapPoint);
        placeNewBuildingOnMap(mapView, mapPoint, antiTankTower);
        decreaseGold(mapView);
    }

    private AntiTankTower newAntiTankTower(MapPoint mapPoint) {
        return new AntiTankTower(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
    }

    private void placeNewBuildingOnMap(MapView mapView, MapPoint mapPoint,
                                       AntiTankTower antiTankTower) {

        mapView.getGameMap().setBuilding(mapPoint, antiTankTower);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - AntiTankTower.COST);
    }
}
