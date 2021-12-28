package com.daruc.towerdefence.building.squaretower;

import android.graphics.PointF;

import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.squaretower.SquareTower;

public class SquareTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return mapView.getGold() >= SquareTower.COST;
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GroundType groundType = mapView.getGameMap().getGround(mapPoint);
        Building building = mapView.getGameMap().getBuilding(mapPoint);

        if (groundType == GroundType.GRASS && building == null) {

            GroundType[][] groundTiles = mapView.getGameMap().getGroundTiles();

            SquareTower squareTower = new SquareTower(new PointF(mapPoint.getX() + 0.5f,
                    mapPoint.getY() + 0.5f), groundTiles);

            mapView.getGameMap().setBuilding(mapPoint, squareTower);
            mapView.setGold(mapView.getGold() - SquareTower.COST);
        }
    }
}
