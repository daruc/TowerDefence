package com.daruc.towerdefence;

/**
 * Created by darek on 02.04.18.
 */

public enum GroundType {
    WATER('W'), GRASS('G'), PATH('R'), STONE('S'), FOREST('F'), CASTLE('C');

    private char ch;

    GroundType(char ch) {
        this.ch = ch;
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
}
