package com.daruc.towerdefence.building.volcanictower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class VolcanicTowerBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return VolcanicTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();

        if (isCorrectPlaceToBuild(gameMap, mapPoint)) {

            VolcanicTower volcanicTower = new VolcanicTower(new PointF(mapPoint.getX() + 0.5f,
                    mapPoint.getY() + 0.5f));

            gameMap.setBuilding(mapPoint, volcanicTower);
            mapView.setGold(mapView.getGold() - VolcanicTower.COST);
        }
    }

    public boolean isCorrectPlaceToBuild(GameMap gameMap, MapPoint mapPoint) {
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS &&
                building == null &&
                gameMap.isNextToPath(mapPoint);
    }
}
