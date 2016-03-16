package com.bendaschel.cs372.brickgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import dagger.ObjectGraph;

public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks
{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeObjectGraph();
		setContentView(R.layout.activity_main);
		Fragment gameFragment = new GameFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, gameFragment)
				.commit();

	}

	private void initializeObjectGraph() {
		ObjectGraphHolder.sObjectGraph = ObjectGraph.create(new AndroidModule());
	}

	@Override
	public void exit() {

	}
}
