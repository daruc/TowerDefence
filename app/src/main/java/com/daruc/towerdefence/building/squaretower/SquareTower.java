package com.daruc.towerdefence.building.squaretower;

import android.graphics.PointF;

import com.daruc.towerdefence.Direction;
import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.powergenerator.PowerGenerator;
import com.daruc.towerdefence.building.PowerReceiver;
import com.daruc.towerdefence.building.Rocket;
import com.daruc.towerdefence.building.Upgradable;

import java.util.ArrayList;
import java.util.List;


public class SquareTower extends Building implements PowerReceiver, Upgradable {
    private static final int MAX_LEVEL = 10;
    public static final int COST = 200;

    private int level = 1;
    private List<Rocket> rockets;
    private PowerGenerator powerGenerator;
    private int enemyDirectionIndex = 0;
    private List<Direction> directions;

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

    public SquareTower(PointF position, GroundType[][] groundTiles) {
        super(position);
        directions = possibleDirections(position, groundTiles);
        rockets = new ArrayList<>(directions.size());
        for (int i = 0; i < directions.size(); ++i) {
            Rocket rocket = new Rocket(position);
            rockets.add(rocket);
        }

        drawingStrategy = new SquareTowerDrawingStrategy(this);
    }

    public List<Direction> possibleDirections(PointF position, GroundType[][] groundTiles) {
        List<Direction> directions = new ArrayList<>(4);

        int towerX = Math.round(position.x - 0.5f);
        int towerY = Math.round(position.y - 0.5f);
        int height = groundTiles.length;
        int width = groundTiles[0].length;
        for (int y = towerY + 1; y < height; ++y) {
            if (groundTiles[y][towerX] == GroundType.PATH) {
                directions.add(Direction.DOWN);
                break;
            }
        }
        for (int y = towerY - 1; y >= 0; --y) {
            if (groundTiles[y][towerX] == GroundType.PATH) {
                directions.add(Direction.UP);
                break;
            }
        }
        for (int x = towerX + 1; x < width; ++x) {
            if (groundTiles[towerY][x] == GroundType.PATH) {
                directions.add(Direction.RIGHT);
                break;
            }
        }
        for (int x = towerX - 1; x >= 0; --x) {
            if (groundTiles[towerY][x] == GroundType.PATH) {
                directions.add(Direction.LEFT);
                break;
            }
        }
        return directions;
    }

    public Direction findEnemies(List<Enemy> enemies) {
        boolean areDead = true;
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                areDead = false;
                break;
            }
        }
        if (areDead) {
            return null;
        }

        enemyDirectionIndex = (enemyDirectionIndex + 1) % directions.size();
        return directions.get(enemyDirectionIndex);
    }

    @Override
    public PointF getPosition() {
        return super.getPosition();
    }

    @Override
    public int getCost() {
        return COST;
    }

    public List<Rocket> getRockets() {
        return rockets;
    }

    @Override
    public boolean withPowerGenerator() {
        return powerGenerator != null;
    }

    @Override
    public void setPowerGenerator(PowerGenerator powerGenerator) {
        this.powerGenerator = powerGenerator;
    }
}
