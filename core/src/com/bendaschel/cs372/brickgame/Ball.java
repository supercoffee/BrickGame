package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ball
        implements Components.TextureComponent, Components.VelocityComponent,
        Components.ShapeComponent, Components.PositionComponent{

    private Texture mTexture;
    private Vector2 mVelocity;
    private Vector2 mPosition;
    private Rectangle mBoundingRectangle;

    public Ball(Texture texture) {
        mTexture = texture;
        mVelocity = new Vector2();
        mBoundingRectangle = new Rectangle(0, 0, texture.getWidth(), texture.getHeight());
        mPosition = mBoundingRectangle.getPosition(new Vector2());
    }

    public Texture getTexture() {
        return mTexture;
    }


    @Override
    public Vector2 getVelocity() {
        return mVelocity;
    }

    @Override
    public Rectangle getBoundary() {
        return mBoundingRectangle;
    }

    @Override
    public Vector2 getPosition() {
        return mBoundingRectangle.getPosition(mPosition);
    }

}
