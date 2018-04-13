package com.daruc.towerdefence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.daruc.towerdefence.building.AntiTankTower;
import com.daruc.towerdefence.building.AreaDamageTower;
import com.daruc.towerdefence.building.Barracks;
import com.daruc.towerdefence.building.Boat;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.Bullet;
import com.daruc.towerdefence.building.Castle;
import com.daruc.towerdefence.building.IceTower;
import com.daruc.towerdefence.building.LaserTower;
import com.daruc.towerdefence.building.PowerGenerator;
import com.daruc.towerdefence.building.Radar;
import com.daruc.towerdefence.building.Rocket;
import com.daruc.towerdefence.building.RoundTower;
import com.daruc.towerdefence.building.SquareTower;
import com.daruc.towerdefence.building.VolcanicTower;
import com.daruc.towerdefence.building.Wall;

import java.io.InputStream;

/**
 * Created by darek on 02.04.18.
 */

public class MapView extends View {
    private Building selectedBuilding;
    private GameMap gameMap;
    private Paint paint = new Paint();
    private int tileSize = 150;
    private int gold = 1_000_000;

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    private int soundId;

    private int refreshTime = 20;

    private Handler handler = new Handler();
    private PointF touchCoordinates = new PointF();

    private UpdateMap updateMap;

    private Paint paintGroundLine;
    private Paint paintEnemy;

    private Bitmap grassBitmap;
    private Bitmap waterBitmap;
    private Bitmap forestBitmap;
    private Bitmap pathBitmap;
    private Bitmap stoneBitmap;

    private int buildingSelectionIdx = 0;
    private enum BuildingSelection {
        ROUND_TOWER(0), SQUARE_TOWER(1), POWER_GENERATOR(2),
        BOAT(3), ANTI_TANK_TOWER(4), BARRACKS(5),
        ICE_TOWER(6), VOLCANIC_TOWER(7), LASER_TOWER(8),
        RADAR(9), WALL(10), AREA_DAMAGE_TOWER(11);

        int index;
        BuildingSelection(int idx)  {
            index = idx;
        }

        public static BuildingSelection fromIndex(int idx) {
            for (BuildingSelection buildingSelection : values()) {
                if (buildingSelection.index == idx) {
                    return buildingSelection;
                }
            }
            return null;
        }
    }

    public MapView(Context context, int mapId) {
        super(context);

        mediaPlayer = MediaPlayer.create(context, R.raw.sound);
        soundId = soundPool.load(context, R.raw.sound, 1);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    touchCoordinates.x = event.getX();
                    touchCoordinates.y = event.getY();
                }
                return false;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionX = (int) (touchCoordinates.x / tileSize);
                int positionY = (int) (touchCoordinates.y / tileSize);

