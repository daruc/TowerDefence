package com.daruc.towerdefence_02.buildings;

import android.graphics.PointF;

import com.daruc.towerdefence_02.Direction;
import com.daruc.towerdefence_02.Enemy;

import java.util.ArrayList;
import java.util.List;

import static com.daruc.towerdefence_02.Direction.DOWN;
import static com.daruc.towerdefence_02.Direction.LEFT;
import static com.daruc.towerdefence_02.Direction.RIGHT;
import static com.daruc.towerdefence_02.Direction.UP;

/**
 * Created by darek on 06.04.18.
 */

public class SquareTower extends Building implements PowerReceiver {
    private List<Rocket> rockets;
    private PowerGenerator powerGenerator;

    public SquareTower(PointF position) {
        super(position);
        rockets = new ArrayList<>(1);
        Rocket rocket = new Rocket(position);
        rockets.add(rocket);
    }

    public Direction findEnemies(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            PointF enemyPosition = enemy.getPosition();
            if (enemyPosition.x == position.x) {
                if (enemyPosition.y > position.y) {
                    return DOWN;
                } else {
                    return UP;
                }
            } else if (enemyPosition.y == position.y) {
                if (enemyPosition.x > position.x) {
                    return RIGHT;
                } else {
                    return LEFT;
                }
            }
        }
        return null;
    }

    @Override
    public PointF getPosition() {
        return super.getPosition();
    }

    public static int getCost() {
        return 200;
    }

    public List<Rocket> getRockets() {
        return rockets;
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
