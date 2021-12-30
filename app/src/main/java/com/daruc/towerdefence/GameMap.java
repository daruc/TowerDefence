package com.daruc.towerdefence;

import android.graphics.Point;
import android.graphics.PointF;

import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.castle.Castle;
import com.daruc.towerdefence.building.powergenerator.PowerGenerator;
import com.daruc.towerdefence.building.PowerReceiver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public static final float ENEMY_WAITING_TIME_FACTOR = 70.0f;
    private GroundType[][] groundTiles;
    private Building[][] buildings;

    private List<Point> enemiesPath;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Building> buildingsList = new ArrayList<>();
    private MapDimensions mapDimensions;

    private int wave = 1;
    private int nEnemies = 3;
    private int health = 5;
    private float speed = 1f;

    public GameMap(InputStream mapResource) {
        loadFromFile(mapResource);
        computeEnemiesPath();
        addEnemies();
        initBuildings();
    }

    private void loadFromFile(InputStream mapResource) {
        MapLoader mapLoader = new MapLoader();
        mapLoader.loadFromFile(mapResource);
        groundTiles = mapLoader.getGroundTiles();
        mapDimensions = mapLoader.getMapDimensions();
    }

    private void computeEnemiesPath() {
        List<Point> points = new ArrayList<>();
        Point currentPoint = findOrigin();
        points.add(currentPoint);

        while (isCastleReached(currentPoint)) {
            Point east = new Point(currentPoint);
            east.x -= 1;
            Point west = new Point(currentPoint);
            west.x += 1;
            Point north = new Point(currentPoint);
            north.y -= 1;
            Point south = new Point(currentPoint);
            south.y += 1;

            if (isNewPathVertex(points, east)) {
                points.add(east);
                currentPoint = east;
            } else if (isNewPathVertex(points, west)) {
                points.add(west);
                currentPoint = west;
            } else if (isNewPathVertex(points, north)) {
                points.add(north);
                currentPoint = north;
            } else if (isNewPathVertex(points, south)) {
                points.add(south);
                currentPoint = south;
            }
        }
        enemiesPath = points;
    }

    private boolean isNewPathVertex(List<Point> points, Point newVertex) {
        return !points.contains(newVertex) &&
               (getGround(newVertex) == GroundType.PATH ||
               getGround(newVertex) == GroundType.CASTLE);
    }

    private boolean isCastleReached(Point currentPoint) {
        return getGround(currentPoint) != GroundType.CASTLE;
    }

    private Point findOrigin() {
        for (int i = 0; i < getHeight(); ++i) {
            if (isPathOnLeftEdge(i)) {
                return new Point(0, i);
            }

            if (isPathOnRightEdge(i)) {
                return new Point(getWidth()-1, i);
            }
        }

        for (int i = 0; i < getWidth(); ++i) {
            if (isPathOnTopEdge(i)) {
                return new Point(i, 0);
            }

            if (isPathOnBottomEdge(i)) {
                return new Point (i, getHeight() - 1);
            }
        }
        throw new IllegalStateException("Map doesn't have path origin.");
    }

    private boolean isPathOnLeftEdge(int mapPointY) {
        return getGround(0, mapPointY) == GroundType.PATH;
    }

    private boolean isPathOnRightEdge(int mapPointY) {
        return getGround(getWidth() - 1, mapPointY) == GroundType.PATH;
    }

    private boolean isPathOnTopEdge(int mapPointX) {
        return getGround(mapPointX, 0) == GroundType.PATH;
    }

    private boolean isPathOnBottomEdge(int mapPointX) {
        return getGround(mapPointX, getHeight() - 1) == GroundType.PATH;
    }

    private void addEnemies() {
        for (int i = 0; i < nEnemies; ++i) {
            float waitingTime = calculateEnemyWaitingTime(i);
            Enemy enemy = new Enemy(enemiesPath, waitingTime);
            enemies.add(enemy);
        }
    }

    private float calculateEnemyWaitingTime(int numberInLine) {
        return numberInLine * (ENEMY_WAITING_TIME_FACTOR / speed);
    }

    private void initBuildings() {
        initBuildingsArray();
        initCastle();
    }

    private void initBuildingsArray() {
        buildings = new Building[getHeight()][];
        for (int i = 0; i < getHeight(); ++i) {
            buildings[i] = new Building[getWidth()];
        }
    }

    private void initCastle() {
        MapPoint mapPoint = findCastleCoordinates();
        PointF pointF = new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f);
        buildings[mapPoint.getY()][mapPoint.getX()] = new Castle(pointF);
        buildingsList.add(buildings[mapPoint.getY()][mapPoint.getX()]);
    }

    private MapPoint findCastleCoordinates() {
        for (int h = 0; h < getHeight(); ++h) {
            for (int w = 0; w < getWidth(); ++w) {
                MapPoint mapPoint = new MapPoint(w, h);
                if (isCastle(mapPoint)) {
                    return mapPoint;
                }
            }
        }
        throw new RuntimeException("Map does not contain the castle.");
    }

    private boolean isCastle(MapPoint mapPoint) {
        return groundTiles[mapPoint.getY()][mapPoint.getX()] == GroundType.CASTLE;
    }

    public GroundType getGround(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return GroundType.STONE;
        return groundTiles[y][x];
    }

    public GroundType getGround(MapPoint mapPoint) {
        return getGround(mapPoint.getX(), mapPoint.getY());
    }

    public GroundType getGround(Point point) {
        return getGround(point.x, point.y);
    }

    public int getHeight() {
        return groundTiles.length;
    }

    public int getWidth() {
        return groundTiles[0].length;
    }

    public MapDimensions mapDimensions() {
        return mapDimensions;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    private boolean isOutOfTiles(int x, int y) {
        return (x < 0 || x >= getWidth() ||
                y < 0 || y >= getHeight());
    }

    public boolean isNextToPath(MapPoint mapPoint) {
        int x = mapPoint.getX();
        int y = mapPoint.getY();

        return (!isOutOfTiles(x, y - 1) && groundTiles[y - 1][x] == GroundType.PATH) ||
                (!isOutOfTiles(x, y + 1) && groundTiles[y + 1][x] == GroundType.PATH) ||
                (!isOutOfTiles(x - 1, y) && groundTiles[y][x - 1] == GroundType.PATH) ||
                (!isOutOfTiles(x + 1, y) && groundTiles[y][x + 1] == GroundType.PATH);
    }

    public Building getBuilding(MapPoint mapPoint) {
        if (mapPoint.getX() < 0 ||
                mapPoint.getX() > getWidth() ||
                mapPoint.getY() < 0 ||
                mapPoint.getY() > getHeight()) {
            return null;
        }
        return buildings[mapPoint.getY()][mapPoint.getX()];
    }

    public void setBuilding(MapPoint mapPoint, Building building) {
        buildings[mapPoint.getY()][mapPoint.getX()] = building;
        buildingsList.add(building);
    }

    public void moveBuilding(MapPoint currentPosition, MapPoint newPosition) {
        Building currentPositionBuilding
                = buildings[currentPosition.getY()][currentPosition.getX()];

        Building newPositionBuilding
                = buildings[newPosition.getY()][newPosition.getX()];

        throwIfThereIsNoBuilding(currentPositionBuilding);
        throwIfNewPositionIsOccupied(newPositionBuilding);

        buildings[newPosition.getY()][newPosition.getX()]
                = buildings[currentPosition.getY()][currentPosition.getX()];
    }

    private void throwIfThereIsNoBuilding(Building currentPositionBuilding) {
        if (currentPositionBuilding == null) {
            throw new RuntimeException(
                    "Can not move building: there is no building in the current position.");
        }
    }

    private void throwIfNewPositionIsOccupied(Building newPositionBuilding) {
        if (newPositionBuilding != null) {
            throw new RuntimeException(
                    "Can not move building: there is already a building in the new position.");
        }
    }

    public List<Building> getBuildings() {
        return buildingsList;
    }

    public Castle getCastle() {
        for (Building building : buildingsList) {
            if (building instanceof Castle) {
                return (Castle) building;
            }
        }
        throw new RuntimeException("Castle not found.");
    }

    public boolean removeBuilding(MapPoint mapPoint) {
        boolean result = (buildings[mapPoint.getY()][mapPoint.getX()] != null);
        buildingsList.remove(buildings[mapPoint.getY()][mapPoint.getX()]);

        if (buildings[mapPoint.getY()][mapPoint.getX()] instanceof PowerGenerator) {
            disconnectPowerGenerator(mapPoint.getX(), mapPoint.getY()); // fix it
        }
        buildings[mapPoint.getY()][mapPoint.getX()] = null;
        return result;
    }

    private void disconnectPowerGenerator(int generatorX, int generatorY) {

        for (int y = -1; y < 2; ++y) {
            for (int x = -1; x < 2; ++x) {

                int receiverX = generatorX + x;
                int receiverY = generatorY + y;

                if (isOutOfTiles(receiverX, receiverY)) {
                    continue;
                }

                if (buildings[generatorY + y][generatorX + x] instanceof PowerReceiver) {
                    PowerReceiver powerReceiver = (PowerReceiver) buildings[generatorY + y][generatorX + x];
                    powerReceiver.setPowerGenerator(null);
                }
            }
        }
    }

    public boolean removeForest(int x, int y) {
        if (groundTiles[y][x] == GroundType.FOREST) {
            groundTiles[y][x] = GroundType.GRASS;
            return true;
        } else {
            return false;
        }
    }

    public boolean enemiesDead() {
        for (Enemy enemy: enemies) {
            if (enemy.isActive()) {
                return false;
            }
        }
        return true;
    }

    public void nextWave() {
        nEnemies += 1;
        health += 3;
        speed += 0.1f;
        enemies.clear();
        addEnemies(nEnemies, health, speed);
    }

    private void addEnemies(int number, int health, float speed) {
        for (int i = 0; i < number; ++i) {
            float waitingTime = i * (0.7f / speed);
            Enemy enemy = new Enemy(enemiesPath, waitingTime);
            enemy.setHealth(health);
            enemy.setSpeed(speed);
            enemies.add(enemy);
        }
    }

    public GroundType[][] getGroundTiles() {
        return groundTiles;
    }
}
