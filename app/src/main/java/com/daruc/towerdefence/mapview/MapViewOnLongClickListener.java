package com.daruc.towerdefence.mapview;

import android.graphics.PointF;
import android.view.View;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.castle.Castle;

public class MapViewOnLongClickListener implements View.OnLongClickListener {

    private MapView mapView;

    public MapViewOnLongClickListener(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public boolean onLongClick(View v) {
        PointF touchCoordinates = mapView.getTouchCoordinates();
        float tileSize = mapView.getTileSize();
        GameMap gameMap = mapView.getGameMap();


        int positionX = (int) (touchCoordinates.x / tileSize);
        int positionY = (int) (touchCoordinates.y / tileSize);
        MapPoint mapPoint = new MapPoint(positionX, positionY);

        Building building = gameMap.getBuilding(mapPoint);

        if (isForest(mapPoint)) {
            removeForest(mapPoint);
        }
        else if (isRemovableBuilding(building)) {
            removeBuilding(building, mapPoint);
        }

        return true;
    }

    private boolean isRemovableBuilding(Building building) {
        return building != null &&
                !(building instanceof Castle);
    }

    private void removeBuilding(Building building, MapPoint mapPoint) {
        int cost = building.getCost() / 2;
        int gold = mapView.getGold();
        GameMap gameMap = mapView.getGameMap();

        if (gold >= cost) {
            gameMap.removeBuilding(mapPoint);
            mapView.setGold(gold - cost);
        }
    }

    private boolean isForest(MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        return gameMap.getGround(mapPoint) == GroundType.FOREST;
    }

    private void removeForest(MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        int gold = mapView.getGold();

        if (gold >= 100) {
            gameMap.removeForest(mapPoint.getX(), mapPoint.getY());
            mapView.setGold(gold - 100);
        }
    }
}
