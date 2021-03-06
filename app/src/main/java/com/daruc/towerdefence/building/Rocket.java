package com.daruc.towerdefence.building;

import android.graphics.PointF;

import com.daruc.towerdefence.Direction;
import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.Vectors;

import java.util.List;

/**
 * Created by darek on 06.04.18.
 */

public class Rocket {
    private PointF position;
    private final PointF towerPosition;
    private Direction direction;
    private Enemy lastVictim;

    private int damage = 5;
    private float speed = 4f;

    public Rocket(PointF towerPosition) {
        this.towerPosition = towerPosition;
        position = Vectors.copy(towerPosition);
    }

    public void move(long deltaTimeMillis) {
        if (direction == null) {
            return;
        }

        float displacement = (deltaTimeMillis / 1000f) * speed;

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
        PointF mapDimensions = gameMap.mapDimensions();
        return (position.x < 0f || position.x > mapDimensions.x ||
                position.y < 0f || position.y > mapDimensions.y);
    }

    public Enemy collision(List<Enemy> enemies) {
        if (isFree()) {
            return null;
        }

        for (Enemy enemy : enemies) {
            if (enemy == lastVictim) continue;

            PointF enemyPosition = enemy.getPosition();
            float distance = Vectors.distance(position, enemyPosition);
            if (distance <= enemy.getRadius()) {
                lastVictim = enemy;
                return enemy;
            }
        }

        return null;
    }

    public int getDamage() {
        return damage;
    }
}
