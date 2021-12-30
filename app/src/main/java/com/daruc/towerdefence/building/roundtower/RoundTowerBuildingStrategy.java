package com.daruc.towerdefence.building.roundtower;

import android.graphics.PointF;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.mapview.MapView;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;

public class RoundTowerBuildingStrategy implements BuildingStrategy {

    @Override
    public boolean hasEnoughGold(MapView mapView) {
        return RoundTower.COST <= mapView.getGold();
    }

    @Override
    public void build(MapView mapView, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        if (groundType == GroundType.GRASS && building == null) {
            RoundTower roundTower = new RoundTower(new PointF(mapPoint.getX() + 0.5f,
                    mapPoint.getY() + 0.5f));
            gameMap.setBuilding(mapPoint, roundTower);
            mapView.setGold(mapView.getGold() - RoundTower.COST);

            //PowerGenerator powerGenerator = findPowerGenerator(x, y);
            //roundTower.setPowerGenerator(powerGenerator);
        }
    }
}
