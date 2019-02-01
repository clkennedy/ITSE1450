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
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Options;

public class LevelScreen extends BaseScreen {
    
    // Plane
    //private Plane plane;
    
    private float starTimer;
    private float starSpawnInterval;
    private float starSpawnChance;
    public static int score;
    static Label scoreLabel;
    
    //enemy stuffs
    private float enemyTimer;
    public float enemySpawnInterval;
    public float enemySpeed;
    public float doubleEnemyChance;
    
    //chicken stuffs
    private float chickenTimer;
    public float chickenSpawnInterval;
    public float chickenSpeed;
    public float chickenSpawnChance;
    
     //powerup stuffs
    private float powerupSpawnInterval;
    private float powerupTimer;
    private float powerupSpeed;
    private float powerupChance;
    private boolean powerupSpawned;
    
    //points for living
    private float addPointsInterval = 15;
    private float addPointsTimer = 0;
    private int PointsToAdd = 3;
    
    private boolean gameOver;
    BaseActor gameOverMessage;
    
    Music backgroundMusic;
    Sound explosionSound;
    Sound sparkleSound;
    
    private boolean returnToMainScreen = false;
    
    

    public void initialize() {
        
        //private Player player;
        
        BaseActor.setMainStage(mainStage);
        Difficulty.passInLevelScreen(this);
        float w = 0;
        float gw = 0;
        
        
        //stage setup
        
        
        
        // Instantiate Plane and set world bounds
        
        BaseActor.setWorldBounds(800, 600);
         
        starSpawnInterval = 4;
        starTimer = MathUtils.random(starSpawnInterval);
        starSpawnChance = .9f;
        
        score = 0;
        scoreLabel = new Label(Integer.toString(score), BaseGame.labelStyle);
        uiTable.pad(10);
        uiTable.add(scoreLabel);
        uiTable.row();
        uiTable.add().expandY();
        
        
        //enemy stuffs
        enemySpawnInterval = Difficulty.ENEMY_INTERVAL * Options.aspectRatio;
        enemyTimer = MathUtils.random(enemySpawnInterval);
        enemySpeed = Difficulty.ENEMY_SPEED * Options.aspectRatio;
        doubleEnemyChance =.1f;
        
        //chicken stuffs
        chickenSpawnInterval = Difficulty.CHICKEN_INTERVAL;
        chickenTimer = MathUtils.random(chickenSpawnInterval);
        chickenSpeed = 50;
        chickenSpawnChance = .5f;
        
        
        //powerup stuffs
        powerupSpawnInterval = 10;
        powerupTimer = MathUtils.random(powerupSpawnInterval);
        powerupSpeed = 200;
        powerupChance = .6f;
        powerupSpawned = false;
        
        gameOver = false;
        
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(Options.musicVolume);
        backgroundMusic.play();
        sparkleSound = Gdx.audio.newSound(Gdx.files.internal("sparkle.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
        
        //plane = new Plane(100, 500, mainStage);
        
        Difficulty.newGame();
    }
	
    public void update(float dt) {
        
    }
}