                switch (BuildingSelection.fromIndex(buildingSelectionIdx)) {

                    case ROUND_TOWER:
                        if (gold >= RoundTower.COST) {
                            RoundTower roundTower = gameMap.buildRoundTower(positionX, positionY);
                            if (roundTower != null) {
                                setGold(gold - roundTower.getCost());
                            }
                        }
                        break;

                    case SQUARE_TOWER:
                        if (gold >= SquareTower.COST) {
                            SquareTower squareTower = gameMap.buildSquareTower(positionX, positionY);
                            if (squareTower != null) {
                                setGold(gold - squareTower.getCost());
                            }
                        }
                        break;

                    case POWER_GENERATOR:
                        if (gold >= PowerGenerator.COST) {
                            PowerGenerator generator = gameMap.buildPowerGenerator(positionX, positionY);
                            if (generator != null) {
                                setGold(gold - generator.getCost());
                            }
                        }
                        break;

                    case BOAT:
                        if (gold >= Boat.COST) {
                            Boat boat = gameMap.buildBoat(positionX, positionY);
                            if (boat != null) {
                                setGold(gold - boat.getCost());
                            }
                        }
                        break;

                    case ANTI_TANK_TOWER:
                        if (gold >= AntiTankTower.COST) {
                            AntiTankTower antiTankTower = gameMap.buildAntiTankTower(positionX, positionY);
                            if (antiTankTower != null) {
                                setGold(gold - antiTankTower.getCost());
                            }
                        }
                        break;
                    case BARRACKS:
                        if (gold >= AntiTankTower.COST) {
                            Barracks barracks = gameMap.buildBarracks(positionX, positionY);
                            if (barracks != null) {
                                setGold(gold - barracks.getCost());
                            }
                        }
                        break;
                    case ICE_TOWER:
                        if (gold >= IceTower.COST) {
                            IceTower iceTower = gameMap.buildIceTower(positionX, positionY);
                            if (iceTower != null) {
                                setGold(gold - iceTower.getCost());
                            }
                        }
                        break;
                    case VOLCANIC_TOWER:
                        if (gold >= VolcanicTower.COST) {
                            VolcanicTower volcanicTower = gameMap.buildVolcanicTower(positionX, positionY);
                            if (volcanicTower != null) {
                                setGold(gold - volcanicTower.getCost());
                            }
                        }
                        break;
                    case LASER_TOWER:
                        if (gold >= LaserTower.COST) {
                            LaserTower laserTower = gameMap.buildLaserTower(positionX, positionY);
                            if (laserTower != null) {
                                setGold(gold - laserTower.getCost());
                            }
                        }
                        break;
                    case RADAR:
                        if (gold >= Radar.COST) {
                            Radar radar = gameMap.buildRadar(positionX, positionY);
                            if (radar != null) {
                                setGold(gold - radar.getCost());
                            }
                        }
                        break;
                    case WALL:
                        if (gold >= Wall.COST) {
                            Wall wall = gameMap.buildWall(positionX, positionY);
                            if (wall != null) {
                                setGold(gold - wall.getCost());
                            }
                        }
                        break;
                    case AREA_DAMAGE_TOWER:
                        if (gold >= AreaDamageTower.COST) {
                            AreaDamageTower areaDamageTower = gameMap.buildAreaDamageTower(positionX, positionY);
                            if (areaDamageTower != null) {
                                setGold(gold - areaDamageTower.getCost());
                            }
                        }
                        break;
                }

