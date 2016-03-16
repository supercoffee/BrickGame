package com.bendaschel.cs372.brickgame.events;

public class BallLostEvent {

    public int ballRemaining;

    public BallLostEvent(int ballRemaining) {
        this.ballRemaining = ballRemaining;
    }
}
