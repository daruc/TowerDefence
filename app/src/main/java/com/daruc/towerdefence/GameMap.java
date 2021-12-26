package com.daruc.towerdefence;

import android.graphics.Point;
import android.graphics.PointF;

import com.daruc.towerdefence.building.AntiTankTower;
import com.daruc.towerdefence.building.AreaDamageTower;
import com.daruc.towerdefence.building.Barracks;
import com.daruc.towerdefence.building.Boat;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Castle;
import com.daruc.towerdefence.building.IceTower;
import com.daruc.towerdefence.building.LaserTower;
import com.daruc.towerdefence.building.PowerGenerator;
import com.daruc.towerdefence.building.PowerReceiver;
import com.daruc.towerdefence.building.Radar;
import com.daruc.towerdefence.building.RoundTower;
import com.daruc.towerdefence.building.SquareTower;
import com.daruc.towerdefence.building.VolcanicTower;
import com.daruc.towerdefence.building.Wall;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
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
        Scanner scanner = new Scanner(mapResource);
        scanner.useDelimiter("\\A");
        String mapStr = scanner.hasNext() ? scanner.next() : "";
        mapStr = mapStr.trim();

        int width = mapStr.indexOf("\r");
        Matcher matcher = Pattern.compile("\r\n|\r|\n").matcher(mapStr);
        int lines = 1;
        while (matcher.find()) {
            ++lines;
        }
        int height = lines;

        StringBuffer stringBuffer = new StringBuffer();

        mapStr = mapStr.replaceAll("\r\n|\r|\n", "");

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
            float waitingTime = i * (0.7f / speed);
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





    public List<Enemy> getEnemies() {
        return enemies;
    }

    public SquareTower buildSquareTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            SquareTower squareTower =
                    new SquareTower(new PointF(x + 0.5f, y + 0.5f), groundTiles);
            buildings[y][x] = squareTower;
            buildingsList.add(squareTower);

            PowerGenerator powerGenerator = findPowerGenerator(x, y);
            squareTower.setPowerGenerator(powerGenerator);
            return squareTower;
        }
        return null;
    }

    public PowerGenerator buildPowerGenerator(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            PowerGenerator generator = new PowerGenerator(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = generator;
            buildingsList.add(generator);
            connectPowerGenerator(x, y);
            return generator;
        }
        return null;
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

    public RoundTower buildRoundTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            RoundTower roundTower = new RoundTower(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = roundTower;
            buildingsList.add(roundTower);

            PowerGenerator powerGenerator = findPowerGenerator(x, y);
            roundTower.setPowerGenerator(powerGenerator);
            return roundTower;
        }
        return null;
    }

    public AntiTankTower buildAntiTankTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            AntiTankTower antiTankTower = new AntiTankTower(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = antiTankTower;
            buildingsList.add(antiTankTower);
            return antiTankTower;
        }
        return null;
    }

    public AreaDamageTower buildAreaDamageTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            AreaDamageTower areaDamageTower = new AreaDamageTower(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = areaDamageTower;
            buildingsList.add(areaDamageTower);
            return areaDamageTower;
        }
        return null;
    }

    public Barracks buildBarracks(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null
                && isNextToPath(x, y)) {

            Barracks barracks = new Barracks(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = barracks;
            buildingsList.add(barracks);
            return barracks;
        }
        return null;
    }

    public Boat buildBoat(int x, int y) {
        if (groundTiles[y][x] == GroundType.WATER && buildings[y][x] == null) {
            Boat boat = new Boat(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = boat;
            buildingsList.add(boat);
            return boat;
        }
        return null;
    }

    public IceTower buildIceTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            IceTower iceTower = new IceTower(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = iceTower;
            buildingsList.add(iceTower);
            return iceTower;
        }
        return null;
    }

    public LaserTower buildLaserTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            LaserTower laserTower = new LaserTower(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = laserTower;
            buildingsList.add(laserTower);
            return laserTower;
        }
        return null;
    }

    public Radar buildRadar(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null) {
            Radar radar = new Radar(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = radar;
            buildingsList.add(radar);
            return radar;
        }
        return null;
    }

    public VolcanicTower buildVolcanicTower(int x, int y) {
        if (groundTiles[y][x] == GroundType.GRASS && buildings[y][x] == null && isNextToPath(x, y)) {
            VolcanicTower volcanicTower = new VolcanicTower(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = volcanicTower;
            buildingsList.add(volcanicTower);
            return volcanicTower;
        }
        return null;
    }

    public Wall buildWall(int x, int y) {
        if (groundTiles[y][x] == GroundType.PATH && buildings[y][x] == null) {
            Wall wall = new Wall(new PointF(x + 0.5f, y + 0.5f));
            buildings[y][x] = wall;
            buildingsList.add(wall);
            return wall;
        }
        return null;
    }

    private boolean isNextToPath(int x, int y) {
        return (!isOutOfTiles(x, y - 1) && groundTiles[y - 1][x] == GroundType.PATH) ||
                (!isOutOfTiles(x, y + 1) && groundTiles[y + 1][x] == GroundType.PATH) ||
                (!isOutOfTiles(x - 1, y) && groundTiles[y][x - 1] == GroundType.PATH) ||
                (!isOutOfTiles(x + 1, y) && groundTiles[y][x + 1] == GroundType.PATH);
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

    public Boat moveBoat(int oldPositionX, int oldPositionY, int newPositionX, int newPositionY) {
        Building building = buildings[oldPositionY][oldPositionX];
        if (building instanceof Boat && buildings[newPositionY][newPositionX] == null
                && groundTiles[newPositionY][newPositionX] == GroundType.WATER) {
            Boat boat = (Boat) building;

            List<MapPoint> moveBoatPath = moveBoatPath(new MapPoint(oldPositionX, oldPositionY),
                    new MapPoint(newPositionX, newPositionY));

            if (moveBoatPath != null) {
                List<PointF> pointFMoveBoatPath = new LinkedList<>();
                for (MapPoint mapPoint : moveBoatPath) {
                    PointF pointF = new PointF(mapPoint.getX() + 0.5f, mapPoint.getY() + 0.5f);
                    pointFMoveBoatPath.add(pointF);
                }
                boat.setPath(pointFMoveBoatPath);

                buildings[newPositionY][newPositionX] = boat;
                buildings[oldPositionY][oldPositionX] = null;
                return boat;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private List<MapPoint> moveBoatPath(MapPoint source, MapPoint destination) {
        boolean map[][] = new boolean[getHeight()][];
        for (int h = 0; h < getHeight(); ++h) {
            map[h] = new boolean[getWidth()];
            for (int w = 0; w < getWidth(); ++w) {
                if (groundTiles[h][w] == GroundType.WATER && buildings[h][w] == null) {
                    map[h][w] = true;
                } else {
                    map[h][w] = false;
                }
            }
        }

        AStarPathFinder aStarPathFinder = new AStarPathFinder(map);

        return aStarPathFinder.find(source, destination);
    }
}
