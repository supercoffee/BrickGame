package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class BrickGame extends ApplicationAdapter {

	static final int NUM_BLOCKS = 20;
	static final int BG_COLOR_RED = 1;
	static final int BG_COLOR_GREEN = 1;
	static final int BG_COLOR_BLUE = 1;
	static final int BG_ALPHA = 1;
	SpriteBatch batch;
	private Ball mBall;
	private Rectangle mGameWallTop;
	private Rectangle mGameWallLeft;
	private Rectangle mGameWallRight;
	private Rectangle mGameWallBottom;
	private NinePatch mBlockPatch;
	private int mScreenHeight;
	private int mScreenWidth;
	private Array<Block> mBlocks;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture ballTexture = new Texture("ball.png");
		mScreenHeight = Gdx.graphics.getHeight();
		mScreenWidth = Gdx.graphics.getWidth();
		mBall = new Ball(ballTexture);
		mBlockPatch = new NinePatch(new Texture("block.png"), 6, 6, 6, 6);
		mBlocks = createBlocks();
		mBall.getVelocity().set(2, 2);
		mGameWallTop = new Rectangle(0, mScreenHeight, mScreenWidth, 1);
		mGameWallLeft = new Rectangle(-1, 0, 1, mScreenHeight);
		mGameWallRight = new Rectangle(mScreenWidth, 0, 1, mScreenHeight);
		mGameWallBottom = new Rectangle(0, -1, mScreenWidth, 1);
	}

	private Array<Block> createBlocks() {
		Array<Block> blocks = new Array<Block>(NUM_BLOCKS);
		int blockHeight = 32;
		int remainingRowSpace = mScreenWidth;
		int row = 1; // starts from top down
		Random random = new Random();
		for (int i = 0; i < NUM_BLOCKS; i++){
			int randomWidth = random.nextInt(mScreenWidth);
			int x = mScreenWidth - remainingRowSpace;
			int y = mScreenHeight - (row * blockHeight);
			int actualBlockWidth = Math.min(randomWidth, remainingRowSpace);
			Block block = new Block(Color.RED, new Rectangle(x, y, actualBlockWidth, blockHeight));
			blocks.add(block);
			remainingRowSpace -= randomWidth;
			if (remainingRowSpace <=0) {
				row++;
				remainingRowSpace = mScreenWidth;
			}
		}
		return blocks;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(BG_COLOR_RED, BG_COLOR_GREEN, BG_COLOR_BLUE, BG_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateBallPosition();
		updateBlocks();
		batch.begin();
		batch.draw(mBall.getTexture(), mBall.getPosition().x, mBall.getPosition().y);
		// Use standard for loop to avoid allocating a new Iterator
		for (int i = 0; i < mBlocks.size; i++) {
			Block block = mBlocks.get(i);
			mBlockPatch.setColor(block.getColor());
			Rectangle blockBounds = block.getBoundary();
			mBlockPatch.draw(batch, blockBounds.x, blockBounds.y,
					blockBounds.getWidth(), blockBounds.getHeight());
		}
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

	private void updateBlocks() {
		for (int i = 0; i < mBlocks.size; i++) {
			Block block = mBlocks.get(i);
			if (mBall.getBoundary().overlaps(block.getBoundary())) {
				mBlocks.removeIndex(i);
			}
		}
	}

}
