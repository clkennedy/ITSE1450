/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.screens;

import com.dslayer.content.options.*;
import com.atkinson.game.engine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.audio.Music;
import java.awt.Desktop.Action;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
/**
 *
 * @author douglas.atkinson
 */
public class MainMenuScreen extends BaseScreen {
    
    BaseActor playButton;
    BaseActor instructionButton;
    BaseActor unlocksButton;
    BaseActor highscore;
    BaseActor newGame;
    BaseActor quitButton;
    
    BaseActor optionButton;
    
    BaseActor[] menuOptions = {playButton, optionButton, instructionButton, unlocksButton, quitButton, newGame};
    
    static int currentMenuIndex = -1;
    
    static Music backgroundMusic;
    
    public static boolean loaded = false;
    private static boolean musicPlaying = false;
    
    public void initialize()
    {
        
        BaseActor.setMainStage(mainStage);
        
        if(!musicPlaying){
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Shatter Me.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(Options.musicVolume);
            backgroundMusic.play();
            musicPlaying = true;
            
        }   
        
        //BaseActor ocean = new BaseActor(0,0, mainStage);
        //ocean.loadTexture( "sky.png" );
        //ocean.setSize(800,600);
        
        //Sky s1 = new Sky(0, 0, mainStage);
        //new Sky(s1.getWidth(), 0, mainStage);
        
        BaseActor title = new BaseActor(0,0, mainStage);
        title.loadTexture( "plane-dodger.png" );
        title.setSize(title.getWidth()  * Options.aspectRatio, title.getHeight() * Options.aspectRatio);
        title.setScale(1.3f);
        title.centerAtPosition(Gdx.graphics.getWidth() / 2,400 * Options.aspectRatio);
        title.moveBy(0,100);
        
       // Hover hListener = new Hover();
        Texture text = new Texture(Gdx.files.internal("play.png"));
        playButton = new BaseActor(0, 0, mainStage);
        playButton.loadTexture("play.png");
        playButton.setSize(text.getWidth() * Options.aspectRatio, text.getHeight() * Options.aspectRatio);
        playButton.setOriginX(playButton.getWidth() / 2);
        playButton.setOriginY(playButton.getHeight()/ 2);
        playButton.setBoundaryRectangle();
        playButton.getBoundaryPolygon();
        playButton.centerAtPosition(Gdx.graphics.getWidth() / 2,350 * Options.aspectRatio);
        playButton.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        
        });
        
        optionButton = new BaseActor(0, 0, mainStage);
        optionButton.loadTexture("options.png");
        optionButton.setSize((optionButton.getWidth() / 1.5f)  * Options.aspectRatio, (optionButton.getHeight() /1.5f)  * Options.aspectRatio);
        optionButton.setOriginX(optionButton.getWidth() / 2);
        optionButton.setOriginY(optionButton.getHeight()/ 2);
        optionButton.setBoundaryRectangle();
        optionButton.getBoundaryPolygon();
        optionButton.centerAtPosition(Gdx.graphics.getWidth() / 2,275 * Options.aspectRatio);
        optionButton.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionScreen();
            }
        
        });
        
        highscore = new BaseActor(0, 0, mainStage);
        highscore.loadTexture("highscore.png");
        highscore.setSize((highscore.getWidth() / 2)  * Options.aspectRatio, (highscore.getHeight() /2)  * Options.aspectRatio);
        highscore.setOriginX(highscore.getWidth() / 2);
        highscore.setOriginY(highscore.getHeight()/ 2);
        highscore.setBoundaryRectangle();
        highscore.getBoundaryPolygon();
        highscore.centerAtPosition(Gdx.graphics.getWidth() / 8,250 * Options.aspectRatio);
        
        //Label score = new Label(Integer.toString( Unlocks.getHighScore()), BaseGame.labelStyle);
        //score.setFontScale(.7f * Options.aspectRatio);
        //score.setSize(score.getWidth() / 2, score.getHeight() /2);
        //score.setPosition(highscore.getX() + highscore.getWidth(), highscore.getY());
        //mainStage.addActor(score);
        
        newGame = new BaseActor(0, 0, mainStage);
        newGame.loadTexture("newgame.png");
        newGame.setSize((newGame.getWidth() / 3)  * Options.aspectRatio, (newGame.getHeight() /3)  * Options.aspectRatio);
        newGame.setOriginX(newGame.getWidth() / 2);
        newGame.setOriginY(newGame.getHeight()/ 2);
        newGame.setBoundaryRectangle();
        newGame.getBoundaryPolygon();
        newGame.centerAtPosition(Gdx.graphics.getWidth() / 8,200 * Options.aspectRatio);
        newGame.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newGame();
            }
        
        });
        
        text = new Texture(Gdx.files.internal("instructions.png"));
        instructionButton = new BaseActor(0, 0, mainStage);
        instructionButton.loadTexture("instructions.png");
        instructionButton.setSize((text.getWidth() / 1.5f)  * Options.aspectRatio, (text.getHeight() /1.5f)  * Options.aspectRatio);
        instructionButton.setOriginX(instructionButton.getWidth() / 2);
        instructionButton.setOriginY(instructionButton.getHeight()/ 2);
        instructionButton.setBoundaryRectangle();
        instructionButton.getBoundaryPolygon();
        instructionButton.centerAtPosition(Gdx.graphics.getWidth() / 2,200 * Options.aspectRatio);
        instructionButton.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instructionScreen();
            }
        
        });
        
        unlocksButton = new BaseActor(0, 0, mainStage);
        unlocksButton.loadTexture("unlocks.png");
        unlocksButton.setSize((unlocksButton.getWidth() / 1.5f) * Options.aspectRatio, (unlocksButton.getHeight() /1.5f) * Options.aspectRatio);
        unlocksButton.setOriginX(unlocksButton.getWidth() / 2);
        unlocksButton.setOriginY(unlocksButton.getHeight()/ 2);
        unlocksButton.setBoundaryRectangle();
        unlocksButton.getBoundaryPolygon();
        unlocksButton.centerAtPosition(Gdx.graphics.getWidth() / 2,125 * Options.aspectRatio);
        unlocksButton.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unlocksScreen();
            }
        
        });
        
        quitButton = new BaseActor(0, 0, mainStage);
        quitButton.loadTexture("quit.png");
        quitButton.setSize((quitButton.getWidth() * 1.2f) * Options.aspectRatio, (quitButton.getHeight() *1.2f) * Options.aspectRatio);
        quitButton.setOriginX(quitButton.getWidth() / 2);
        quitButton.setOriginY(quitButton.getHeight()/ 2);
        quitButton.setBoundaryRectangle();
        quitButton.getBoundaryPolygon();
        quitButton.centerAtPosition(Gdx.graphics.getWidth() / 2,50 * Options.aspectRatio);
        quitButton.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quitGame();
            }
        
        });
        
        //BaseActor start = new BaseActor(0,0, mainStage);
       // start.loadTexture( "message-start.png" );
        //start.centerAtPosition(400,300);
       // start.moveBy(0,-100);
       currentMenuIndex = -1;
       this.show();
        
    }
    
    
    public void update(float dt) {
        //mainStage.act(dt);
        if (Gdx.input.isKeyPressed(Keys.S));
            //BaseGame.setActiveScreen( new LevelScreen() );
    }
    
    public void instructionScreen(){
        removeButtons();
        //BaseGame.setActiveScreen( new InstructionScreen());
    }
    
    public void newGame(){
        removeButtons();
        //Unlocks.reset();
        BaseGame.setActiveScreen(new MainMenuScreen());
    }
    
    public void unlocksScreen(){
       // System.err.println("Boo");
        removeButtons();
        //BaseGame.setActiveScreen( new Unlocks());
    }
    public void removeButtons(){
        playButton.remove();
        instructionButton.remove();
        unlocksButton.remove();
        newGame.remove();
        optionButton.remove();
    }
    public void startGame(){
        removeButtons();
        backgroundMusic.stop();
        musicPlaying = false;
        //BaseGame.setActiveScreen( new LevelScreen() );    
    }
    public void quitGame(){
        removeButtons();
        backgroundMusic.stop();
        musicPlaying = false;
        Progress.Save();
        Gdx.app.exit();
        System.exit(0);
    }
    public void optionScreen(){
        removeButtons();
        BaseGame.setActiveScreen( new Options() );    
    }
    
    public boolean keyDown(int keyCode) {
      if(keyCode == Keys.DOWN){
          descaleOption();
          currentMenuIndex++;
          if(currentMenuIndex > menuOptions.length - 1){
              currentMenuIndex = menuOptions.length - 1;
          }
          scaleOption();
      }
      if(keyCode == Keys.UP){
          descaleOption();
          currentMenuIndex--;
          if(currentMenuIndex < 0){
              currentMenuIndex = 0;
          }
          scaleOption();
      }  
      
      if(keyCode == Keys.ENTER){
          selectOption();
      }
        return true;
    }
    private void descaleOption(){
        if(currentMenuIndex >= 0)
            menuOptions[currentMenuIndex].setScale(1f);
    }
    
    private void scaleOption(){
        if(currentMenuIndex <= menuOptions.length - 1)
        menuOptions[currentMenuIndex].setScale(1.2f);
    }
    
    private void selectOption(){
        switch(currentMenuIndex){
            case 0:
                startGame();
            break;
            case 1:
                optionScreen();
            break;
            case 2:
                instructionScreen();
            break;
            case 3:
                unlocksScreen();
            break;
            case 4:
                quitGame();
            break;
            case 5:
                newGame();
            break;
            default:
            break;
        }
    } 
    
}