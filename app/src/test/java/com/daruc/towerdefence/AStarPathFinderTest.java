package com.daruc.towerdefence;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import java.util.List;

public class AStarPathFinderTest {

    private boolean[][] createMap() {
        boolean map[][] = {
                {true, true, true, true},
                {false, false, false, true},
                {true, true, true, true},
                {true, false, false, false},
                {true, true, true, true}
        };
        return map;
    }

    @Test
    public void testFindPath() {
        boolean map[][] = createMap();

        AStarPathFinder pathFinder = new AStarPathFinder(map);
        List<MapPoint> path = pathFinder.find(new MapPoint(0, 0), new MapPoint(3, 4));

        assertEquals(13, path.size());

        assertEquals(new MapPoint(1, 0), path.get(0));
        assertEquals(new MapPoint(2, 0), path.get(1));
        assertEquals(new MapPoint(3, 0), path.get(2));
        assertEquals(new MapPoint(3, 1), path.get(3));
        assertEquals(new MapPoint(3, 2), path.get(4));
        assertEquals(new MapPoint(2, 2), path.get(5));
        assertEquals(new MapPoint(1, 2), path.get(6));
        assertEquals(new MapPoint(0, 2), path.get(7));
        assertEquals(new MapPoint(0, 3), path.get(8));
        assertEquals(new MapPoint(0, 4), path.get(9));
        assertEquals(new MapPoint(1, 4), path.get(10));
        assertEquals(new MapPoint(2, 4), path.get(11));
        assertEquals(new MapPoint(3, 4), path.get(12));
    }

    @Test(expected = RuntimeException.class)
    public void testSourceXOutOfRange() {
        boolean map[][] = createMap();
        MapPoint sourceWrongX = new MapPoint(100, 1);
        MapPoint destination = new MapPoint(1, 1);

        AStarPathFinder pathFinder = new AStarPathFinder(map);
        pathFinder.find(sourceWrongX, destination);
    }

    @Test(expected = RuntimeException.class)
    public void testSourceYOutOfRange() {
        boolean map[][] = createMap();
        MapPoint sourceWrongX = new MapPoint(1, 100);
        MapPoint destination = new MapPoint(1, 1);

        AStarPathFinder pathFinder = new AStarPathFinder(map);
        pathFinder.find(sourceWrongX, destination);
    }

    @Test(expected = RuntimeException.class)
    public void testDestinationXOutOfRange() {
        boolean map[][] = createMap();
        MapPoint source = new MapPoint(1, 1);
        MapPoint destinationWrongX = new MapPoint(100, 1);

        AStarPathFinder pathFinder = new AStarPathFinder(map);
        pathFinder.find(source, destinationWrongX);
    }

    @Test(expected = RuntimeException.class)
    public void testDestinationYOutOfRange() {
        boolean map[][] = createMap();
        MapPoint source = new MapPoint(1, 1);
        MapPoint destinationWrongY = new MapPoint(1, 100);

        AStarPathFinder pathFinder = new AStarPathFinder(map);
        pathFinder.find(source, destinationWrongY);
    }

    @Test(expected = RuntimeException.class)
    public void testMapWrongSize() {
        boolean map[][] = {{true}};
        MapPoint source = new MapPoint(0, 0);
        MapPoint destination = new MapPoint(0, 0);

        AStarPathFinder pathFinder = new AStarPathFinder(map);
    }
}
