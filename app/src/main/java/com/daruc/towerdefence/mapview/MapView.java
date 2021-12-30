package com.daruc.towerdefence.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.daruc.towerdefence.Enemy;
import com.daruc.towerdefence.GameMap;
import com.daruc.towerdefence.GameView;
import com.daruc.towerdefence.GroundType;
import com.daruc.towerdefence.MapPoint;
import com.daruc.towerdefence.R;
import com.daruc.towerdefence.UpdateMap;
import com.daruc.towerdefence.building.antitanktower.AntiTankTowerBuildingStrategy;
import com.daruc.towerdefence.building.areadamagetower.AreaDamageTowerBuildingStrategy;
import com.daruc.towerdefence.building.barracks.BarracksBuildingStrategy;
import com.daruc.towerdefence.building.boat.BoatBuildingStrategy;
import com.daruc.towerdefence.building.Building;
import com.daruc.towerdefence.building.BuildingStrategy;
import com.daruc.towerdefence.building.icetower.IceTowerBuildingStrategy;
import com.daruc.towerdefence.building.lasertower.LaserTowerBuildingStrategy;
import com.daruc.towerdefence.building.powergenerator.PowerGeneratorBuildingStrategy;
import com.daruc.towerdefence.building.radar.RadarBuildingStrategy;
import com.daruc.towerdefence.building.volcanictower.VolcanicTowerBuildingStrategy;
import com.daruc.towerdefence.building.roundtower.RoundTowerBuildingStrategy;
import com.daruc.towerdefence.building.squaretower.SquareTowerBuildingStrategy;
import com.daruc.towerdefence.building.wall.WallBuildingStrategy;

import java.io.InputStream;


public class MapView extends SurfaceView implements Runnable {

    private Thread thread;
    private volatile boolean playing;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private long lastTime;

    private Building selectedBuilding;
    private GameMap gameMap;
    private Paint paint = new Paint();
    private int tileSize = 150;
    private int gold = 1_000_000;

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    private int soundId;

    private int refreshTime = 20;

    private PointF touchCoordinates = new PointF();

    private UpdateMap updateMap;

    private Paint paintGroundLine;

    private Bitmap roundTowerBitmap;
    private Bitmap squareTowerBitmap;
    private Bitmap powerGeneratorBitmap;
    private Bitmap boatBitmap;

    private int buildingSelectionIdx = 0;

    private enum BuildingSelection {
        ROUND_TOWER(0, new RoundTowerBuildingStrategy()),
        SQUARE_TOWER(1, new SquareTowerBuildingStrategy()),
        POWER_GENERATOR(2, new PowerGeneratorBuildingStrategy()),
        BOAT(3, new BoatBuildingStrategy()),
        ANTI_TANK_TOWER(4, new AntiTankTowerBuildingStrategy()),
        BARRACKS(5, new BarracksBuildingStrategy()),
        ICE_TOWER(6, new IceTowerBuildingStrategy()),
        VOLCANIC_TOWER(7, new VolcanicTowerBuildingStrategy()),
        LASER_TOWER(8, new LaserTowerBuildingStrategy()),
        RADAR(9, new RadarBuildingStrategy()),
        WALL(10, new WallBuildingStrategy()),
        AREA_DAMAGE_TOWER(11, new AreaDamageTowerBuildingStrategy());

        int index;
        BuildingStrategy buildingStrategy;

        BuildingSelection(int idx, BuildingStrategy buildingStrategy)  {
            index = idx;
            this.buildingStrategy = buildingStrategy;
        }

        public static BuildingSelection fromIndex(int idx) {
            for (BuildingSelection buildingSelection : values()) {
                if (buildingSelection.index == idx) {
                    return buildingSelection;
                }
            }
            return null;
        }

        public BuildingStrategy getBuildingStrategy() {
            return buildingStrategy;
        }
    }

    public MapView(Context context, int mapId) {
        super(context);

        surfaceHolder = getHolder();

        initSounds(context);

        setOnTouchListener(new MapViewOnTouchListener(this));
        setOnClickListener(new MapViewOnClickListener(this));
        setOnLongClickListener(new MapViewOnLongClickListener(this));

        initMap(context, mapId);
        initPaints();

        resume();
    }

    private void initSounds(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.sound);
        soundId = soundPool.load(context, R.raw.sound, 1);
    }

    private void initMap(Context context, int mapId) {
        InputStream mapResource = context.getResources().openRawResource(mapId);
        gameMap = new GameMap(mapResource);
        updateMap = new UpdateMap(this);
    }

    private void initPaints() {
        paintGroundLine = new Paint();
        paintGroundLine.setColor(Color.argb(128, 160, 160, 160));
        paintGroundLine.setStrokeWidth(3f);

        roundTowerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.round_tower);
        squareTowerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.square_tower);
        powerGeneratorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.power_generator);
        boatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boat);
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
        MapPoint mapPoint = new MapPoint(tileX, tileY);
        type.getDrawingStrategy().draw(canvas, this, mapPoint);
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
                enemy.getDrawingStrategy().draw(canvas, this);
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
        final TextView goldView = gameView.getGoldView();

        goldView.post(new Runnable() {
            @Override
            public void run() {
                goldView.setText("Gold: " + String.valueOf(gold));
            }
        });
    }

    public void nextWave() {
        gameMap.nextWave();
    }

    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    public void setSelectedBuilding(Building building) {
        selectedBuilding = building;
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

    public Bitmap getRoundTowerBitmap() {
        return roundTowerBitmap;
    }

    public Bitmap getSquareTowerBitmap()  {
        return squareTowerBitmap;
    }

    public Bitmap getPowerGeneratorBitmap() {
        return powerGeneratorBitmap;
    }

    public Bitmap getBoatBitmap() {
        return boatBitmap;
    }

    @Override
    public void run() {
        lastTime = System.currentTimeMillis();
        while (playing) {
            long deltaTime = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            if (deltaTime < refreshTime) {
                try {
                    Thread.sleep(refreshTime - deltaTime);
                    deltaTime = refreshTime;
                } catch (InterruptedException e) {
                    Log.e("GAME", "Main loop sleep error.", e);
                }
            }
            updateMap.update(deltaTime);
            draw();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            drawGround(canvas);
            drawEnemies(canvas);
            drawBuildings(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Game", "MapView thread error" ,e);
        }
    }

    public void resume() {
        playing = true;
        thread = new Thread(this);
        thread.start();
    }

    public PointF getTouchCoordinates() {
        return touchCoordinates;
    }

    public void setTouchCoordinates(PointF touchCoordinates) {
        this.touchCoordinates = touchCoordinates;
    }
}
