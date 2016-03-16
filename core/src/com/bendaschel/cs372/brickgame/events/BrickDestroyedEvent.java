package com.bendaschel.cs372.brickgame.events;

public class BrickDestroyedEvent {

    public int bricksRemaining;

    public BrickDestroyedEvent(int bricksRemaining) {
        this.bricksRemaining = bricksRemaining;
    }
}
