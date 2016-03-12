package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BrickGame extends ApplicationAdapter {

	static final int BG_COLOR_RED = 1;
	static final int BG_COLOR_GREEN = 1;
	static final int BG_COLOR_BLUE = 1;
	static final int BG_ALPHA = 1;
	SpriteBatch batch;
	private Ball mBall;
	private int mScreenHeight;
	private int mScreenWidth;
	private Rectangle mGameWallTop;
	private Rectangle mGameWallLeft;
	private Rectangle mGameWallRight;
	private Rectangle mGameWallBottom;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture ballTexture = new Texture("ball.png");
		mScreenHeight = Gdx.graphics.getHeight();
		mScreenWidth = Gdx.graphics.getWidth();
		mBall = new Ball(ballTexture);
		mBall.getVelocity().set(2, 2);
		mGameWallTop = new Rectangle(0, mScreenHeight, mScreenWidth, 1);
		mGameWallLeft = new Rectangle(-1, 0, 1, mScreenHeight);
		mGameWallRight = new Rectangle(mScreenWidth, 0, 1, mScreenHeight);
		mGameWallBottom = new Rectangle(0, -1, mScreenWidth, 1);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(BG_COLOR_RED, BG_COLOR_GREEN, BG_COLOR_BLUE, BG_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateBallPosition();
		batch.begin();
		batch.draw(mBall.getTexture(), mBall.getPosition().x, mBall.getPosition().y);
		batch.end();
	}

	private void updateBallPosition() {
		Rectangle bounds = mBall.getBoundary();
		Vector2 ballVelocity = mBall.getVelocity();
		if (bounds.overlaps(mGameWallBottom) || bounds.overlaps(mGameWallTop)) {
			ballVelocity.set(ballVelocity.x, ballVelocity.y * -1);
		}
		if (bounds.overlaps(mGameWallLeft) || bounds.overlaps(mGameWallRight)) {
			ballVelocity.set(ballVelocity.x * -1, ballVelocity.y);
		}
		bounds.setPosition(mBall.getPosition().add(ballVelocity));
	}

}
