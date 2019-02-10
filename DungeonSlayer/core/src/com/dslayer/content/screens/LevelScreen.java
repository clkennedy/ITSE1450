package com.dslayer.content.screens;

import com.atkinson.game.engine.BaseScreen;
import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Options;
import com.dslayer.gamemodes.GameMode;
import com.dslayer.gamemodes.SurvivalGameMode;

public class LevelScreen extends BaseScreen {
    
    BaseActor gameOverMessage;
    
    private GameMode currentGamemode;
    
    Music backgroundMusic;
    
    private boolean returnToMainScreen = false;
    
    public void setGameMode(GameMode gm){
        currentGamemode = gm;
        currentGamemode.updateMainStage();
        currentGamemode.setup();
    }

    public void initialize() {
        
        BaseActor.setMainStage(mainStage);
        BaseActor.setUIStage(uiStage);
        Difficulty.passInLevelScreen(this);

        //stage setup
        if(currentGamemode != null){
        currentGamemode.updateMainStage();
        currentGamemode.setup();
        }
        
        // Instantiate Plane and set world bounds
        
        playing = true;
    }
	
    public void update(float dt) {
        
        if(currentGamemode.isGameOver())
        {
            gameover = true;
        }
        currentGamemode.update(dt);
    }
}
