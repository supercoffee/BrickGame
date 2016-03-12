package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Components {

    public interface TextureComponent {
        Texture getTexture();
    }

    public interface VelocityComponent {
        Vector2 getVelocity();
    }

    public interface PositionComponent {
        Vector2 getPosition();
    }

    public interface ShapeComponent {
        Rectangle getBoundary();
    }
}
