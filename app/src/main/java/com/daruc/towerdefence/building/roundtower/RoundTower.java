package com.daruc.towerdefence.building.roundtower;

import android.graphics.PointF;

import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.Vector;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Bullet;
import com.daruc.towerdefence.building.powergenerator.PowerGenerator;
import com.daruc.towerdefence.building.PowerReceiver;
import com.daruc.towerdefence.building.Upgradable;

import java.util.ArrayList;
import java.util.List;


public class RoundTower extends Building implements PowerReceiver, Upgradable {

    private static final int MAX_LEVEL = 10;
    public static final int COST = 10;

    private List<Bullet> bullets = new ArrayList<>(1);
    private int maxBullets = 1;
    private float scopeRadius = 1.5f;  // in map units
    private float radius = 0.25f;
    private List<Enemy> enemies = new ArrayList<>();
    private int level = 1;
    private float frequency = 0f;
    private long lastShotTime = -2000;
    private PowerGenerator powerGenerator;

    public RoundTower(PointF coordinates) {
        super(coordinates);
        position = new PointF(coordinates.x, coordinates.y);
        for (int i = 0; i < maxBullets; ++i) {
            bullets.add(new Bullet(position));
        }
        drawingStrategy = new RoundTowerDrawingStrategy(this);
    }

    public int getCost() {
        return COST;
    }

    public void findEnemies(List<Enemy> allEnemies) {
        enemies.clear();
        for (Enemy enemy : allEnemies) {
            if (!enemy.isActive()) continue;
            float distance = calculateDistance(enemy.getPosition(), position);
            if (distance <= scopeRadius) {
                enemies.add(enemy);
            }
        }
    }

    public float calculateDistance(PointF enemy, PointF position) {
        Vector enemyVector = new Vector(enemy);
        Vector positionVector = new Vector(position);
        return enemyVector.minus(positionVector).length();
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

    @Override
    public boolean upgrade() {
        if (getLevel() >= getMaxLevel()) {
            return false;
        }
        ++level;
        if (level <= 3) {
            radius += 0.1f;

            bullets.add(new Bullet(position));
            for (Bullet bullet : bullets) {
                bullet.setDamage(bullet.getDamage() + 1);
            }
        }
        scopeRadius += 0.5f;
        return true;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
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
