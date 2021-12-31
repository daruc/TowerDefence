package com.daruc.towerdefence.building.roundtower;

import android.graphics.PointF;
import android.media.AudioManager;
import android.media.SoundPool;

import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.Updatable;
import com.daruc.towerdefence.Vector;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Bullet;
import com.daruc.towerdefence.building.powergenerator.PowerGenerator;
import com.daruc.towerdefence.building.PowerReceiver;
import com.daruc.towerdefence.building.Upgradable;
import com.daruc.towerdefence.mapview.MapView;

import java.util.ArrayList;
import java.util.List;


public class RoundTower extends Building implements PowerReceiver, Upgradable, Updatable {

    private static final int MAX_LEVEL = 10;
    public static final int COST = 10;

    private List<Bullet> bullets = new ArrayList<>(1);
    private int maxBullets = 1;
    private float scopeRadius = 1.5f;  // in map units
    private float radius = 0.25f;
    private List<Enemy> enemies = new ArrayList<>();
    private int level = 1;
    private float shotFrequencyPerSecond = 1.0f;
    private float secondsToShot = 0.0f;
    private PowerGenerator powerGenerator;
    private MapView mapView;
    private SoundPool soundPool;
    private int shotSoundId;

    public RoundTower(MapView mapView, PointF coordinates) {
        super(coordinates);
        this.mapView = mapView;
        position = new PointF(coordinates.x, coordinates.y);
        for (int i = 0; i < maxBullets; ++i) {
            bullets.add(new Bullet(position));
        }
        drawingStrategy = new RoundTowerDrawingStrategy(this);
        soundPool = mapView.getSoundPool();
        shotSoundId = mapView.getSoundId();
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

    @Override
    public boolean withPowerGenerator() {
        return powerGenerator != null;
    }

    @Override
    public void setPowerGenerator(PowerGenerator powerGenerator) {
        this.powerGenerator = powerGenerator;
    }

    @Override
    public void update(float deltaSeconds) {
        updateLastShotTime(deltaSeconds);
        GameMap gameMap = mapView.getGameMap();
        findEnemies(gameMap.getEnemies());

        for (Bullet bullet : getBullets()) {
            updateBullet(deltaSeconds, bullet);
        }
    }

    private void updateBullet(float deltaSeconds, Bullet bullet) {
        GameMap gameMap = mapView.getGameMap();

        if (isReadyToShot(bullet)) {
            bullet.setTarget(getEnemies().get(0));
            resetLastShotTime();
        } else if (shouldResetBullet(bullet)) {
            bullet.reset();
        }

        bullet.update(deltaSeconds);

        if (bullet.collision()) {
            Enemy target = bullet.getTarget();
            target.decreaseHealth(bullet.getDamage());
            soundPool.play(shotSoundId, 1, 1, 1, 0, 1.0f);

            if (target.isDead()) {
                mapView.setGold(mapView.getGold() + 5);
            }
            bullet.reset();
        }

        if (bullet.isOutOfMap(gameMap)) {
            bullet.reset();
        }
    }

    private void updateLastShotTime(float deltaSeconds) {
        this.secondsToShot = this.secondsToShot - deltaSeconds;
        if (this.secondsToShot < 0.0f) {
            this.secondsToShot = 0.0f;
        }
    }

    private boolean isReadyToShot(Bullet bullet) {
        return bullet.isFree() &&
                !bullet.hasTarget() &&
                hasEnemies() &&
                secondsToShot == 0.0f;
    }

    private void resetLastShotTime() {
        secondsToShot = shotFrequencyPerSecond;
    }

    private boolean shouldResetBullet(Bullet bullet) {
        return bullet.hasTarget() && bullet.getTarget().isDead();
    }

}
