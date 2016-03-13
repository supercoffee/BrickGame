package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Block implements Components.ShapeComponent,
        Components.ColorComponent{

    private final Rectangle mBoundary;
    private Color mColor;

    public Block(Color color, Rectangle boundary) {
        mColor = color;
        mBoundary = boundary;
    }

    @Override
    public Rectangle getBoundary() {
        return mBoundary;
    }

    @Override
    public Color getColor() {
        return mColor;
    }
}
