package com.daruc.towerdefence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by darek on 11.08.18.
 */

public class AStarPathFinder {
    private boolean[][] map;
    private int[][] cost;
    private PriorityPoint[][] predecessor;

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

        predecessor = new PriorityPoint[map.length][];
        for (int y = 0; y < map.length; ++y) {
            predecessor[y] = new PriorityPoint[map[0].length];
        }
    }

    public List<MapPoint> find(MapPoint pSource, MapPoint pDestination) {
        if (pDestination.getY() < 0 || pDestination.getY() >= map.length) {
            throw new RuntimeException("Wrong sourceY");
        }
        if (pSource.getX() < 0 || pSource.getX() >= map[0].length) {
            throw new RuntimeException("Wrong sourceX");
        }

        PriorityPoint source = new PriorityPoint(pSource.getX(), pSource.getY());
        PriorityPoint destination = new PriorityPoint(pDestination.getX(), pDestination.getY());

        PriorityQueue<PriorityPoint> frontier = new PriorityQueue<>(10, new IntPointComparator());
        frontier.add(source);
        cost[pSource.getY()][pSource.getX()] = 0;

        while (!frontier.isEmpty()) {
            PriorityPoint current = frontier.poll();
            if (current.equals(destination)) {
                break;
            }

            for (PriorityPoint next : getNeighbours(current)) {
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

        List<MapPoint> result = new LinkedList<>();
        PriorityPoint current = destination;
        do {
            result.add(new MapPoint(current.getX(), current.getY()));
            current = predecessor[current.getY()][current.getX()];
            if (current == null) {
                return null;
            }
        } while (!current.equals(source));

        Collections.reverse(result);
        return result;
    }

    private List<PriorityPoint> getNeighbours(PriorityPoint point) {
        List<PriorityPoint> neighbours = new ArrayList<>(4);
        if (point.getX() - 1 >= 0 && map[point.getY()][point.getX() - 1]) {
            neighbours.add(new PriorityPoint(point.getX() - 1, point.getY()));
        }

        if (point.getX() + 1 < map[0].length && map[point.getY()][point.getX() + 1]) {
            neighbours.add(new PriorityPoint(point.getX() + 1, point.getY()));
        }

        if (point.getY() - 1 >= 0 && map[point.getY() - 1][point.getX()]) {
            neighbours.add(new PriorityPoint(point.getX(), point.getY() - 1));
        }

        if (point.getY() + 1 < map.length && map[point.getY() + 1][point.getX()]) {
            neighbours.add(new PriorityPoint(point.getX(), point.getY() + 1));
        }
        return neighbours;
    }

    private int distance(PriorityPoint pointA, PriorityPoint pointB) {
        return Math.abs(pointA.getX() - pointB.getX()) + Math.abs(pointA.getY() - pointB.getY());
    }

    private static class PriorityPoint {

        private int x;
        private int y;
        private int priority;

        public PriorityPoint(int x, int y) {
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

    private static class IntPointComparator implements Comparator<PriorityPoint> {
        @Override
        public int compare(PriorityPoint pointA, PriorityPoint pointB) {
            return pointA.getPriority() - pointB.getPriority();
        }
    }
}
