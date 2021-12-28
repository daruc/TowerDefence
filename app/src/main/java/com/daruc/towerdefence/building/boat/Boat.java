package com.daruc.towerdefence.building.boat;

import android.graphics.PointF;

import com.daruc.towerdefence.AStarPathFinder;
import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapDimensions;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.Vector;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Upgradable;

import java.util.LinkedList;
import java.util.List;


public class Boat extends Building implements Upgradable {

    private static final int MAX_LEVEL = 10;
    public static final int COST = 250;
    private float scope = 1.5f;
    private int level = 1;

    private List<PointF> movePath = null;
    private float moveSpeed = 1f;
    private GameMap gameMap;

    public Boat(PointF position, GameMap gameMap) {
        super(position);
        drawingStrategy = new BoatDrawingStrategy(this);
        this.gameMap = gameMap;
    }

    @Override
    public boolean upgrade() {
        if (getLevel() < getMaxLevel()) {
            ++level;
            return true;
        }
        return false;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    @Override
    public int getCost() {
        return COST;
    }

    public float getScope() {
        return scope;
    }

    public void setPath(List<PointF> path) {
        movePath = path;
    }

    public void update(long deltaTimeMillis) {
        if (isMoving()) {
            Vector direction = calculateDirection();
            Vector offset = direction.multiply((deltaTimeMillis / 1000.0f) * moveSpeed);
            PointF offsetPointF = offset.convertToPointF();
            position.x += offsetPointF.x;
            position.y += offsetPointF.y;

            if (isGoalReached()) {
                position = movePath.get(0);
                movePath.remove(0);
            }
        }
    }

    private boolean isMoving() {
        return movePath != null && !movePath.isEmpty();
    }

    private Vector calculateDirection() {
        Vector positionVector = new Vector(position);
        Vector movePathVector = new Vector(movePath.get(0));
        return movePathVector.minus(positionVector).getUnitVector();
    }

    private float calculateDistance() {
        Vector positionVector = new Vector(position);
        Vector movePathVector = new Vector(movePath.get(0));
        return positionVector.minus(movePathVector).length();
    }

    private boolean isGoalReached() {
        return calculateDistance() < 0.05f;
    }

    public void move(MapPoint destination) {

        if (isDestinationEmptyWithWater(destination)) {
            List<MapPoint> moveBoatPath = findMovePath(destination);

            if (isMoveBoatPath(moveBoatPath)) {
                List<PointF> pointFMoveBoatPath = mapMapPointToPointF(moveBoatPath);
                setPath(pointFMoveBoatPath);
                MapPoint source = new MapPoint(position);
                gameMap.moveBuilding(source, destination);
            }
        }
    }

    private boolean isDestinationEmptyWithWater(MapPoint destination) {
        Building buildingInNewPosition = gameMap.getBuilding(destination);
        GroundType groundTypeInNewPosition = gameMap.getGround(destination);
        return (buildingInNewPosition == null && groundTypeInNewPosition == GroundType.WATER);
    }

    private boolean isMoveBoatPath(List<MapPoint> moveBoatPath) {
        return moveBoatPath != null;
    }

    private List<PointF> mapMapPointToPointF(List<MapPoint> mapPoints) {
        List<PointF> pointFMoveBoatPath = new LinkedList<>();
        for (MapPoint mapPoint : mapPoints) {
            PointF pointF = mapMapPointToPointF(mapPoint);
            pointFMoveBoatPath.add(pointF);
        }
        return pointFMoveBoatPath;
    }

    private PointF mapMapPointToPointF(MapPoint mapPoint) {
        return new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f);
    }

    private List<MapPoint> findMovePath(MapPoint destination) {
        boolean[][] obstaclesMap = mapGroundTypeMapToObstacleMap();
        AStarPathFinder aStarPathFinder = new AStarPathFinder(obstaclesMap);
        MapPoint source = new MapPoint(position);
        return aStarPathFinder.find(source, destination);
    }

    private boolean[][] mapGroundTypeMapToObstacleMap() {
        MapDimensions mapDimensions = gameMap.mapDimensions();
        boolean map[][] = new boolean[mapDimensions.height][];

        for (int h = 0; h < mapDimensions.height; ++h) {
            map[h] = new boolean[mapDimensions.width];

            for (int w = 0; w < mapDimensions.width; ++w) {
                MapPoint mapPoint = new MapPoint(w, h);
                map[h][w] = isNotObstacle(mapPoint);
            }
        }

        return map;
    }

    private boolean isNotObstacle(MapPoint mapPoint) {
        GroundType groundType = gameMap.getGround(mapPoint);
        Building building = gameMap.getBuilding(mapPoint);

        return groundType == GroundType.WATER &&
                building == null;
    }
}
