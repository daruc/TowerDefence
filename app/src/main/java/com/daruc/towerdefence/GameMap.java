package com.daruc.towerdefence;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Pair;

import com.daruc.towerdefence.building.antitanktower.AntiTankTower;
import com.daruc.towerdefence.building.areadamagetower.AreaDamageTower;
import com.daruc.towerdefence.building.barracks.Barracks;
import com.daruc.towerdefence.building.boat.Boat;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.castle.Castle;
import com.daruc.towerdefence.building.icetower.IceTower;
import com.daruc.towerdefence.building.lasertower.LaserTower;
import com.daruc.towerdefence.building.powergenerator.PowerGenerator;
import com.daruc.towerdefence.building.PowerReceiver;
import com.daruc.towerdefence.building.radar.Radar;
import com.daruc.towerdefence.building.roundtower.RoundTower;
import com.daruc.towerdefence.building.squaretower.SquareTower;
import com.daruc.towerdefence.building.volcanictower.VolcanicTower;
import com.daruc.towerdefence.building.wall.Wall;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMap {
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

        while (getGround(currentPoint) != GroundType.CASTLE) {
            Point east = new Point(currentPoint);
            east.x -= 1;
            Point west = new Point(currentPoint);
            west.x += 1;
            Point north = new Point(currentPoint);
            north.y -= 1;
            Point south = new Point(currentPoint);
            south.y += 1;

            if (!points.contains(east) &&
                    (getGround(east) == GroundType.PATH || getGround(east) == GroundType.CASTLE)) {

                points.add(east);
                currentPoint = east;

            } else if (!points.contains(west) &&
                    (getGround(west) == GroundType.PATH || getGround(west) == GroundType.CASTLE)) {

                points.add(west);
                currentPoint = west;

            } else if (!points.contains(north) &&
                    (getGround(north) == GroundType.PATH ||
                            getGround(north) == GroundType.CASTLE)) {

                points.add(north);
                currentPoint = north;

            } else if (!points.contains(south) &&
                    (getGround(south) == GroundType.PATH || getGround(south) == GroundType.CASTLE)) {

                points.add(south);
                currentPoint = south;
            }
        }
        enemiesPath = points;
    }

    private Point findOrigin() {
        for (int i = 0; i < getHeight(); ++i) {
            if (getGround(0, i) == GroundType.PATH) {
                return new Point(0, i);
            }

            if (getGround(getWidth() - 1, i) == GroundType.PATH) {
                return new Point(getWidth()-1, i);
            }
        }

        for (int i = 0; i < getWidth(); ++i) {
            if (getGround(i, 0) == GroundType.PATH) {
                return new Point(i, 0);
            }

            if (getGround(i, getHeight() - 1) == GroundType.PATH) {
                return new Point (i, getHeight() - 1);
            }
        }
        throw new IllegalStateException("Map doesn't have path origin.");
    }

    private void addEnemies() {

        for (int i = 0; i < nEnemies; ++i) {
            float waitingTime = i * (70.0f / speed);
            Enemy enemy = new Enemy(enemiesPath, waitingTime);
            enemies.add(enemy);
        }
    }

    private void initBuildings() {
        buildings = new Building[getHeight()][];
        for (int i = 0; i < getHeight(); ++i) {
            buildings[i] = new Building[getWidth()];
        }

        // find castle
        for (int h = 0; h < getHeight(); ++h) {
            for (int w = 0; w < getWidth(); ++w) {
                if (groundTiles[h][w] == GroundType.CASTLE) {
                    buildings[h][w] = new Castle(new PointF(w + 0.5f, h + 0.5f));
                    buildingsList.add(buildings[h][w]);
                    return;
                }
            }
        }
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

    private void connectPowerGenerator(int generatorX, int generatorY) {

        for (int x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                int receiverX = generatorX + x;
                int receiverY = generatorY + y;

                if (isOutOfTiles(receiverX, receiverY)) {
                    continue;
                }

                if (buildings[generatorY + y][generatorX + x] instanceof PowerReceiver) {
                    PowerReceiver powerReceiver =
                            (PowerReceiver) buildings[generatorY + y][generatorX + x];
                    
                    powerReceiver.
                            setPowerGenerator((PowerGenerator) buildings[generatorY][generatorX]);
                }
            }
        }
    }

    private PowerGenerator findPowerGenerator(int buildingX, int buildingY) {

        int generatorX, generatorY;

        for (int x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                generatorX = buildingX + x;
                generatorY = buildingY + y;

                if (isOutOfTiles(generatorX, generatorY)) {
                    continue;
                }

                if (buildings[generatorY][generatorX] instanceof PowerGenerator) {
                    return (PowerGenerator) buildings[generatorY][generatorX];
                }
            }
        }
        return null;
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

    public boolean removeBuilding(int x, int y) {
        boolean result = (buildings[y][x] != null);
        buildingsList.remove(buildings[y][x]);

        if (buildings[y][x] instanceof PowerGenerator) {
            disconnectPowerGenerator(x, y); // fix it
        }
        buildings[y][x] = null;
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
