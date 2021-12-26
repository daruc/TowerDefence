package com.daruc.towerdefence.building.squaretower;

import android.graphics.PointF;

import com.daruc.towerdefence.GroundType;
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
    public void build(MapView mapView, int mapX, int mapY) {
        GroundType groundType = mapView.getGameMap().getGround(mapX, mapY);
        Building building = mapView.getGameMap().getBuilding(mapX, mapY);

        if (groundType == GroundType.GRASS && building == null) {

            GroundType[][] groundTiles = mapView.getGameMap().getGroundTiles();

            SquareTower squareTower =
                    new SquareTower(new PointF(mapX + 0.5f, mapY + 0.5f), groundTiles);

            mapView.getGameMap().setBuilding(mapX, mapY, squareTower);
            mapView.setGold(mapView.getGold() - SquareTower.COST);
        }
    }
}
