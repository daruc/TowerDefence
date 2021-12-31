package com.daruc.towerdefence.building.powergenerator;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapAdapter;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class PowerGeneratorBuildingStrategy implements BuildingStrategy {
    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return PowerGenerator.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        if (isCorrectPlaceToBuild(mapView, mapPoint)) {
            buildPowerGenerator(mapView, mapPoint);
        }
    }

    private boolean isCorrectPlaceToBuild(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.GRASS && building == null;
    }

    private void buildPowerGenerator(MapView mapView, MapPoint mapPoint) {
        PowerGenerator generator = newPowerGenerator(mapPoint);
        GameMap gameMap = mapView.getGameMap();
        placeNewBuildingOnMap(gameMap, generator, mapPoint);
        decreaseGold(mapView);
    }

    private PowerGenerator newPowerGenerator(MapPoint mapPoint) {
        return new PowerGenerator(new PointF(mapPoint.getX() + 0.5f,
                mapPoint.getY() + 0.5f));
    }

    private void placeNewBuildingOnMap(GameMap gameMap, PowerGenerator powerGenerator,
                                       MapPoint mapPoint) {
        gameMap.setBuilding(mapPoint, powerGenerator);
    }

    private void decreaseGold(MapView mapView) {
        mapView.setGold(mapView.getGold() - PowerGenerator.COST);
    }
}
