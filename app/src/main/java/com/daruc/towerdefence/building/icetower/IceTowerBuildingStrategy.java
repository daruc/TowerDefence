package com.daruc.towerdefence.building.icetower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;


public class IceTowerBuildingStrategy implements BuildingStrategy {

    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return IceTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        if (groundType == GroundType.GRASS && building == null) {
            IceTower iceTower = new IceTower(new PointF(mapPoint.getX() + 0.5f,
                    mapPoint.getY() + 0.5f));

            gameMap.setBuilding(mapPoint, iceTower);
            mapView.setGold(mapView.getGold() - IceTower.COST);
        }
    }
}
