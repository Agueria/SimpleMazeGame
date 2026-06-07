package com.example.simplemazegame;

public enum Direction {
    LEFT(1, 0, -1),
    RIGHT(2, 0, 1),
    UP(4, -1, 0),
    DOWN(8, 1, 0);

    private final int mask;
    private final int rowDelta;
    private final int columnDelta;

    Direction(int mask, int rowDelta, int columnDelta) {
        this.mask = mask;
        this.rowDelta = rowDelta;
        this.columnDelta = columnDelta;
    }

    public int getMask() {
        return mask;
    }

    public int getRowDelta() {
        return rowDelta;
    }

    public int getColumnDelta() {
        return columnDelta;
    }
}
