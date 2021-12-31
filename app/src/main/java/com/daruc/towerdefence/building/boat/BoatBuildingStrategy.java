package com.daruc.towerdefence.building.boat;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapAdapter;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class BoatBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return Boat.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildBoat(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.WATER && building == null;
    }

    private void buildBoat(MapView mapView, MapPoint mapPoint) {
        Boat boat = newBoat(mapView, mapPoint);
        GameMap gameMap = mapView.getGameMap();
        placeNewBoatOnMap(gameMap, boat, mapPoint);
        decreaseGold(mapView);
    }

    private Boat newBoat(MapView mapView, MapPoint mapPoint) {
        return new Boat(new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f),
                mapView.getGameMap());
    }

    private void placeNewBoatOnMap(GameMap gameMap, Boat boat, MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, boat);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - Boat.COST);
    }
}
