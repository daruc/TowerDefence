package com.daruc.towerdefence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AStarPathFinder {
    private boolean[][] map;
    private int[][] cost;
    private PriorityPoint[][] predecessor;

    public AStarPathFinder(boolean[][] map) {
        throwIfWrongMapSize(map);
        this.map = map;
        initInfiniteCostMap();
        initPredecessorMap();
    }

    private void throwIfWrongMapSize(boolean[][] map) {
        if (isMapSizeWrong(map)) {
            throw new RuntimeException("Wrong map size.");
        }
    }

    private boolean isMapSizeWrong(boolean[][] map) {
        return map.length < 2 || map[0].length < 2;
    }

    private void initInfiniteCostMap() {
        cost = new int[map.length][];
        for (int y = 0; y < cost.length; ++y) {
            cost[y] = new int[map[0].length];
            for (int x = 0; x < cost[0].length; ++x) {
                cost[y][x] = Integer.MAX_VALUE;
            }
        }
    }

    private void initPredecessorMap() {
        predecessor = new PriorityPoint[map.length][];
        for (int y = 0; y < map.length; ++y) {
            predecessor[y] = new PriorityPoint[map[0].length];
        }
    }

    public List<MapPoint> find(MapPoint pSource, MapPoint pDestination) {
        throwIfCoordinatesOutOfRange(pSource);
        throwIfCoordinatesOutOfRange(pDestination);

        PriorityPoint source = new PriorityPoint(pSource.getX(), pSource.getY());
        PriorityPoint destination = new PriorityPoint(pDestination.getX(), pDestination.getY());

        PriorityQueue<PriorityPoint> frontier = createFrontier(source);
        computeCosts(destination, frontier);

        return getPath(source, destination);
    }

    private void computeCosts(PriorityPoint destination, PriorityQueue<PriorityPoint> frontier) {
        while (!frontier.isEmpty()) {
            PriorityPoint current = frontier.poll();
            if (current.equals(destination)) {
                break;
            }
            computeCostForNeighbours(destination, frontier, current);
        }
    }

    private void computeCostForNeighbours(PriorityPoint destination,
                                          PriorityQueue<PriorityPoint> frontier,
                                          PriorityPoint current) {

        for (PriorityPoint next : getNeighbours(current)) {
            int newCost = calculateNewConst(current);
            if (isNewCostLower(newCost, next)) {
                updateCost(destination, frontier, current, next, newCost);
            }
        }
    }

    private int calculateNewConst(PriorityPoint current) {
        return cost[current.getY()][current.getX()] + 1;
    }

    private boolean isNewCostLower(int newCost, PriorityPoint nextPoint) {
        return newCost < cost[nextPoint.getY()][nextPoint.getX()];
    }

    private void updateCost(PriorityPoint destination, PriorityQueue<PriorityPoint> frontier,
                            PriorityPoint current, PriorityPoint next, int newCost) {

        cost[next.getY()][next.getX()] = newCost;
        int priority = newCost + next.getDistance(destination);
        next.setPriority(priority);
        frontier.add(next);
        predecessor[next.getY()][next.getX()] = current;
    }

    private List<MapPoint> getPath(PriorityPoint source, PriorityPoint destination) {
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

    private PriorityQueue<PriorityPoint> createFrontier(PriorityPoint source) {
        PriorityQueue<PriorityPoint> frontier =
                new PriorityQueue<>(10, new IntPointComparator());
        frontier.add(source);
        cost[source.getY()][source.getX()] = 0;
        return frontier;
    }

    private void throwIfCoordinatesOutOfRange(MapPoint mapPoint) {
        throwIfXCoordinateOutOfRange(mapPoint.getX());
        throwIfYCoordinateOutOfRange(mapPoint.getY());
    }

    private void throwIfXCoordinateOutOfRange(float xCoordinate) {
        if (xCoordinate < 0 || xCoordinate >= map[0].length) {
            throw new RuntimeException("X coordinate = " + xCoordinate + " in A* out of range.");
        }
    }

    private void throwIfYCoordinateOutOfRange(float yCoordinate) {
        if (yCoordinate < 0 || yCoordinate >= map.length) {
            throw new RuntimeException("Y coordinate = " + yCoordinate + " in A* out of range.");
        }
    }

    private List<PriorityPoint> getNeighbours(PriorityPoint point) {
        List<PriorityPoint> neighbours = new ArrayList<>(4);

        if (hasLeftNeighbour(point)) {
            addLeftNeighbour(point, neighbours);
        }

        if (hasRightNeighbour(point)) {
            addRightNeighbour(point, neighbours);
        }

        if (hasTopNeighbour(point)) {
            addTopNeighbour(point, neighbours);
        }

        if (hasBottomNeighbour(point)) {
            addBottomNeighbour(point, neighbours);
        }

        return neighbours;
    }

    private boolean hasLeftNeighbour(PriorityPoint point) {
        return point.getX() - 1 >= 0 && map[point.getY()][point.getX() - 1];
    }

    private void addLeftNeighbour(PriorityPoint point, List<PriorityPoint> neighbours) {
        neighbours.add(new PriorityPoint(point.getX() - 1, point.getY()));
    }

    private boolean hasRightNeighbour(PriorityPoint point) {
        return point.getX() + 1 < map[0].length && map[point.getY()][point.getX() + 1];
    }

    private void addRightNeighbour(PriorityPoint point, List<PriorityPoint> neighbours) {
        neighbours.add(new PriorityPoint(point.getX() + 1, point.getY()));
    }

    private boolean hasTopNeighbour(PriorityPoint point) {
        return point.getY() - 1 >= 0 && map[point.getY() - 1][point.getX()];
    }

    private void addTopNeighbour(PriorityPoint point, List<PriorityPoint> neighbours) {
        neighbours.add(new PriorityPoint(point.getX(), point.getY() - 1));
    }

    private boolean hasBottomNeighbour(PriorityPoint point) {
        return point.getY() + 1 < map.length && map[point.getY() + 1][point.getX()];
    }

    private void addBottomNeighbour(PriorityPoint point, List<PriorityPoint> neighbours) {
        neighbours.add(new PriorityPoint(point.getX(), point.getY() + 1));
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

        private int getDistance(PriorityPoint otherPoint) {
            return Math.abs(x - otherPoint.getX()) + Math.abs(y - otherPoint.getY());
        }
    }

    private static class IntPointComparator implements Comparator<PriorityPoint> {
        @Override
        public int compare(PriorityPoint pointA, PriorityPoint pointB) {
            return pointA.getPriority() - pointB.getPriority();
        }
    }
}
