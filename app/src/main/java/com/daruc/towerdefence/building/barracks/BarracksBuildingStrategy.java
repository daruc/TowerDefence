package com.daruc.towerdefence.building.barracks;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class BarracksBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return Barracks.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        if (groundType == GroundType.GRASS && building == null
                && gameMap.isNextToPath(mapPoint)) {

            Barracks barracks = new Barracks(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
            gameMap.setBuilding(mapPoint, barracks);
            mapView.setGold(mapView.getGold() - Barracks.COST);
        }
    }
}
