package com.bendaschel.cs372.brickgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bendaschel.cs372.brickgame.events.BallLostEvent;
import com.bendaschel.cs372.brickgame.events.BrickDestroyedEvent;
import com.bendaschel.cs372.brickgame.events.GameOverEvent;
import com.bendaschel.cs372.brickgame.events.GameStartedEvent;
import com.bendaschel.cs372.brickgame.events.LevelCompleteEvent;
import com.bendaschel.cs372.brickgame.events.ScoreEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class BrickGame extends ApplicationAdapter implements GestureDetector.GestureListener {

	public static final int INITIAL_BALLS_REMAINING = 3;
	static final int NUM_BLOCKS = 20;
	static final int BG_COLOR_RED = 1;
	static final int BG_COLOR_GREEN = 1;
	static final int BG_COLOR_BLUE = 1;
	static final int BG_ALPHA = 1;
	private static final int BLOCKS_PER_ROW = 10;
	private static final int MAX_BLOCK_ROWS = 10;
	private static final int INITIAL_SCORE = 0;
	private static final int STARTING_LEVEL = 1;
	private static final float BALL_VELOCITY_INCREASE_PER_LEVEL = 1.25f;
	private static final float PADDLE_WIDTH = 128;
	private final EventBus mEventBus;
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

	private ScoreEvent mScoreEvent;
	private BrickDestroyedEvent mBrickDestroyedEvent;
	private BallLostEvent mBallLostEvent;
	private GameOverEvent mGameOverEvent;
	private boolean mGameRunning;

	private int mGameTotalScore;
	private int mLevelScore;
	private int mCurrentLevel;

	@Inject
	public BrickGame(EventBus eventBus) {
		mEventBus = eventBus;
	}

	/**
	 * Automatically called by game engine when initialized in GameFragment
	 */
	@Override
	public void create () {
		// Resources
		Gdx.input.setInputProcessor(new GestureDetector(this));
		batch = new SpriteBatch();
		Texture ballTexture = new Texture("ball.png");
		mBlockPatch = new NinePatch(new Texture("block.png"), 6, 6, 6, 6);
		// game actor objects
		mPaddle = new Paddle(Color.GREEN);
		mBall = new Ball(ballTexture);
		// Game walls
		mScreenHeight = Gdx.graphics.getHeight();
		mScreenWidth = Gdx.graphics.getWidth();
		mGameWallTop = new Rectangle(0, mScreenHeight, mScreenWidth, 1);
		mGameWallLeft = new Rectangle(-1, 0, 1, mScreenHeight);
		mGameWallRight = new Rectangle(mScreenWidth, 0, 1, mScreenHeight);
		mGameWallBottom = new Rectangle(0, -1, mScreenWidth, 1);
		// put everything in it's starting place
		startGame();
	}


	/**
	 * Begins the game with an initial settings
	 */
	public void startGame() {
		// TODO: when using random blocks, we need to restore previous configuration
		mBall.getVelocity().set(2, -2);
		startGame(createBlocks(), INITIAL_SCORE, INITIAL_BALLS_REMAINING, STARTING_LEVEL);
	}

	private void startGame(Array<Block> startingBlocks, int score, int numBalls, int level) {
		// Events
		mCurrentLevel = level;
		mGameTotalScore = score;
		mLevelScore = 0;
		mScoreEvent = new ScoreEvent();
		mBrickDestroyedEvent = new BrickDestroyedEvent(startingBlocks.size);
		mBallLostEvent = new BallLostEvent(numBalls);
		mGameOverEvent = new GameOverEvent();
		mPaddle.getBoundary().set(mScreenWidth / 2 - (mPaddle.getBoundary().getWidth() /2), 0, PADDLE_WIDTH, 32 );
		mBlocks = startingBlocks;
		spawnBall(numBalls);
		setGameRunning(true);
		mEventBus.post(new GameStartedEvent(score, numBalls, level, mBlocks.size));
	}

	/**
	 * Begins the game again from the last played configuration
	 * Restores brick layout and ball speed
	 * Reset paddle and ball position
	 */
	public void restartLevel() {
		Array<Block> blocks = createBlocks();
		startGame(blocks, mGameTotalScore, INITIAL_BALLS_REMAINING, mCurrentLevel);
	}

	private Array<Block> createBlocks() {
		Array<Block> blocks = new Array<Block>(NUM_BLOCKS);
		int blockHeight = getBlockHeight();
		int blockWidth = mScreenWidth / BLOCKS_PER_ROW;
		int row = 1;
		for (int i = 0; i < NUM_BLOCKS; i++, row = (i/ BLOCKS_PER_ROW) + 1){
			int x = (i % BLOCKS_PER_ROW) * blockWidth;
			int y = mScreenHeight - (row * blockHeight);
			Block block = new Block(Color.RED, new Rectangle(x, y, blockWidth, blockHeight));
			blocks.add(block);
		}
		return blocks;
	}

	private int getBlockHeight() {
		return mScreenHeight / 2 / MAX_BLOCK_ROWS;
	}

	@Override
	public void render () {
		if (mGameRunning) {
			doRender();
		}
	}

	private void doRender() {
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
			// change ball position to prevent glitchiness when balls collides with paddle side
			bounds.setY(mPaddle.getBoundary().y + mPaddle.getBoundary().getHeight() + 1);
			reverseYVelocity(ballVelocity);
		}
		if (bounds.overlaps(mGameWallTop)) {
			reverseYVelocity(ballVelocity);
		}
		if (bounds.overlaps(mGameWallLeft) || bounds.overlaps(mGameWallRight)) {
			reverseXVelocity(ballVelocity);
		}
		if (bounds.overlaps(mGameWallBottom)) {
			onBallLost();
			return;
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
			onBrickDestroyed();
			break; // out of loop
		}

		bounds.setPosition(mBall.getPosition().add(ballVelocity));
	}

	private void onBrickDestroyed() {
		mLevelScore++;
		mScoreEvent.score = mLevelScore + mGameTotalScore;
		mEventBus.post(mScoreEvent);
		mBrickDestroyedEvent.bricksRemaining = mBlocks.size;
		mEventBus.post(mBrickDestroyedEvent);
		if (mBlocks.size == 0) {
			onLevelComplete();
		}
	}

	private void onLevelComplete() {
		setGameRunning(false);
		mEventBus.post(new LevelCompleteEvent());
	}

	private void onBallLost() {
		mBallLostEvent.ballRemaining--;
		mEventBus.post(mBallLostEvent);
		if (spawnBall(mBallLostEvent.ballRemaining)) {
			return;
		}
		setGameRunning(false);
		mEventBus.post(mGameOverEvent);
	}

	/**
	 * Spawn a ball for values greater than 0
	 * @param ballsRemaining
	 * @return true if ball was spawned
	 */
	private boolean spawnBall(int ballsRemaining) {
		if (ballsRemaining > 0){
			mBall.getBoundary().setPosition(mScreenWidth / 2, (mScreenHeight / 2) - getBlockHeight());
			mBall.getVelocity().setAngle(-45);
		}
		return ballsRemaining > 0;
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
		setGameRunning(!mGameRunning);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		onLevelComplete(); // for testing easily
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

	public void setGameRunning(boolean gameRunning) {
		mGameRunning = gameRunning;
	}

	public void nextLevel() {
		Array<Block> blocks = createBlocks();
		mBall.getVelocity().scl(BALL_VELOCITY_INCREASE_PER_LEVEL);
		startGame(blocks, mGameTotalScore + mLevelScore, INITIAL_BALLS_REMAINING, mCurrentLevel + 1);
	}
}
