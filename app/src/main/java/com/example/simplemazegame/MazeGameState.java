package com.example.simplemazegame;

import java.util.Arrays;
import java.util.Objects;

public final class MazeGameState {
    public static final int START_MARKER = 16;
    public static final int END_ROOM = 0;

    static final int[][] DEFAULT_MAZE = {
            {10, 8, 10, 9},
            {28, 1, 0, 12},
            {12, 10, 9, 13},
            {6, 5, 6, 5}
    };

    private final int[][] maze;
    private final int row;
    private final int column;

    public MazeGameState(int[][] maze, int row, int column) {
        validateMaze(maze);
        if (!isInside(maze, row, column)) {
            throw new IllegalArgumentException("Position is outside the maze.");
        }

        this.maze = copyMaze(maze);
        this.row = row;
        this.column = column;
    }

    public static MazeGameState createDefaultGame() {
        return createFromMaze(DEFAULT_MAZE);
    }

    public static MazeGameState createFromMaze(int[][] maze) {
        Position start = findStart(maze);
        return new MazeGameState(maze, start.row, start.column);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getCurrentRoomValue() {
        return maze[row][column];
    }

    public int getRows() {
        return maze.length;
    }

    public int getColumns() {
        return maze[0].length;
    }

    public boolean isFinished() {
        return getCurrentRoomValue() == END_ROOM;
    }

    public boolean canMove(Direction direction) {
        Objects.requireNonNull(direction, "direction");

        if (isFinished()) {
            return false;
        }

        int movementBits = getCurrentRoomValue() & ~START_MARKER;
        int nextRow = row + direction.getRowDelta();
        int nextColumn = column + direction.getColumnDelta();
        return (movementBits & direction.getMask()) != 0 && isInside(maze, nextRow, nextColumn);
    }

    public MazeGameState move(Direction direction) {
        if (!canMove(direction)) {
            return this;
        }

        int nextRow = row + direction.getRowDelta();
        int nextColumn = column + direction.getColumnDelta();
        return new MazeGameState(maze, nextRow, nextColumn);
    }

    public MazeGameState restart() {
        return createFromMaze(maze);
    }

    private static Position findStart(int[][] maze) {
        validateMaze(maze);

        for (int rowIndex = 0; rowIndex < maze.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < maze[rowIndex].length; columnIndex++) {
                if ((maze[rowIndex][columnIndex] & START_MARKER) != 0) {
                    return new Position(rowIndex, columnIndex);
                }
            }
        }

        throw new IllegalArgumentException("Maze must contain a start room marked with bit 16.");
    }

    private static void validateMaze(int[][] maze) {
        if (maze == null || maze.length == 0 || maze[0] == null || maze[0].length == 0) {
            throw new IllegalArgumentException("Maze must not be empty.");
        }

        int expectedColumns = maze[0].length;
        for (int rowIndex = 0; rowIndex < maze.length; rowIndex++) {
            if (maze[rowIndex] == null || maze[rowIndex].length != expectedColumns) {
                throw new IllegalArgumentException("Maze rows must have the same length.");
            }
        }
    }

    private static boolean isInside(int[][] maze, int row, int column) {
        return row >= 0 && row < maze.length && column >= 0 && column < maze[row].length;
    }

    private static int[][] copyMaze(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int rowIndex = 0; rowIndex < source.length; rowIndex++) {
            copy[rowIndex] = Arrays.copyOf(source[rowIndex], source[rowIndex].length);
        }
        return copy;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MazeGameState)) {
            return false;
        }
        MazeGameState that = (MazeGameState) other;
        return row == that.row && column == that.column && Arrays.deepEquals(maze, that.maze);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(row, column);
        result = 31 * result + Arrays.deepHashCode(maze);
        return result;
    }

    private static final class Position {
        private final int row;
        private final int column;

        private Position(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
