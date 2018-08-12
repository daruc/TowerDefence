package com.daruc.towerdefence;

import android.graphics.Point;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import java.util.List;

/**
 * Created by darek on 11.08.18.
 */

public class AStarPathFinderTest {

    @Test
    public void pathFinderTest1() {
        boolean map[][] = {
                {true, true, true, true},
                {false, false, false, true},
                {true, true, true, true},
                {true, false, false, false},
                {true, true, true, true}
        };

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
}
