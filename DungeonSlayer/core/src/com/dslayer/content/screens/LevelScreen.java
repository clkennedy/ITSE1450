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
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Options;

public class LevelScreen extends BaseScreen {
    
    private Player player;
    private boolean gameOver;
    BaseActor gameOverMessage;
    
    Music backgroundMusic;
    
    
    private boolean returnToMainScreen = false;
    
    

    public void initialize() {
        
        //private Player player;
        
        BaseActor.setMainStage(mainStage);
        Difficulty.passInLevelScreen(this);
        float w = 0;
        float gw = 0;
        
        
        
        Room dr = new DungeonRoom();
        dr.generateRoom(30,40);
        
        Difficulty.worldHeight = dr.getRoomHeight();
        Difficulty.worldWidth = dr.getRoomWidth();
        Difficulty.newGame();
        
        System.out.println(Gdx.graphics.getHeight());
        
        dr.Draw(mainStage);
        
        player = new Player(400,300, mainStage);
        //stage setup
        
        // Instantiate Plane and set world bounds
        
        
    }
	
    public void update(float dt) {
        
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
            //BaseGame.setActiveScreen( new MainMenuScreen());
        }
        
    }
}
