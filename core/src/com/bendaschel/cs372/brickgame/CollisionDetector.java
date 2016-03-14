package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.math.Rectangle;

// http://www.owenpellegrin.com/articles/vb-net/simple-collision-detection/

public class CollisionDetector {

    public enum CollisionEdge {
        TOP,
        BOTTOM,
        RIGHT,
        LEFT,
        NONE
    }

    private Rectangle mBoundingBox;

    public CollisionDetector(Rectangle boundingBox) {
        mBoundingBox = boundingBox;
    }

    public CollisionEdge detectCollision(Rectangle other) {
        if (mBoundingBox.overlaps(other)) {
            float myLeftEdge = mBoundingBox.x;
            float myRightEdge = myLeftEdge + mBoundingBox.getWidth();
            float myBottomEdge = mBoundingBox.y;
            float myTopEdge = myBottomEdge + mBoundingBox.getHeight();

            float otherLeftEdge = other.x;
            float otherRightEdge = otherLeftEdge + other.getWidth();
            float otherBottomEdge = other.y;
            float otherTopEdge = otherBottomEdge + other.getHeight();
            if (myLeftEdge < otherLeftEdge && myRightEdge > otherLeftEdge) {
                return CollisionEdge.RIGHT;
            }
            if (myRightEdge > otherRightEdge && myLeftEdge < otherLeftEdge) {
                return CollisionEdge.LEFT;
            }
            if (myTopEdge > otherBottomEdge && myBottomEdge < otherBottomEdge){
                return CollisionEdge.TOP;
            }
            if (myBottomEdge < otherTopEdge && myTopEdge > otherTopEdge) {
                return CollisionEdge.BOTTOM;
            }
        }
        return CollisionEdge.NONE;
    }

}
