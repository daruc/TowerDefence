package com.daruc.towerdefence.building;

import android.graphics.PointF;

import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.Vector;


public class Bullet {
    private PointF position;
    private final PointF towerPosition;
    private PointF direction;
    private Enemy target;
    private boolean free = true;

    private int damage = 1;
    private float speed = 4f;   // map units per second

    public Bullet(PointF position) {
        towerPosition = position;
        this.position = new PointF(position.x, position.y);
    }

    public PointF getPosition() {
        return position;
    }

    public void move(long deltaTimeMillis) {

        if (hasTarget()) {
            direction = calculateDirection();
            float displacement = ((float) deltaTimeMillis / 1000) * speed;
            position.x += direction.x * displacement;
            position.y += direction.y * displacement;
        } else if (!free) {
            moveStraight(deltaTimeMillis);
        }
    }

    private PointF calculateDirection() {
        Vector targetVector = new Vector(target.getPosition());
        Vector positionVector = new Vector(position);
        return targetVector.minus(positionVector).getUnitVector().convertToPointF();
    }

    public void moveStraight(long deltaTimeMillis) {
        float displacement = (deltaTimeMillis / 1000) * speed;
        position.x += direction.x * displacement;
        position.y += direction.y * displacement;
    }

    public void setTarget(Enemy enemy) {
        target = enemy;
        free = false;
    }

    public boolean isFree() {
        return free;
    }

    public boolean hasTarget() {
        return target!= null;
    }

    public boolean isOutOfMap(GameMap gameMap) {
        PointF mapDimensions = gameMap.mapDimensions();
        return (position.x < 0f || position.x > mapDimensions.x ||
                position.y < 0f || position.y > mapDimensions.y);
    }

    public void reset() {
        target = null;
        position = new PointF(towerPosition.x, towerPosition.y);
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
        Vector positionVector = new Vector(position);
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
