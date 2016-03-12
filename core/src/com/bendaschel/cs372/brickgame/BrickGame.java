package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BrickGame extends ApplicationAdapter {

	static final int BG_COLOR_RED = 1;
	static final int BG_COLOR_GREEN = 1;
	static final int BG_COLOR_BLUE = 1;
	static final int BG_ALPHA = 1;
	SpriteBatch batch;
	private Ball mBall;
	private int mScreenHeight;
	private int mScreenWidth;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture ballTexture = new Texture("ball.png");
		mScreenHeight = Gdx.graphics.getHeight();
		mScreenWidth = Gdx.graphics.getWidth();
		mBall = new Ball(ballTexture);
		mBall.getPosition().set(0, 0);
		mBall.getVelocity().set(1, 1);
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
		mBall.getPosition().add(mBall.getVelocity());
	}

}
