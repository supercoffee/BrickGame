package com.bendaschel.cs372.brickgame;

import dagger.Module;

@Module(
        includes = {
                GameModule.class
        },
        injects = {
                ScoreBoardFragment.class,
                GameFragment.class
        }
)
public class AndroidModule {
}
