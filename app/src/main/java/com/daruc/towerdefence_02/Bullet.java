package com.daruc.towerdefence_02;

import android.graphics.PointF;

/**
 * Created by darek on 04.04.18.
 */

public class Bullet {
    private PointF towerPosition;
    private PointF position;
    private float speed = 7f;   // map units per second
    private Enemy target;
    private boolean free = true;
    private int damage = 1;

    public Bullet(PointF position) {
        towerPosition = position;
        this.position = new PointF(position.x, position.y);
    }

    public PointF getPosition() {
        return position;
    }

    public void move(float deltaTimeMillis) {

        if (hasTarget()) {
            PointF direction = Vectors.unitVector(position, target.getPosition());

            float replacement = (deltaTimeMillis / 1000) * speed;
            direction.x *= replacement;
            direction.y *= replacement;

            position.x += direction.x;
            position.y += direction.y;
        }
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
        float distance = Vectors.distance(target.getPosition(), position);
        return distance <= target.getRadius();
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
}
