/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.atkinson.game.engine.BaseScreen;
import com.atkinson.game.engine.Hover;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dslayer.content.screens.MainMenuScreen;

/**
 *
 * @author ARustedKnight
 */
public class Options extends BaseScreen{

    public static float musicVolume = 1f;
    public static float soundVolume = 1f;
    
    public static Label lMusicVol = new Label(Integer.toString((int)(musicVolume * 10)), BaseGame.labelStyle);
    public static Label lSoundVol = new Label(Integer.toString((int)(soundVolume * 10)), BaseGame.labelStyle);
    
    public static float aspectRatio = 1;
    
    static int currentMenuIndex = -1;
    BaseActor mainmenu;
    
    public static float desktopWidth= 0;
    public static float desktopheight= 0;
    
    public static enum DisplayType {FULLSCREEN, WINDOW_BORDERLESS, WINDOWED};
    
    public static DisplayType displayType = DisplayType.WINDOWED;
    
    public void initialize()
    {
        float w = 0;
        
        while(w < Gdx.graphics.getWidth()){
            //BaseActor bGround =new Sky(w, 0, mainStage, "sky.png");
            //bGround.setSpeed(0);
            //w+=bGround.getWidth();
        }
        
        BaseActor musicVol = new BaseActor(0, 0, mainStage);
        musicVol.loadTexture("musicvol.png");
        musicVol.setSize(musicVol.getWidth() * Options.aspectRatio, musicVol.getHeight()  * Options.aspectRatio);
        musicVol.setOriginX(musicVol.getWidth() / 2);
        musicVol.setOriginY(musicVol.getHeight()/ 2);
        musicVol.setBoundaryRectangle();
        musicVol.getBoundaryPolygon();
        musicVol.centerAtPosition(Gdx.graphics.getWidth() / 4,550 * Options.aspectRatio);
        
        BaseActor musicVolDec = new BaseActor(0, 0, mainStage);
        musicVolDec.loadTexture("leftarrow.png");
        musicVolDec.setSize(musicVolDec.getWidth()  * Options.aspectRatio, musicVolDec.getHeight() * Options.aspectRatio);
        musicVolDec.setOriginX(musicVolDec.getWidth() / 2);
        musicVolDec.setOriginY(musicVolDec.getHeight()/ 2);
        musicVolDec.setBoundaryRectangle();
        musicVolDec.getBoundaryPolygon();
        musicVolDec.setPosition(Gdx.graphics.getWidth() / 8,470 * Options.aspectRatio);
        musicVolDec.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                decreaseMusicVol();
            }
        
        });
        
        lMusicVol.setText(Integer.toString((int)(musicVolume * 10)));
        lMusicVol.setFontScale(.7f * Options.aspectRatio);
        lMusicVol.setColor(Color.FOREST);
        lMusicVol.setSize(lMusicVol.getWidth(), lMusicVol.getHeight());
        //lMusicVol.setPosition(Gdx.graphics.getWidth() / (Difficulty.worldWidth /125),460 * Options.aspectRatio);
        mainStage.addActor(lMusicVol);
        
        BaseActor musicVolInc = new BaseActor(0, 0, mainStage);
        musicVolInc.loadTexture("rightarrow.png");
        musicVolInc.setSize(musicVolInc.getWidth() * Options.aspectRatio, musicVolInc.getHeight() * Options.aspectRatio);
        musicVolInc.setOriginX(musicVolInc.getWidth() / 2);
        musicVolInc.setOriginY(musicVolInc.getHeight()/ 2);
        musicVolInc.setBoundaryRectangle();
        musicVolInc.getBoundaryPolygon();
        musicVolInc.setPosition(Gdx.graphics.getWidth() / 4, 470 * Options.aspectRatio);
        musicVolInc.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                increaseMusicVol();
            }
        
        });
        
        
        
        //Sound Options
        BaseActor soundVol = new BaseActor(0, 0, mainStage);
        soundVol.loadTexture("soundvol.png");
        soundVol.setSize(soundVol.getWidth() * Options.aspectRatio, soundVol.getHeight() * Options.aspectRatio);
        soundVol.setOriginX(soundVol.getWidth() / 2);
        soundVol.setOriginY(soundVol.getHeight()/ 2);
        soundVol.setBoundaryRectangle();
        soundVol.getBoundaryPolygon();
        soundVol.centerAtPosition(Gdx.graphics.getWidth() / 4,350 * Options.aspectRatio);
        
        BaseActor soundVolDec = new BaseActor(0, 0, mainStage);
        soundVolDec.loadTexture("leftarrow.png");
        soundVolDec.setSize(soundVolDec.getWidth() * Options.aspectRatio, soundVolDec.getHeight() * Options.aspectRatio);
        soundVolDec.setOriginX(soundVolDec.getWidth() / 2);
        soundVolDec.setOriginY(soundVolDec.getHeight()/ 2);
        soundVolDec.setBoundaryRectangle();
        soundVolDec.getBoundaryPolygon();
        soundVolDec.setPosition(Gdx.graphics.getWidth() / 8,270 * Options.aspectRatio);
        soundVolDec.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                decreaseSoundVol();
            }
        
        });
        
        lSoundVol.setText(Integer.toString((int)(soundVolume * 10)));
        lSoundVol.setFontScale(.7f * Options.aspectRatio);
        lSoundVol.setColor(Color.FOREST);
        lSoundVol.setSize(lSoundVol.getWidth(), lSoundVol.getHeight());
        //lSoundVol.setPosition(Gdx.graphics.getWidth() / (Difficulty.worldWidth /125),260 * Options.aspectRatio);
        mainStage.addActor(lSoundVol);
        
        BaseActor soundVolInc = new BaseActor(0, 0, mainStage);
        soundVolInc.loadTexture("rightarrow.png");
        soundVolInc.setSize(soundVolInc.getWidth() * Options.aspectRatio, soundVolInc.getHeight() * Options.aspectRatio);
        soundVolInc.setOriginX(soundVolInc.getWidth() / 2);
        soundVolInc.setOriginY(soundVolInc.getHeight()/ 2);
        soundVolInc.setBoundaryRectangle();
        soundVolInc.getBoundaryPolygon();
        soundVolInc.setPosition(Gdx.graphics.getWidth() / 4,270 * Options.aspectRatio);
        soundVolInc.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                increaseSoundVol();
            }
        
        });
        
        BaseActor lastDisplay = new BaseActor(0, 0, mainStage);
        lastDisplay.loadTexture("leftarrow.png");
        lastDisplay.setSize(lastDisplay.getWidth() * Options.aspectRatio, lastDisplay.getHeight() * Options.aspectRatio);
        lastDisplay.setOriginX(lastDisplay.getWidth() / 2);
        lastDisplay.setOriginY(lastDisplay.getHeight()/ 2);
        lastDisplay.setBoundaryRectangle();
        lastDisplay.getBoundaryPolygon();
        lastDisplay.setPosition(Gdx.graphics.getWidth() / 8,200 * Options.aspectRatio);
        lastDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lastDisplaySetting();
            }
        
        });
        
        Label displayText = new Label("", BaseGame.labelStyle);
        displayText.setText((displayType == DisplayType.FULLSCREEN)? "Fullscreen" :
                (displayType == DisplayType.WINDOWED)? "Windowed": "Borderless");
        displayText.setFontScale(.7f * Options.aspectRatio);
        displayText.setColor(Color.FOREST);
        displayText.setSize(displayText.getWidth(), displayText.getHeight());
        //displayText.setPosition(Gdx.graphics.getWidth() / (Difficulty.worldWidth /125),220* Options.aspectRatio);
        mainStage.addActor(displayText);
        
        BaseActor nextDisplay = new BaseActor(0, 0, mainStage);
        nextDisplay.loadTexture("rightarrow.png");
        nextDisplay.setSize(nextDisplay.getWidth() * Options.aspectRatio, nextDisplay.getHeight() * Options.aspectRatio);
        nextDisplay.setOriginX(nextDisplay.getWidth() / 2);
        nextDisplay.setOriginY(nextDisplay.getHeight()/ 2);
        nextDisplay.setBoundaryRectangle();
        nextDisplay.getBoundaryPolygon();
        nextDisplay.setPosition(displayText.getX()+ (190 * Options.aspectRatio),200 * Options.aspectRatio);
        nextDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextDisplaySetting();
            }
        
        });
        
        //mainmenu
        
        
        mainmenu = new BaseActor(0, 0, mainStage);
        mainmenu.loadTexture("mainmenu.png");
        mainmenu.setSize((mainmenu.getWidth() / 3) * Options.aspectRatio,(mainmenu.getHeight() /3) * Options.aspectRatio);
        mainmenu.setOriginX(mainmenu.getWidth() / 2);
        mainmenu.setOriginY(mainmenu.getHeight()/ 2);
        mainmenu.setBoundaryRectangle();
        mainmenu.getBoundaryPolygon();
        mainmenu.centerAtPosition(Gdx.graphics.getWidth() / 2,50 * Options.aspectRatio);
        mainmenu.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.getListenerActor().remove();
                Progress.Save();
                //BaseGame.setActiveScreen( new MenuScreen());
            }
        
        });
        
    }
    
    public static void increaseMusicVol(){
        musicVolume = ((musicVolume * 10) + 1) / 10;
        if(musicVolume >1){
            musicVolume = 1f;
        }
        if(musicVolume < 0){
            musicVolume = 0f;
        }
        lMusicVol.setText(Integer.toString((int)(musicVolume * 10)));
    }
    
    public static void decreaseMusicVol(){
        musicVolume = ((musicVolume * 10) - 1) / 10;
        
        if(musicVolume >1){
            musicVolume = 1f;
        }
        if(musicVolume < 0){
            musicVolume = 0f;
        }
        lMusicVol.setText(Integer.toString((int)(musicVolume * 10)));
    }
    
    public static void increaseSoundVol(){
        soundVolume = ((soundVolume * 10) + 1) / 10;
        
        if(soundVolume >1){
            soundVolume = 1f;
        }
        if(soundVolume < 0){
            soundVolume = 0f;
        }
        lSoundVol.setText(Integer.toString((int)(soundVolume * 10)));
    }
    
    public static void decreaseSoundVol(){
        soundVolume = ((soundVolume * 10) - 1) / 10;
        if(soundVolume >1){
            soundVolume = 1f;
        }
        if(soundVolume < 0){
            soundVolume = 0f;
        }
        lSoundVol.setText(Integer.toString((int)(soundVolume * 10)));
    }
    
    public static void nextDisplaySetting(){
        int d = displayType.ordinal();
        d++;
        if(d > 2){
            d = 0;
        }
        displayType = DisplayType.values()[d];
        setDisplay();
        BaseGame.setActiveScreen(new Options());
    }
    public static void lastDisplaySetting(){
        int d = displayType.ordinal();
        d--;
        if(d < 0){
            d = 2;
        }
        displayType = DisplayType.values()[d];
        setDisplay();
        BaseGame.setActiveScreen(new Options());
    }
    
    public void update(float dt) {
        
        //MenuScreen.backgroundMusic.setVolume(musicVolume);
    }
    @Override
    public boolean keyDown(int keyCode) {
      if(keyCode == Input.Keys.ESCAPE){
          BaseGame.setActiveScreen(new MainMenuScreen());
      }
      if(keyCode == Input.Keys.UP){
          descaleOption();
          currentMenuIndex--;
          currentMenuIndex = MathUtils.clamp(currentMenuIndex, -1, 0);
          scaleOption();
      }
      if(keyCode == Input.Keys.DOWN){
          descaleOption();
          currentMenuIndex++;
          currentMenuIndex = MathUtils.clamp(currentMenuIndex, -1, 0);
          scaleOption();
      }
      if(keyCode == Input.Keys.ENTER){
          if(currentMenuIndex == 0){
              BaseGame.setActiveScreen(new MainMenuScreen());
          }
      }
      if(keyCode == Input.Keys.F){
        displayType = DisplayType.FULLSCREEN;
        setDisplay();
        BaseGame.setActiveScreen(new Options());
      }
      if(keyCode == Input.Keys.W){
        displayType = DisplayType.WINDOWED;
        setDisplay();
        BaseGame.setActiveScreen(new Options());
      }
      if(keyCode == Input.Keys.B){
        displayType = DisplayType.WINDOW_BORDERLESS;
        setDisplay();
        BaseGame.setActiveScreen(new Options());
      }
        return true;
    }
    
    private void descaleOption(){
        
        mainmenu.setScale(1f);
    }
    private void scaleOption(){
        if(currentMenuIndex == 0)
            mainmenu.setScale(1.2f);
    }
    
    public static void setDisplay(){
        //System.err.println(Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor()).width);
        Gdx.graphics.setResizable(false);
        if(Gdx.graphics.supportsDisplayModeChange() && displayType == DisplayType.FULLSCREEN){
              if(Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode())){
                  displayType = DisplayType.FULLSCREEN;
                  Options.aspectRatio = (float)(Gdx.graphics.getHeight() /(float)Difficulty.worldHeight);
              }
          }
          else if(Gdx.graphics.supportsDisplayModeChange() && displayType == DisplayType.WINDOWED){
              Gdx.graphics.setUndecorated(false);
              if(Gdx.graphics.setWindowedMode((int)Difficulty.worldWidth, (int)Difficulty.worldHeight)){
                //System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                //Gdx.graphics.setUndecorated(true);
                  aspectRatio = Gdx.graphics.getHeight() /(float)Difficulty.worldHeight;
              }
          }
        else if(Gdx.graphics.supportsDisplayModeChange() && displayType == DisplayType.WINDOW_BORDERLESS){
            Gdx.graphics.setUndecorated(true);
              if(Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor()).width,
                      (int)Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor()).height)){
                aspectRatio = Gdx.graphics.getHeight() /(float)Difficulty.worldHeight;
                  
              }
          }
    }
    
}