                Building newSelectedBuilding = gameMap.getBuilding(positionX, positionY);
                if (newSelectedBuilding == selectedBuilding) {
                    selectedBuilding = null;
                } else {
                    selectedBuilding = gameMap.getBuilding(positionX, positionY);
                }
            }
        });

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int positionX = (int) (touchCoordinates.x / tileSize);
                int positionY = (int) (touchCoordinates.y / tileSize);

                Building building = gameMap.getBuilding(positionX, positionY);
                if (building != null && !(building instanceof Castle)) {
                    int cost = building.getCost() / 2;
                    if (gold >= cost) {
                        gameMap.removeBuilding(positionX, positionY);
                        setGold(gold - cost);
                    }
                } else if (building == null &&
                        gameMap.getGround(positionX, positionY) == GroundType.FOREST) {

                    if (gold >= 100) {
                        gameMap.removeForest(positionX, positionY);
                        setGold(gold - 100);
                    }
                }
                return true;
            }
        });

        InputStream mapResource = context.getResources().openRawResource(mapId);
        gameMap = new GameMap(mapResource);

        updateMap = new UpdateMap(this);
        updateMap.run();

        initPaints();
    }

    private void initPaints() {
        paintGroundLine = new Paint();
        paintGroundLine.setColor(Color.argb(128, 160, 160, 160));
        paintGroundLine.setStrokeWidth(3f);

        paintEnemy = new Paint();
        paintEnemy.setColor(Color.BLACK);
        paintEnemy.setStyle(Paint.Style.FILL);



        grassBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        pathBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.path);
        stoneBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stone);
        waterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        forestBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.forest);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGround(canvas);
        drawEnemies(canvas);
        drawBuildings(canvas);
        postInvalidateDelayed(refreshTime);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h > w) {
            tileSize = w / gameMap.getWidth();
        } else {
            tileSize = h / gameMap.getHeight();
        }
    }

    private void drawGround(Canvas canvas) {
        int mapHeight = gameMap.getHeight();
        int mapWidth = gameMap.getWidth();

        for (int h = 0; h < mapHeight; ++h) {
            for (int w = 0; w < mapWidth; ++w) {
                GroundType groundType = gameMap.getGround(w, h);
                int tileX = w * tileSize;
                int tileY = h * tileSize;
                drawGroundTile(canvas, tileX, tileY, groundType);
            }
        }

        drawGroundLines(canvas);
    }

    private void drawGroundTile(Canvas canvas, int tileX, int tileY, GroundType type) {
        Rect source = new Rect(0, 0, grassBitmap.getWidth(), grassBitmap.getHeight());
        RectF dest = new RectF(tileX, tileY, tileX + tileSize, tileY + tileSize);
        switch (type) {
            case PATH:
                canvas.drawBitmap(pathBitmap, source, dest, paint);
                break;
            case GRASS:
                canvas.drawBitmap(grassBitmap, source, dest, paint);
                break;
            case STONE:
                canvas.drawBitmap(stoneBitmap, source, dest, paint);
                break;
            case WATER:
                canvas.drawBitmap(waterBitmap, source, dest, paint);
                break;
            case CASTLE:
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(new Rect(tileX, tileY, tileX + tileSize, tileY + tileSize), paint);
                break;
            case FOREST:
                canvas.drawBitmap(forestBitmap, source, dest, paint);
                break;
        }
    }

    private void drawGroundLines(Canvas canvas) {
        int mapHeight = gameMap.getHeight();
        int mapWidth = gameMap.getWidth();

        float stopX = mapWidth * tileSize;
        for (int h = 1; h < mapHeight; ++h) {
            float startY = h  * tileSize;
            canvas.drawLine(0f, startY, stopX, startY, paintGroundLine);
        }

        float stopY = mapHeight * tileSize;
        for (int w = 1; w < mapWidth; ++w) {
            float startX = w * tileSize;
            canvas.drawLine(startX, 0f, startX, stopY, paintGroundLine);
        }
    }

    private void drawEnemies(Canvas canvas) {

        for (Enemy enemy : gameMap.getEnemies()) {
            if (enemy.isActive()) {
                PointF position = enemy.getPosition();
                position = new PointF(position.x, position.y);
                position.x *= tileSize;
                position.y *= tileSize;
                canvas.drawCircle(position.x, position.y, enemy.getRadius() * tileSize,
                        paintEnemy);
            }
        }
    }

    private void drawBuildings(Canvas canvas) {
        for (Building building : gameMap.getBuildings()) {
            building.getDrawingStrategy().draw(canvas, this);
        }
    }

    public void restartGame(Context context) {
        InputStream mapResource = context.getResources().openRawResource(R.raw.map_1);
        gameMap = new GameMap(mapResource);
        gold = 1_000_000;
        refreshGoldView();

        updateMap.stop();
        updateMap = new UpdateMap(this);
        updateMap.run();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public int getSoundId() {
        return soundId;
    }

    public Handler getHandler() {
        return handler;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        if (gold < 0) {
            this.gold = 0;
        } else {
            this.gold = gold;
        }
        refreshGoldView();
    }

    private void refreshGoldView() {
        GameView gameView = (GameView) getParent();
        TextView goldView = gameView.getGoldView();
        goldView.setText("Gold: " + String.valueOf(gold));
    }

    public void nextWave() {
        gameMap.nextWave();
    }

    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    public void setBuildingSelectionIndex(int index) {
        buildingSelectionIdx = index;
    }

    public int getBuildingSelectionIdx() {
        return buildingSelectionIdx;
    }

    public int getTileSize() {
        return tileSize;
    }
}
