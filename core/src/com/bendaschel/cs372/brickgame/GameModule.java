package com.bendaschel.cs372.brickgame;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        library = true,
        injects = {
                BrickGame.class
        }
)
public class GameModule {

    @Provides
    @Singleton
    public EventBus provideGameEventBus() {
        return EventBus.builder().build();
    }
}
