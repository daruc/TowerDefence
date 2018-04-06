package com.daruc.towerdefence_02;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by darek on 04.04.18.
 */

public class Enemy {
    private PointF position;
    private Iterator<PointF> nextPositionIt;
    private PointF nextPosition;
    private float speed = 0.4f;
    private List<PointF> enemiesPath;
    private float waitingTimeSec;
    private float radius = 0.1f;
    private int health = 100;

    public Enemy(List<Point> pEnemiesPath) {
        this(pEnemiesPath, 0f);
    }

    public Enemy(List<Point> pEnemiesPath, float waitingTimeSec) {

        this.waitingTimeSec = waitingTimeSec;

        enemiesPath = new ArrayList<>(pEnemiesPath.size());
        for (Point point : pEnemiesPath) {
            enemiesPath.add(new PointF(point.x + 0.5f, point.y + 0.5f));
        }

        nextPositionIt = enemiesPath.iterator();
        if (nextPositionIt.hasNext()) {
            nextPosition = nextPositionIt.next();
        }
        this.position = new PointF(nextPosition.x, nextPosition.y);
        if (position.x == 0.5f) {
            position.x = -0.5f;
        } else if (position.y == 0.5f) {
            position.y = -0.5f;
        }
    }

    public PointF getPosition() {
        return position;
    }

    public void move(long deltaTimeMillis) {
        float deltaTimeSec = deltaTimeMillis / 1000f;

        if (deltaTimeSec <= waitingTimeSec) {
            waitingTimeSec -= deltaTimeSec;
            return;
        }

        float displacement = speed * deltaTimeSec;
        if (Math.abs(position.x - nextPosition.x) < displacement &&
            Math.abs(position.y - nextPosition.y) < displacement) {  // new position

            position = nextPosition;
            if (nextPositionIt.hasNext()) {
                nextPosition = nextPositionIt.next();
            } else {
                nextPosition = position;
            }

        }else if (position.x < nextPosition.x) {  // move right
            position.x += displacement;

        } else if (position.x > nextPosition.x) {   // move left
            position.x -= displacement;

        } else if (position.y < nextPosition.y) {   // move down
            position.y += displacement;

        } else {   // move up
            position.y -= displacement;
        }
    }

    public float getRadius() {
        return radius;
    }

    public void decreaseHealth(int hp) {
        health -= hp;
        if (health < 0) {
            health = 0;
        }
    }

    public int getHealth() {
        return health;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Enemy) {
            Enemy enemy = (Enemy) obj;
            return enemy.position.equals(((Enemy) obj).position);
        }
        return false;
    }

    public boolean isActive() {
        return health > 0;
    }

    public boolean castleCollision(Castle castle) {
        float castleRadius = castle.getRadius();
        PointF castlePosition = castle.getPosition();
        float distance = Vectors.distance(position, castlePosition);
        return (distance < castleRadius);
    }
}
