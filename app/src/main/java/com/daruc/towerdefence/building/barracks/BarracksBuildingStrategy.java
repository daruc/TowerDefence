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
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildBarracks(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS && building == null
                && gameMap.isNextToPath(mapPoint);
    }

    private void buildBarracks(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        Barracks barracks = newBarracks(mapPoint);
        placeNewBuildingOnMap(gameMap, barracks, mapPoint);
        decreaseGold(mapView, mapPoint);
    }

    private Barracks newBarracks(MapPoint mapPoint) {
        return new Barracks(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
    }

    private void placeNewBuildingOnMap(GameMap gameMap, Building building, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, building);
    }

    private void decreaseGold(MapView mapView, MapPoint mapPoint) {
        mapView.setGold(mapView.getGold() - Barracks.COST);
    }
}
