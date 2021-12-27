package com.daruc.towerdefence;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapLoader {

    private static final String NEW_LINE_REGEX = "\r\n|\r|\n";

    private GroundType[][] groundTiles;
    private MapDimensions mapDimensions;

    public void loadFromFile(InputStream mapResource) {
        Scanner scanner = new Scanner(mapResource);
        scanner.useDelimiter("\\A");

        String mapStr = readFromStreamAndTrim(scanner);
        calculateMapDimensions(mapStr);
        mapStr = removeNewLinesSymbols(mapStr);
        parseGroundTiles(mapStr);
    }

    private String readFromStreamAndTrim(Scanner scanner) {
        String mapStr = "";
        if (scanner.hasNext()) {
            mapStr = scanner.next();
            mapStr.trim();
        }
        return mapStr;
    }

    private void calculateMapDimensions(String mapStr) {
        int width = calculateMapWidth(mapStr);
        int height = calculateMapHeight(mapStr);
        mapDimensions = new MapDimensions(width, height);
    }

    private int calculateMapWidth(String mapStr) {
        return mapStr.indexOf("\r");
    }

    private int calculateMapHeight(String mapStr) {
        Matcher matcher = Pattern.compile(NEW_LINE_REGEX).matcher(mapStr);
        int lines = 1;
        while (matcher.find()) {
            ++lines;
        }
        return lines;
    }

    private String removeNewLinesSymbols(String mapStr) {
        return mapStr.replaceAll(NEW_LINE_REGEX, "");
    }

    private void parseGroundTiles(String mapStr) {
        groundTiles = new GroundType[mapDimensions.height][];
        for (int h = 0; h < mapDimensions.height; ++h) {
            groundTiles[h] = new GroundType[mapDimensions.width];
            for (int w = 0; w < mapDimensions.width; ++w) {
                int index = (h * mapDimensions.width) + w;
                groundTiles[h][w] = GroundType.fromChar(mapStr.charAt(index));
            }
        }
    }

    public GroundType[][] getGroundTiles() {
        return groundTiles;
    }

    public MapDimensions getMapDimensions() {
        return mapDimensions;
    }
}
