package com.daruc.towerdefence.building.boat;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTower;

public class BoatBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return Boat.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        if (groundType == GroundType.WATER && building == null) {
            Boat boat = new Boat(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f),
                    mapView.getGameMap());

            gameMap.setBuilding(mapPoint, boat);
            mapView.setGold(mapView.getGold() - Boat.COST);
        }
    }
}
