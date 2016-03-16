package com.bendaschel.cs372.brickgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bendaschel.cs372.brickgame.events.BrickDestroyedEvent;
import com.bendaschel.cs372.brickgame.events.ScoreEvent;
import com.bendaschel.sevensegmentview.SevenSegmentView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreBoardFragment extends Fragment {

    @Bind(R.id.ssv_score)
    SevenSegmentView mScoreView;
    @Bind(R.id.ssv_score2)
    SevenSegmentView mScoreView2;
    @Bind(R.id.ssv_score3)
    SevenSegmentView mScoreView3;
    private SevenSegmentViewAdapter mScoreAdapter;

    @Bind(R.id.ssv_bricks_remaining)
    SevenSegmentView mBricksView;
    @Bind(R.id.ssv_bricks_remaining2)
    SevenSegmentView mBricksView2;
    private SevenSegmentViewAdapter mBricksAdapter;

    @Bind(R.id.ssv_balls_remaining)
    SevenSegmentView mBallsView;

    @Inject
    EventBus mGameEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectGraphHolder.sObjectGraph.inject(this);
        mGameEventBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mScoreAdapter = new SevenSegmentViewAdapter(mScoreView, mScoreView2, mScoreView3);
        mBricksAdapter = new SevenSegmentViewAdapter(mBricksView, mBricksView2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameEventBus.unregister(this);
    }

    @OnClick(R.id.btn_menu)
    public void onMenuClicked(View menuButton) {

        // TODO: Change to menu screen with more options
        Toast.makeText(getActivity(), R.string.toast_about, Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScoreChanged(ScoreEvent event){
        mScoreAdapter.setCurrentValue(event.score);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onBrickDestroyed(BrickDestroyedEvent event) {
        mBricksAdapter.setCurrentValue(event.bricksRemaining);
    }
}
