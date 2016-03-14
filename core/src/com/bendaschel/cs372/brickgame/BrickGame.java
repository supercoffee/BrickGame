package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class BrickGame extends ApplicationAdapter implements GestureDetector.GestureListener {

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
	private Paddle mPaddle;
	private BitmapFont mFont;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(new GestureDetector(this));
		Texture ballTexture = new Texture("ball.png");
		mScreenHeight = Gdx.graphics.getHeight();
		mScreenWidth = Gdx.graphics.getWidth();
		mBlockPatch = new NinePatch(new Texture("block.png"), 6, 6, 6, 6);
		mPaddle = new Paddle(Color.GREEN);
		mPaddle.getBoundary().set(0, 0, 128, 32);
		mBall = new Ball(ballTexture);
		mBall.getBoundary().setY(33); // Just above the height of the paddle to start
		mFont = new BitmapFont();
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
		batch.begin();
		mBlockPatch.setColor(mPaddle.getColor());
		// Paddle uses same nine-patch as the bricks
		mBlockPatch.draw(batch, mPaddle.getBoundary().x, mPaddle.getBoundary().y,
				mPaddle.getBoundary().getWidth(), mPaddle.getBoundary().getHeight());
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
		if (mBall.detectCollision(mPaddle.getBoundary()) != CollisionDetector.CollisionEdge.NONE) {
			reverseYVelocity(ballVelocity);
		}
		if (bounds.overlaps(mGameWallBottom) || bounds.overlaps(mGameWallTop)) {
			reverseYVelocity(ballVelocity);
		}
		if (bounds.overlaps(mGameWallLeft) || bounds.overlaps(mGameWallRight)) {
			reverseXVelocity(ballVelocity);
		}

		// Detect ball to block collisions
		for (int i = 0; i < mBlocks.size; i++) {
			Block block = mBlocks.get(i);
			CollisionDetector.CollisionEdge edge = mBall.detectCollision(block.getBoundary());
			switch (edge) {
				case TOP:
				case BOTTOM:
					reverseYVelocity(ballVelocity);
					break;
				case LEFT:
				case RIGHT:
					reverseXVelocity(ballVelocity);
					break;
				default: continue; // to top of loop
			}
			mBlocks.removeIndex(i);
			break; // out of loop
		}

		bounds.setPosition(mBall.getPosition().add(ballVelocity));
	}

	private void reverseXVelocity(Vector2 ballVelocity) {
		ballVelocity.set(ballVelocity.x * -1, ballVelocity.y);
	}

	private void reverseYVelocity(Vector2 ballVelocity) {
		ballVelocity.set(ballVelocity.x, ballVelocity.y * -1);
	}


	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		float originalX = mPaddle.getBoundary().x;
		float newX =  originalX + deltaX;
		mPaddle.getBoundary().setX(newX);
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}
