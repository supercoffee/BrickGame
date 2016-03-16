package com.bendaschel.cs372.brickgame;

import dagger.Module;

@Module(
        includes = {
                GameModule.class
        },
        injects = {
                ScoreBoardFragment.class
        }
)
public class AndroidModule {
}
