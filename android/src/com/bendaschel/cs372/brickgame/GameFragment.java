package com.bendaschel.cs372.brickgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.bendaschel.cs372.brickgame.events.GameOverEvent;
import com.bendaschel.cs372.brickgame.events.LevelCompleteEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class GameFragment extends AndroidFragmentApplication {

    private static final String TAG = "GameFragment";
    @Inject
    BrickGame mGame;

    @Inject
    EventBus mEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraphHolder.sObjectGraph.inject(this);
        mEventBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initializeForView(mGame);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameOver(final GameOverEvent event){
        if (!event.won){
            new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setTitle(R.string.game_over)
                    .setMessage(R.string.play_again)
                    .setPositiveButton(R.string.retry_level, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "Restarting level");
                            mGame.restartLevel();
                        }
                    })
                    .setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "Starting game over");
                            mGame.startGame();
                        }
                    })
                    .create().show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLevelComplete(LevelCompleteEvent event) {
        // TODO: SHOW continue dialog
        Log.d(TAG, "onLevelComplete");
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle(R.string.level_complete)
                .setPositiveButton(R.string.next_level, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Starting next level");
                        mGame.nextLevel();
                    }
                })
                .setNegativeButton(R.string.retry_level, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Restarting level");
                        mGame.restartLevel();
                    }
                })
                .create().show();

    }
}
