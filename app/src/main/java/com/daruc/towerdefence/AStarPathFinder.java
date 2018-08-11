package com.daruc.towerdefence;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by darek on 11.08.18.
 */

public class AStarPathFinder {
    private boolean[][] map;
    private int[][] cost;
    private IntPoint[][] predecessor;

    public AStarPathFinder(boolean[][] map) {
        if (map.length < 2 || map[0].length < 2) {
            throw new RuntimeException("Wrong map size.");
        }

        this.map = map;
        cost = new int[map.length][];
        for (int y = 0; y < cost.length; ++y) {
            cost[y] = new int[map[0].length];
            for (int x = 0; x < cost[0].length; ++x) {
                cost[y][x] = Integer.MAX_VALUE;
            }
        }

        predecessor = new IntPoint[map.length][];
        for (int y = 0; y < map.length; ++y) {
            predecessor[y] = new IntPoint[map[0].length];
        }
    }

    public List<MapPoint> find(int sourceX, int sourceY, int destinationX, int destinationY) {
        if (sourceY < 0 || sourceY >= map.length) {
            throw new RuntimeException("Wrong sourceY");
        }
        if (sourceX < 0 || sourceX >= map[0].length) {
            throw new RuntimeException("Wrong sourceX");
        }

        IntPoint source = new IntPoint(sourceX, sourceY);
        IntPoint destination = new IntPoint(destinationX, destinationY);

        PriorityQueue<IntPoint> frontier = new PriorityQueue<>(10, new IntPointComparator());
        frontier.add(source);
        cost[sourceY][sourceX] = 0;

        while (!frontier.isEmpty()) {
            IntPoint current = frontier.poll();
            if (current.equals(destination)) {
                break;
            }

            for (IntPoint next : getNeighbours(current)) {
                int newCost = cost[current.getY()][current.getX()] + 1;
                if (newCost < cost[next.getY()][next.getX()]) {
                    cost[next.getY()][next.getX()] = newCost;
                    int priority = newCost + distance(next, destination);
                    next.setPriority(priority);
                    frontier.add(next);
                    predecessor[next.getY()][next.getX()] = current;
                }
            }
        }

        List<MapPoint> result = new ArrayList<>();
        IntPoint current = destination;
        do {
            result.add(new MapPoint(current.getX(), current.getY()));
            current = predecessor[current.getY()][current.getX()];
        } while (!current.equals(source));

        result.add(new MapPoint(source.getX(), source.getY()));
        Collections.reverse(result);
        return result;
    }

    private List<IntPoint> getNeighbours(IntPoint point) {
        List<IntPoint> neighbours = new ArrayList<>(4);
        if (point.getX() - 1 >= 0 && map[point.getY()][point.getX() - 1]) {
            neighbours.add(new IntPoint(point.getX() - 1, point.getY()));
        }

        if (point.getX() + 1 < map[0].length && map[point.getY()][point.getX() + 1]) {
            neighbours.add(new IntPoint(point.getX() + 1, point.getY()));
        }

        if (point.getY() - 1 >= 0 && map[point.getY() - 1][point.getX()]) {
            neighbours.add(new IntPoint(point.getX(), point.getY() - 1));
        }

        if (point.getY() + 1 < map.length && map[point.getY() + 1][point.getX()]) {
            neighbours.add(new IntPoint(point.getX(), point.getY() + 1));
        }
        return neighbours;
    }

    private int distance(IntPoint pointA, IntPoint pointB) {
        return Math.abs(pointA.getX() - pointB.getX()) + Math.abs(pointA.getY() - pointB.getY());
    }

    private static class IntPoint {

        private int x;
        private int y;
        private int priority;

        public IntPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }

    private static class IntPointComparator implements Comparator<IntPoint> {
        @Override
        public int compare(IntPoint pointA, IntPoint pointB) {
            return pointA.getPriority() - pointB.getPriority();
        }
    }
}
