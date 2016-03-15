package com.bendaschel.cs372.brickgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.bendaschel.cs372.brickgame.BrickGame;

public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks
{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Fragment gameFragment = new GameFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, gameFragment)
				.commit();

	}

	@Override
	public void exit() {

	}
}
