package com.daruc.towerdefence;

import android.graphics.Point;
import android.graphics.PointF;

import com.daruc.towerdefence.building.castle.Castle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Enemy implements Updatable {
    private PointF position;
    private Iterator<PointF> nextPositionIt;
    private PointF nextPosition;

    private List<PointF> enemiesPath;
    private float waitingTimeSec;

    private float radius = 0.1f;
    private float speed = 0.01f;
    private int health = 10;

    public Enemy(List<Point> pEnemiesPath) {
        this(pEnemiesPath, 0f);
    }

    public Enemy(List<Point> pEnemiesPath, float waitingTimeSec) {
        this.waitingTimeSec = waitingTimeSec;
        enemiesPath = mapEnemiesPath(pEnemiesPath);
        initNextPosition();
        initPosition();
    }

    private List<PointF> mapEnemiesPath(List<Point> enemiesPath) {
        List<PointF> result = new ArrayList<>(enemiesPath.size());
        for (Point point : enemiesPath) {
            result.add(new PointF(point.x + 0.5f, point.y + 0.5f));
        }
        return result;
    }

    private void initNextPosition() {
        nextPositionIt = enemiesPath.iterator();
        if (nextPositionIt.hasNext()) {
            nextPosition = nextPositionIt.next();
        }
    }

    private void initPosition() {
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

    public void update(float deltaTimeSeconds) {

        if (deltaTimeSeconds <= waitingTimeSec) {
            waitingTimeSec -= deltaTimeSeconds;
            return;
        }

        float displacement = speed * deltaTimeSeconds;
        updateNextPositionIfDestinationAchieved(displacement);
        updatePosition(displacement);
    }

    private void updateNextPositionIfDestinationAchieved(float displacement) {
        if (isDestinationAchieved(displacement)) {
            updateNextPosition();
        }
    }

    private boolean isDestinationAchieved(float displacement) {
        return Math.abs(position.x - nextPosition.x) < displacement &&
                Math.abs(position.y - nextPosition.y) < displacement;
    }

    private void updateNextPosition() {
        position = nextPosition;
        if (nextPositionIt.hasNext()) {
            nextPosition = nextPositionIt.next();
        } else {
            nextPosition = position;
        }
    }

    private void updatePosition(float displacement) {
        Vector currentPosition = new Vector(position);
        Vector destinationPosition = new Vector(nextPosition);
        Vector direction = destinationPosition.minus(currentPosition).getUnitVector();
        Vector displacementVector = direction.multiply(displacement);
        position.x += displacementVector.getX();
        position.y += displacementVector.getY();
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
        return health > 0;  // fix it
    }

    public boolean castleCollision(Castle castle) {
        float castleRadius = castle.getRadius();
        PointF castlePosition = castle.getPosition();
        float distance = calculateDistance(castlePosition);
        return (distance < castleRadius);
    }

    public float calculateDistance(PointF otherPosition) {
        Vector otherPositionVector = new Vector(otherPosition);
        Vector positionVector = new Vector(position);
        return otherPositionVector.minus(positionVector).length();
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void setHealth(int hp) {
        health = hp;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


}
