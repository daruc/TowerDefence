package com.daruc.towerdefence.mapview;

import android.graphics.PointF;
import android.view.View;

import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.antitanktower.AntiTankTowerBuildingStrategy;
import com.daruc.towerdefence.building.areadamagetower.AreaDamageTowerBuildingStrategy;
import com.daruc.towerdefence.building.barracks.BarracksBuildingStrategy;
import com.daruc.towerdefence.building.boat.Boat;
import com.daruc.towerdefence.building.boat.BoatBuildingStrategy;
import com.daruc.towerdefence.building.icetower.IceTowerBuildingStrategy;
import com.daruc.towerdefence.building.lasertower.LaserTowerBuildingStrategy;
import com.daruc.towerdefence.building.powergenerator.PowerGeneratorBuildingStrategy;
import com.daruc.towerdefence.building.radar.RadarBuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTowerBuildingStrategy;
import com.daruc.towerdefence.building.squaretower.SquareTowerBuildingStrategy;
import com.daruc.towerdefence.building.volcanictower.VolcanicTowerBuildingStrategy;
import com.daruc.towerdefence.building.wall.WallBuildingStrategy;

public class MapViewOnClickListener implements View.OnClickListener {

    private enum BuildingSelection {
        ROUND_TOWER(0, new RoundTowerBuildingStrategy()),
        SQUARE_TOWER(1, new SquareTowerBuildingStrategy()),
        POWER_GENERATOR(2, new PowerGeneratorBuildingStrategy()),
        BOAT(3, new BoatBuildingStrategy()),
        ANTI_TANK_TOWER(4, new AntiTankTowerBuildingStrategy()),
        BARRACKS(5, new BarracksBuildingStrategy()),
        ICE_TOWER(6, new IceTowerBuildingStrategy()),
        VOLCANIC_TOWER(7, new VolcanicTowerBuildingStrategy()),
        LASER_TOWER(8, new LaserTowerBuildingStrategy()),
        RADAR(9, new RadarBuildingStrategy()),
        WALL(10, new WallBuildingStrategy()),
        AREA_DAMAGE_TOWER(11, new AreaDamageTowerBuildingStrategy());

        int index;
        BuildingStrategy buildingStrategy;

        BuildingSelection(int idx, BuildingStrategy buildingStrategy)  {
            index = idx;
            this.buildingStrategy = buildingStrategy;
        }

        public static BuildingSelection fromIndex(int idx) {
            for (BuildingSelection buildingSelection : values()) {
                if (buildingSelection.index == idx) {
                    return buildingSelection;
                }
            }
            return null;
        }

        public BuildingStrategy getBuildingStrategy() {
            return buildingStrategy;
        }
    }

    private MapView mapView;

    public MapViewOnClickListener(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public void onClick(View v) {
        MapPoint mapPoint = getMapPoint();

        if (isMapPointOutsideMap(mapPoint)) {
            return;
        }

        Building selectedBuilding = mapView.getSelectedBuilding();
        if (selectedBuilding instanceof Boat) {
            moveBoat(selectedBuilding, mapPoint);

        } else {
           buildBuilding(mapPoint);
        }

        updateSelectedBuilding(selectedBuilding, mapPoint);
    }

    private boolean isMapPointOutsideMap(MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        return mapPoint.getX() >= gameMap.getWidth()
                || mapPoint.getY() >= gameMap.getHeight();
    }

    private void moveBoat(Building building, MapPoint mapPoint) {
        Boat boat = (Boat) building;
        boat.move(mapPoint);
    }

    private void buildBuilding(MapPoint mapPoint) {
        int buildingSelectionIndex = mapView.getBuildingSelectionIdx();

        BuildingStrategy buildingStrategy =
                BuildingSelection.fromIndex(buildingSelectionIndex)
                        .getBuildingStrategy();

        if (buildingStrategy.hasEnoughGold(mapView)) {
            buildingStrategy.build(mapView, mapPoint);
        }
    }

    private void updateSelectedBuilding(Building selectedBuilding, MapPoint mapPoint) {
        GameMap gameMap = mapView.getGameMap();
        Building newSelectedBuilding = gameMap.getBuilding(mapPoint);
        if (newSelectedBuilding == selectedBuilding) {
            mapView.setSelectedBuilding(null);
        } else {
            selectedBuilding = gameMap.getBuilding(mapPoint);
            mapView.setSelectedBuilding(selectedBuilding);
        }
    }

    public MapPoint getMapPoint() {
        float tileSize = mapView.getTileSize();
        PointF mapCoordinates = new PointF();
        mapCoordinates.x = mapView.getTouchCoordinates().x / tileSize;
        mapCoordinates.y = mapView.getTouchCoordinates().y / tileSize;
        return new MapPoint(mapCoordinates);
    }
}
