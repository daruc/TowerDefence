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

        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildIceTower(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS && building == null;
    }

    private void buildIceTower(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();

        IceTower iceTower = buildIceTower(mapPoint);
        placeNewBuildingOnMap(gameMap, iceTower, mapPoint);
        decreaseGold(mapView);
    }

    private IceTower buildIceTower(MapPoint mapPoint) {
        return new IceTower(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f));
    }

    private void placeNewBuildingOnMap(GameMap gameMap, IceTower iceTower, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, iceTower);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - IceTower.COST);
    }
}
