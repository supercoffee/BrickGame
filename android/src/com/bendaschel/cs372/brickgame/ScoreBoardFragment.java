package com.bendaschel.cs372.brickgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bendaschel.sevensegmentview.SevenSegmentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreBoardFragment extends Fragment {

    @Bind(R.id.ssv_score)
    SevenSegmentView mScoreView;

    @Bind(R.id.ssv_bricks_remaining)
    SevenSegmentView mBricksView;

    @Bind(R.id.ssv_balls_remaining)
    SevenSegmentView mBallsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_menu)
    public void onMenuClicked(View menuButton) {

        // TODO: Change to menu screen with more options
        Toast.makeText(getActivity(), R.string.toast_about, Toast.LENGTH_LONG).show();
    }
}
