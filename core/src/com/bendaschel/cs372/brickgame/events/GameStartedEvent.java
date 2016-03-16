package com.bendaschel.cs372.brickgame.events;

public class GameStartedEvent {

    public int score;
    public int numBalls;
    public int level;
    public int bricksRemaining;

    public GameStartedEvent(int score, int numBalls, int level, int bricksRemaining) {
        this.score = score;
        this.numBalls = numBalls;
        this.level = level;
        this.bricksRemaining = bricksRemaining;
    }
}
