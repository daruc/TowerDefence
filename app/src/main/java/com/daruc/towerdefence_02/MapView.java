package com.daruc.towerdefence_02;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by darek on 02.04.18.
 */

public class MapView extends View {
    private Building selectedBuilding;
    private GameMap gameMap;
    private Paint paint = new Paint();
    private int tileSize = 150;

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    private int soundId;

    private int refreshTime = 20;

    private Handler handler = new Handler();
    private PointF touchCoordinates = new PointF();

    private UpdateMap updateMap;

    private Paint paintGroundLine;
    private Paint paintEnemy;
    private Paint paintTower;
    private Paint paintCastle;
    private Paint paintTowerRange;
    private Paint paintBullet;

    private Bitmap grassBitmap;
    private Bitmap waterBitmap;
    private Bitmap forestBitmap;
    private Bitmap pathBitmap;
    private Bitmap stoneBitmap;

    public MapView(Context context) {
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
                gameMap.buildTower(positionX, positionY);

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
                gameMap.removeBuilding(positionX, positionY);
                return true;
            }
        });
        InputStream mapResource = context.getResources().openRawResource(R.raw.map_1);
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

        paintBullet = new Paint();
        paintBullet.setStyle(Paint.Style.FILL);
        paintBullet.setColor(Color.DKGRAY);

        paintTower = new Paint();
        paintTower.setColor(Color.BLUE);
        paintTower.setStyle(Paint.Style.FILL);

        paintTowerRange = new Paint();
        paintTowerRange.setStyle(Paint.Style.STROKE);
        paintTowerRange.setStrokeWidth(4f);
        paintTowerRange.setColor(Color.argb(170, 160, 160, 160));

        paintCastle = new Paint();
        paintCastle.setStyle(Paint.Style.FILL);
        paintCastle.setColor(Color.LTGRAY);

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
                setTileColor(type);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(new Rect(tileX, tileY, tileX + tileSize, tileY + tileSize), paint);
                break;
            case FOREST:
                canvas.drawBitmap(forestBitmap, source, dest, paint);
                break;
        }
    }

    private void setTileColor(GroundType type) {
        switch (type) {
            case PATH:
                paint.setColor(Color.YELLOW);
                break;
            case GRASS:
                paint.setColor(Color.GREEN);
                break;
            case WATER:
                paint.setColor(Color.BLUE);
                break;
            case STONE:
                paint.setColor(Color.GRAY);
                break;
            case CASTLE:
                paint.setColor(Color.RED);
                break;
            case FOREST:
                paint.setColor(Color.argb(255, 0, 120, 0));
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
            PointF position = Vectors.copy(building.getPosition());
            position.x *= tileSize;
            position.y *= tileSize;

            float radius = (tileSize / 2 - 10);
            if (building instanceof Castle) {
                radius = ((Castle) building).getRadius() * tileSize;
                canvas.drawCircle(position.x, position.y, radius, paintCastle);
            } else {
                Tower tower = (Tower) building;
                canvas.drawCircle(position.x, position.y, radius, paintTower);
                float scope = tower.getScope() * tileSize;
                if (building == selectedBuilding) {
                    canvas.drawCircle(position.x, position.y, scope, paintTowerRange);
                }
                drawBullets(canvas, tower);
            }
        }
    }

    private void drawBullets(Canvas canvas, Tower tower) {
        for (Bullet bullet : tower.getBullets()) {
            PointF position = Vectors.copy(bullet.getPosition());
            position.x *= tileSize;
            position.y *= tileSize;

            canvas.drawCircle(position.x, position.y, 10f, paintBullet);
        }

    }

    public void restartGame(Context context) {
        InputStream mapResource = context.getResources().openRawResource(R.raw.map_1);
        gameMap = new GameMap(mapResource);
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
}
