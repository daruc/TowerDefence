package com.daruc.towerdefence;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import com.daruc.towerdefence.building.boat.Boat;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Bullet;
import com.daruc.towerdefence.building.Rocket;
import com.daruc.towerdefence.building.roundtower.RoundTower;
import com.daruc.towerdefence.building.squaretower.SquareTower;
import com.daruc.towerdefence.mapview.MapView;

public class UpdateMap {
    private static final long REFRESH_TIME = 20;

    private MapView mapView;
    private GameMap gameMap;
    private float refreshTimeSeconds = 20;
    private int soundId;
    private Context context;
    private SoundPool soundPool;
    private boolean stop = false;

    public UpdateMap(MapView mapView) {
        this.mapView = mapView;
        gameMap = mapView.getGameMap();
        soundId = mapView.getSoundId();
        context = mapView.getContext();
        soundPool = mapView.getSoundPool();
    }

    public void update(long pRefreshTime) {
        long steps = pRefreshTime / REFRESH_TIME;
        long lastStepRefreshTime = pRefreshTime % REFRESH_TIME;

        long singleStepRefreshTime = pRefreshTime / steps;
        float singleStepDeltaSeconds = singleStepRefreshTime / 1000f;
        for (int i = 0; i < steps; ++i) {
            updateSingleStep(singleStepDeltaSeconds);
        }
        updateSingleStep(lastStepRefreshTime);
    }

    private void updateSingleStep(float refreshTimeSeconds) {
        this.refreshTimeSeconds = refreshTimeSeconds;
        for (Enemy enemy: gameMap.getEnemies()) {
            if (enemy.isActive()) {
                enemy.update(refreshTimeSeconds);
                if (enemy.castleCollision(gameMap.getCastle())) {
                    enemy.decreaseHealth(100);
                    gameMap.getCastle().decreaseHealth(1);
                    if (gameMap.getCastle().getHealth() == 0) {
                        Log.d("GAME", "Castle destroyed");
                    }
                }
            }
        }

        if (!gameMap.getEnemies().isEmpty() && gameMap.enemiesDead()) {
            gameMap.getEnemies().clear();
        }

        long time = System.currentTimeMillis();
        for (Building building : gameMap.getBuildings()) {
            if (building instanceof RoundTower) {
                updateTower(refreshTimeSeconds, (RoundTower) building);
            } else if (building instanceof SquareTower) {
                updateSquareTower(time, (SquareTower) building);
            } else if (building instanceof Boat) {
                updateBoat((Boat) building);
            }
        }
    }

    private void updateTower(float deltaSeconds, RoundTower roundTower) {
        roundTower.update(deltaSeconds);
    }

    private void updateSquareTower(long time, SquareTower squareTower) {
        for (Rocket rocket : squareTower.getRockets()) {
            if (rocket.isFree()) {
                Direction direction = squareTower.findEnemies(gameMap.getEnemies());
                rocket.setDirection(direction);
            }
            rocket.update(refreshTimeSeconds);
            Enemy victim = rocket.collision(gameMap.getEnemies());
            if (victim != null) {
                victim.decreaseHealth(rocket.getDamage());
            }

            if (rocket.isOutOfMap(gameMap)) {
                rocket.reset();
            }
        }

    }

    private void updateBoat(Boat boat) {
        boat.update(refreshTimeSeconds);
    }

    public void stop() {
        stop = true;
    }
}
