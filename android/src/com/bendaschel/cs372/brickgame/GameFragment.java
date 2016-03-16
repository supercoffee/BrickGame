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
                        }
                    })
                    .setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "Starting game over");
                            mGame.setBalls(BrickGame.INITIAL_BALLS_REMAINING);
                            mGame.setGameRunning(true);
                        }
                    })
                    .create().show();
        }
    }
}
