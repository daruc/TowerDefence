package com.daruc.towerdefence.building;

import android.graphics.PointF;

import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.MapDimensions;
import com.daruc.towerdefence.Vector;


public class Bullet {
    private PointF mapSpacePosition;
    private final PointF towerPosition;
    private PointF direction;
    private Enemy target;
    private boolean free = true;

    private int damage = 1;
    private float speed = 1f;   // map units per second

    public Bullet(PointF position) {
        towerPosition = position;
        this.mapSpacePosition = new PointF(position.x, position.y);
    }

    public PointF getMapSpacePosition() {
        return mapSpacePosition;
    }

    public void update(float deltaTimeSeconds) {

        if (hasTarget()) {
            direction = calculateDirection();
            float displacement = deltaTimeSeconds * speed;
            mapSpacePosition.x += direction.x * displacement;
            mapSpacePosition.y += direction.y * displacement;
        } else if (!free) {
            moveStraight(deltaTimeSeconds);
        }
    }

    private PointF calculateDirection() {
        Vector targetVector = new Vector(target.getPosition());
        Vector positionVector = new Vector(mapSpacePosition);
        return targetVector.minus(positionVector).getUnitVector().convertToPointF();
    }

    public void moveStraight(float deltaTimeSeconds) {
        float displacement = deltaTimeSeconds * speed;
        mapSpacePosition.x += direction.x * displacement;
        mapSpacePosition.y += direction.y * displacement;
    }

    public void setTarget(Enemy enemy) {
        target = enemy;
        free = false;
    }

    public boolean isFree() {
        return free;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean isOutOfMap(GameMap gameMap) {
        MapDimensions mapDimensions = gameMap.mapDimensions();
        return (mapSpacePosition.x < 0f || mapSpacePosition.x > mapDimensions.width ||
                mapSpacePosition.y < 0f || mapSpacePosition.y > mapDimensions.height);
    }

    public void reset() {
        target = null;
        mapSpacePosition = new PointF(towerPosition.x, towerPosition.y);
        free = true;
    }

    public boolean collision() {
        if (target == null) {
            return false;
        }
        float distance = calculateDistance();
        return distance <= target.getRadius();
    }

    private float calculateDistance() {
        Vector targetVector = new Vector(target.getPosition());
        Vector positionVector = new Vector(mapSpacePosition);
        return targetVector.minus(positionVector).length();
    }

    public Enemy getTarget() {
        return target;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage)  {
        this.damage = damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
