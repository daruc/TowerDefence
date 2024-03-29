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
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildVolcanicTower(mapView, mapPoint);
        }
    }

    public boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS &&
                building == null &&
                gameMap.isNextToPath(mapPoint);
    }

    private void buildVolcanicTower(MapView mapView, MapPoint mapPoint) {
        VolcanicTower volcanicTower = newVolcanicTower(mapPoint);
        GameMap gameMap = mapView.getGameMap();
        setNewBuildingOnMap(gameMap, volcanicTower, mapPoint);
        decreaseGold(mapView);
    }

    private VolcanicTower newVolcanicTower(MapPoint mapPoint) {
        return new VolcanicTower(new PointF(mapPoint.getX() + 0.5f,
                mapPoint.getY() + 0.5f));
    }

    private void setNewBuildingOnMap(GameMap gameMap, VolcanicTower volcanicTower,
                                     MapPoint mapPoint) {

        gameMap.setBuilding(mapPoint, volcanicTower);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - VolcanicTower.COST);
    }
}
