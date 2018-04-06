package com.daruc.towerdefence_02;

import android.graphics.Point;
import android.graphics.PointF;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by darek on 02.04.18.
 */

public class GameMap {
    private GroundType[][] groundTiles;
    private Building[][] buildings;
    private List<Point> enemiesPath;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Building> buildingsList = new ArrayList<>();
    private PointF mapDimensions;
    private int nEnemies = 3;

    private int wave = 1;
    private int health = 5;
    private float speed = 1f;

    public GameMap(InputStream mapResource) {
        loadFromFile(mapResource);
        computeEnemiesPath();
        addEnemies();
        initBuildings();
    }

    private void loadFromFile(InputStream mapResource) {
        Scanner scanner = new Scanner(mapResource);
        scanner.useDelimiter("\\A");
        String mapStr = scanner.hasNext() ? scanner.next() : "";
        mapStr = mapStr.trim();

        int width = mapStr.indexOf("\n");
        Matcher matcher = Pattern.compile("\r\n|\r|\n").matcher(mapStr);
        int lines = 1;
        while (matcher.find()) {
            ++lines;
        }
        int height = lines;

        StringBuffer stringBuffer = new StringBuffer();
        for (char ch : mapStr.toCharArray()) {
            if (ch != '\n') {
                stringBuffer.append(ch);
            }
        }
        mapStr = stringBuffer.toString();

        groundTiles = new GroundType[height][];
        for (int h = 0; h < height; ++h) {
            groundTiles[h] = new GroundType[width];
            for (int w = 0; w < width; ++w) {
                int index = (h * width) + w;
                groundTiles[h][w] = GroundType.fromChar(mapStr.charAt(index));
            }
        }

        mapDimensions = new PointF(width, height);
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

    private void addEnemies() {

        for (int i = 0; i < nEnemies; ++i) {
            enemies.add(new Enemy(enemiesPath, i));
        }
    }

    public GroundType getGround(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return GroundType.STONE;
        return groundTiles[y][x];
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

    public PointF mapDimensions() {
        return mapDimensions;
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

            if (getGround(getWidth()-1, i) == GroundType.PATH) {
                return new Point(getWidth()-1, i);
            }
        }

        for (int i = 0; i < getWidth(); ++i) {
            if (getGround(i, 0) == GroundType.PATH) {
                return new Point(i, 0);
            }

            if (getGround(i, getHeight()-1) == GroundType.PATH) {
                return new Point (i, getHeight()-1);
            }
        }
        throw new IllegalStateException("Map doesn't have path origin.");
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Tower buildTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            Tower tower = new Tower(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = tower;
            buildingsList.add(tower);
            return tower;
        }
        return null;
    }

    public Building getBuilding(int x, int y) {
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
            return null;
        }
        return buildings[y][x];
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
        return null;
    }

    public boolean removeBuilding(int x, int y) {
        boolean result = (buildings[y][x] != null);
        buildingsList.remove(buildings[y][x]);
        buildings[y][x] = null;
        return result;
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
        health += 15;
        speed += 0.1f;
        enemies.clear();
        addEnemies(nEnemies, health, speed);
    }

    private void addEnemies(int number, int health, float speed) {
        for (int i = 0; i < number; ++i) {
            float waitingTime = i * (1f / speed);
            Enemy enemy = new Enemy(enemiesPath, waitingTime);
            enemy.setHealth(health);
            enemy.setSpeed(speed);
            enemies.add(enemy);
        }
    }
}
