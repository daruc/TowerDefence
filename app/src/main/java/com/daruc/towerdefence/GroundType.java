package com.daruc.towerdefence;


import com.daruc.towerdefence.groundtile.ForestTileDrawingStrategy;
import com.daruc.towerdefence.groundtile.GrassTileDrawingStrategy;
import com.daruc.towerdefence.groundtile.GroundTileDrawingStrategy;
import com.daruc.towerdefence.groundtile.PathTileDrawingStrategy;
import com.daruc.towerdefence.groundtile.StoneTileDrawingStrategy;
import com.daruc.towerdefence.groundtile.WaterTileDrawingStrategy;


public enum GroundType {
    WATER('W', new WaterTileDrawingStrategy()),
    GRASS('G', new GrassTileDrawingStrategy()),
    PATH('R', new PathTileDrawingStrategy()),
    STONE('S', new StoneTileDrawingStrategy()),
    FOREST('F', new ForestTileDrawingStrategy()),
    CASTLE('C', new PathTileDrawingStrategy());

    private char ch;
    private GroundTileDrawingStrategy groundTileDrawingStrategy;

    GroundType(char ch, GroundTileDrawingStrategy groundTileDrawingStrategy) {
        this.ch = ch;
        this.groundTileDrawingStrategy = groundTileDrawingStrategy;
    }

    public char getChar() {
        return ch;
    }

    public static GroundType fromChar(char ch) {
        for (GroundType type : values()) {
            if (type.ch == ch) {
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot find GroundType by char '" + ch + "'.");
    }

    public GroundTileDrawingStrategy getDrawingStrategy() {
        return groundTileDrawingStrategy;
    }
}
