package com.bendaschel.cs372.brickgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class GameFragment extends AndroidFragmentApplication {

    BrickGame mGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGame = ObjectGraphHolder.sObjectGraph.get(BrickGame.class);
        return initializeForView(mGame);
    }


}
