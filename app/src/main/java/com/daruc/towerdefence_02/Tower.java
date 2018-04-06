package com.daruc.towerdefence_02;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darek on 04.04.18.
 */

public class Tower extends Building {
    private List<Bullet> bullets = new ArrayList<>(1);
    private int maxBullets = 1;
    private float scopeRadius = 2f;  // in map units
    private List<Enemy> enemies = new ArrayList<>();

    public Tower(PointF coordinates) {
        super(coordinates);
        position = new PointF(coordinates.x, coordinates.y);
        for (int i = 0; i < maxBullets; ++i) {
            bullets.add(new Bullet(position));
        }
    }

    public static int getCost() {
        return 10;
    }

    public void findEnemies(List<Enemy> allEnemies) {
        enemies.clear();
        for (Enemy enemy : allEnemies) {
            if (!enemy.isActive()) continue;
            float distance = Vectors.distance(enemy.getPosition(), position);
            if (distance <= scopeRadius) {
                enemies.add(enemy);
            }
        }
    }

    public float getScope() {
        return scopeRadius;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public boolean hasEnemies() {
        return !enemies.isEmpty();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
