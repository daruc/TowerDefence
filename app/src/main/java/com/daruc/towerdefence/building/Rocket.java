package com.daruc.towerdefence.building;

import android.graphics.PointF;
import android.util.Pair;

import com.daruc.towerdefence.Direction;
import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.MapDimensions;
import com.daruc.towerdefence.MapLoader;
import com.daruc.towerdefence.Updatable;
import com.daruc.towerdefence.Vector;

import java.util.List;


public class Rocket implements Updatable {
    private PointF position;
    private final PointF towerPosition;
    private Direction direction;
    private Enemy lastVictim;

    private int damage = 5;
    private float speed = 4f;

    public Rocket(PointF towerPosition) {
        this.towerPosition = towerPosition;
        position = new PointF(towerPosition.x, towerPosition.y);
    }

    public void update(float deltaTimeSeconds) {
        if (direction == null) {
            return;
        }

        float displacement = deltaTimeSeconds * speed;

        switch (direction) {
            case RIGHT:
                position.x += displacement;
                break;
            case LEFT:
                position.x -= displacement;
                break;
            case DOWN:
                position.y += displacement;
                break;
            case UP:
                position.y -= displacement;
                break;
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void reset() {
        direction = null;
        position.x = towerPosition.x;
        position.y = towerPosition.y;
        lastVictim = null;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isFree() {
        return direction == null;
    }

    public PointF getPosition() {
        return position;
    }

    public boolean isOutOfMap(GameMap gameMap) {
        MapDimensions mapDimensions = gameMap.mapDimensions();
        return (position.x < 0f || position.x > mapDimensions.width ||
                position.y < 0f || position.y > mapDimensions.height);
    }

    public Enemy collision(List<Enemy> enemies) {
        if (isFree()) {
            return null;
        }

        for (Enemy enemy : enemies) {
            if (enemy == lastVictim) continue;

            PointF enemyPosition = enemy.getPosition();
            float distance = calculateDistance(enemyPosition);
            if (distance <= enemy.getRadius()) {
                lastVictim = enemy;
                return enemy;
            }
        }

        return null;
    }

    public float calculateDistance(PointF enemyPosition) {
        Vector positionVector = new Vector(position);
        Vector enemyVector = new Vector(enemyPosition);
        return positionVector.minus(enemyVector).length();
    }

    public int getDamage() {
        return damage;
    }
}
