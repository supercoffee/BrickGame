package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Paddle implements Components.ShapeComponent, Components.ColorComponent, Components.CollisionComponent {

    private final CollisionDetector mCollisionDetector;
    private Rectangle mBounds;
    private Color mColor;

    public Paddle(Color color) {
        mColor = color;
        mBounds = new Rectangle();
        mCollisionDetector = new CollisionDetector(mBounds);
    }

    @Override
    public Rectangle getBoundary() {
        return mBounds;
    }

    public Color getColor() {
        return mColor;
    }

    @Override
    public CollisionDetector.CollisionEdge detectCollision(Rectangle other) {
        return mCollisionDetector.detectCollision(other);
    }
}
