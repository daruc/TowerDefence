package com.daruc.towerdefence_02;

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.daruc.towerdefence_02.buildings.Building;
import com.daruc.towerdefence_02.buildings.Bullet;
import com.daruc.towerdefence_02.buildings.Rocket;
import com.daruc.towerdefence_02.buildings.SquareTower;
import com.daruc.towerdefence_02.buildings.Tower;

/**
 * Created by darek on 05.04.18.
 */

public class UpdateMap implements Runnable {
    private MapView mapView;
    private GameMap gameMap;
    private int refreshTime = 20;
    private int soundId;
    private Context context;
    private SoundPool soundPool;
    private Handler handler;
    private boolean stop = false;

    public UpdateMap(MapView mapView) {
        this.mapView = mapView;
        gameMap = mapView.getGameMap();
        soundId = mapView.getSoundId();
        context = mapView.getContext();
        soundPool = mapView.getSoundPool();
        handler = mapView.getHandler();
    }

    @Override
    public void run() {
        for (Enemy enemy: gameMap.getEnemies()) {
            if (enemy.isActive()) {
                enemy.move(refreshTime);
                if (enemy.castleCollision(gameMap.getCastle())) {
                    enemy.decreaseHealth(100);
                    gameMap.getCastle().decreaseHealth(1);
                    if (gameMap.getCastle().getHealth() == 0) {
                        Log.d("GAME", "Castle destroyed");
                        Toast.makeText(context, "Defeat", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

        if (!gameMap.getEnemies().isEmpty() && gameMap.enemiesDead()) {
            Toast.makeText(context, "Victory", Toast.LENGTH_LONG).show();
            gameMap.getEnemies().clear();
            //gameMap.nextWave();
        }

        long time = System.currentTimeMillis();
        for (Building building : gameMap.getBuildings()) {
            if (building instanceof Tower) {
                updateTower(time, (Tower) building);
            } else if (building instanceof SquareTower) {
                updateSquareTower(time, (SquareTower) building);
            }
        }

        if (!stop) {
            handler.postDelayed(this, refreshTime);
        }
    }

    private void updateTower(long time, Tower tower) {
        tower.findEnemies(gameMap.getEnemies());
        for (Bullet bullet : tower.getBullets()) {
            if (bullet.isFree() && !bullet.hasTarget() && tower.hasEnemies() && tower.isReady()) {
                bullet.setTarget(tower.getEnemies().get(0));
                tower.setLastShotTime(time);
            } else if (bullet.hasTarget() && bullet.getTarget().isDead()) {
                bullet.reset();
            }
            bullet.move(refreshTime);

            if (bullet.collision()) {
                Enemy target = bullet.getTarget();
                target.decreaseHealth(bullet.getDamage());
                soundPool.play(soundId, 1, 1, 1, 0, 1.0f);

                if (target.isDead()) {
                    mapView.setGold(mapView.getGold() + 5);
                }
                bullet.reset();
            }

            if (bullet.isOutOfMap(gameMap)) {
                bullet.reset();
            }

        }
    }

    private void updateSquareTower(long time, SquareTower squareTower) {
        Direction direction = squareTower.findEnemies(gameMap.getEnemies());

        for (Rocket rocket : squareTower.getRockets()) {
            if (direction != null) {
                if (rocket.isFree()) {
                    rocket.setDirection(direction);
                }
                rocket.move(refreshTime);
                Enemy victim = rocket.collision(gameMap.getEnemies());
                if (victim != null) {
                    //Log.d("ENEMY", "rocket-enemy collision" + victim);
                    victim.decreaseHealth(rocket.getDamage());
                }
            }
            if (rocket.isOutOfMap(gameMap) || direction == null) {
                rocket.reset();
            }
        }

    }

    public void stop() {
        stop = true;
    }
}
