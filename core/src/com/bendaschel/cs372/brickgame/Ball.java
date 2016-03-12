package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ball
        implements Components.TextureComponent, Components.VelocityComponent,
        Components.PositionComponent{

    private Texture mTexture;
    private Vector2 mVelocity;
    private Vector2 mPosition;

    public Ball(Texture texture) {
        mTexture = texture;
        mVelocity = new Vector2();
        mPosition = new Vector2();
    }

    public Texture getTexture() {
        return mTexture;
    }


    @Override
    public Vector2 getPosition() {
        return mPosition;
    }

    @Override
    public Vector2 getVelocity() {
        return mVelocity;
    }
}
