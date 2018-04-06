package com.daruc.towerdefence_02.buildings;

import android.graphics.PointF;

import com.daruc.towerdefence_02.Direction;
import com.daruc.towerdefence_02.Enemy;
import com.daruc.towerdefence_02.GameMap;
import com.daruc.towerdefence_02.Vectors;

import java.util.List;

/**
 * Created by darek on 06.04.18.
 */

public class Rocket {
    private PointF position;
    private PointF towerPosition;
    private float speed = 4f;
    private int damage = 5;
    private Direction direction;
    private Enemy lastVictim;

    public Rocket(PointF towerPosition) {
        this.towerPosition = towerPosition;
        position = Vectors.copy(towerPosition);
    }

    public void move(long deltaTimeMillis) {
        if (direction == null) {
            return;
        }

        float deltaTime = deltaTimeMillis / 1000f;

        switch (direction) {
            case RIGHT:
                position.x += deltaTime * speed;
                break;
            case LEFT:
                position.x -= deltaTime * speed;
                break;
            case DOWN:
                position.y += deltaTime * speed;
                break;
            case UP:
                position.y -= deltaTime * speed;
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