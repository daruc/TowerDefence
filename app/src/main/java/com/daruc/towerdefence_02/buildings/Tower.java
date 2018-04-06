package com.daruc.towerdefence_02.buildings;

import android.graphics.PointF;

import com.daruc.towerdefence_02.Enemy;
import com.daruc.towerdefence_02.Vectors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darek on 04.04.18.
 */

public class Tower extends Building implements PowerReceiver {
    private List<Bullet> bullets = new ArrayList<>(1);
    private int maxBullets = 1;
    private float scopeRadius = 1.5f;  // in map units
    private float radius = 0.25f;
    private List<Enemy> enemies = new ArrayList<>();
    private int level = 1;
    private float frequency = 0f;
    private long lastShotTime = -2000;
    private PowerGenerator powerGenerator;

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

    public void upgrade() {
        ++level;
        if (level <= 3){
            radius += 0.1f;

            bullets.add(new Bullet(position));
            for (Bullet bullet : bullets) {
                bullet.setDamage(bullet.getDamage() + 1);
            }
        }
        scopeRadius += 0.5f;
    }

    public float getRadius() {
        return radius;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }

    public boolean isReady() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastShotTime) / 1000f >= frequency;
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