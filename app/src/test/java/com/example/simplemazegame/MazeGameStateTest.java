package com.example.simplemazegame;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MazeGameStateTest {

    @Test
    public void defaultMazeFindsStartMarkerAndIgnoresMarkerForDoors() {
        MazeGameState state = MazeGameState.createDefaultGame();

        assertEquals(1, state.getRow());
        assertEquals(0, state.getColumn());
        assertEquals(28, state.getCurrentRoomValue());
        assertTrue(state.canMove(Direction.UP));
        assertTrue(state.canMove(Direction.DOWN));
        assertFalse(state.canMove(Direction.LEFT));
        assertFalse(state.canMove(Direction.RIGHT));
    }

    @Test
    public void moveUsesOnlyCurrentRoomBitmask() {
        MazeGameState state = new MazeGameState(MazeGameState.DEFAULT_MAZE, 0, 1);

        assertTrue(state.canMove(Direction.DOWN));

        MazeGameState moved = state.move(Direction.DOWN);
        assertEquals(1, moved.getRow());
        assertEquals(1, moved.getColumn());
        assertTrue(moved.canMove(Direction.LEFT));
        assertFalse(moved.canMove(Direction.RIGHT));
    }

    @Test
    public void moveOutsideMazeIsBlockedEvenWhenDoorBitExists() {
        int[][] maze = {
                {Direction.UP.getMask() | MazeGameState.START_MARKER, 0}
        };
        MazeGameState state = MazeGameState.createFromMaze(maze);

        assertFalse(state.canMove(Direction.UP));
        assertEquals(state, state.move(Direction.UP));
    }

    @Test
    public void reachingZeroRoomFinishesGameImmediately() {
        MazeGameState state = new MazeGameState(MazeGameState.DEFAULT_MAZE, 0, 2);

        MazeGameState moved = state.move(Direction.DOWN);

        assertEquals(1, moved.getRow());
        assertEquals(2, moved.getColumn());
        assertEquals(0, moved.getCurrentRoomValue());
        assertTrue(moved.isFinished());
    }

    @Test
    public void restartReturnsToTheMarkedStartRoom() {
        MazeGameState state = new MazeGameState(MazeGameState.DEFAULT_MAZE, 0, 2)
                .move(Direction.DOWN);

        MazeGameState restarted = state.restart();

        assertFalse(restarted.isFinished());
        assertEquals(1, restarted.getRow());
        assertEquals(0, restarted.getColumn());
    }
}
