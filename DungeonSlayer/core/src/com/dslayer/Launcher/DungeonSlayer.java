package com.dslayer.Launcher;

import com.atkinson.game.engine.BaseGame;
import static com.atkinson.game.engine.BaseGame.setActiveScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dslayer.content.options.Progress;
import com.dslayer.content.screens.LoadScreen;
import com.dslayer.content.screens.LogoScreen;
import com.dslayer.content.screens.MainMenuScreen;

public class DungeonSlayer extends BaseGame {
    
    public void create()
    {
        super.create();
        Progress.Load();
        setActiveScreen( new LogoScreen() );
    }
}
