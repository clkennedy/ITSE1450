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
    
    private Player player;
    private boolean gameOver;
    BaseActor gameOverMessage;
    
    private static GameMode currentGamemode;
    
    private BaseEnemy debugEnemy;
    Music backgroundMusic;
    
    
    private boolean returnToMainScreen = false;
    
    public static void setGameMode(GameMode gm){
        currentGamemode = gm;
    }

    public void initialize() {
        
        BaseActor.setMainStage(mainStage);
        Difficulty.passInLevelScreen(this);

        //stage setup
        currentGamemode.updateMainStage();
        currentGamemode.setup();
        player = currentGamemode.getPlayer();
        // Instantiate Plane and set world bounds
        
        playing = true;
    }
	
    public void update(float dt) {
        currentGamemode.update(dt);
    }
}
