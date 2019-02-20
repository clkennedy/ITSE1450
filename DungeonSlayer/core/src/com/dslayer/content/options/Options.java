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
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.screens.MainMenuScreen;

/**
 *
 * @author ARustedKnight
 */
public class Options extends BaseScreen{

    public static float musicVolume = 1f;
    public static float soundVolume = 1f;
    
    public static Label lMusicVol = null;
    public static Label lSoundVol = null;
    
    public static float aspectRatio = 1;
    
    static int currentMenuIndex = -1;
    Label mainmenu;
    
    public static float baseWidth= 0;
    public static float baseHeight= 0;
    
    public static enum DisplayType {FULLSCREEN, WINDOW_BORDERLESS, WINDOWED};
    
    public static DisplayType displayType = DisplayType.WINDOWED;
    
    public void initialize()
    {
        float w = 0;
       
        
        Label musicVol = new Label("Music Volume", MainMenuScreen.buttonStyle);
        musicVol.setSize(musicVol.getWidth() * Options.aspectRatio, musicVol.getHeight()  * Options.aspectRatio);
        musicVol.setFontScale(1f * Options.aspectRatio);
        musicVol.setOriginX(musicVol.getWidth() / 2);
        musicVol.setOriginY(musicVol.getHeight()/ 2);
        musicVol.setAlignment(Align.center);
        musicVol.setPosition(Gdx.graphics.getWidth() / 8, 500 * Options.aspectRatio);
        
        mainStage.addActor(musicVol);
        
        Label musicVolDec = new Label("-", MainMenuScreen.buttonStyle);
        musicVolDec.setSize(musicVolDec.getWidth()  * Options.aspectRatio, musicVolDec.getHeight() * Options.aspectRatio);
        musicVolDec.setFontScale(1f * Options.aspectRatio);
        musicVolDec.setOriginX(musicVolDec.getWidth() / 2);
        musicVolDec.setOriginY(musicVolDec.getHeight()/ 2);
        musicVolDec.setAlignment(Align.center);
        musicVolDec.setPosition(Gdx.graphics.getWidth() / 8, 470 * Options.aspectRatio);
        musicVolDec.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                decreaseMusicVol();
            }
        
        });
        mainStage.addActor(musicVolDec);
        
        //lMusicVol
        lMusicVol = new Label("", MainMenuScreen.buttonStyle);
        lMusicVol.setText(Integer.toString((int)(musicVolume * 10)));
        lMusicVol.setStyle(MainMenuScreen.buttonStyle);
        lMusicVol.setFontScale(1f * Options.aspectRatio);
        lMusicVol.setSize(lMusicVol.getWidth(), lMusicVol.getHeight());
        lMusicVol.setOriginX(lMusicVol.getWidth() / 2);
        lMusicVol.setOriginY(lMusicVol.getHeight()/ 2);
        lMusicVol.setAlignment(Align.center);
        lMusicVol.setPosition((((musicVolDec.getX() + musicVolDec.getWidth()) ) + (40* Options.aspectRatio)),musicVolDec.getY() + musicVolDec.getOriginY());
        mainStage.addActor(lMusicVol);
        
        Label musicVolInc = new Label("+", MainMenuScreen.buttonStyle);
        musicVolInc.setSize(musicVolInc.getWidth() * Options.aspectRatio, musicVolInc.getHeight() * Options.aspectRatio);
        musicVolInc.setFontScale(1f * Options.aspectRatio);
        musicVolInc.setOriginX(musicVolInc.getWidth() / 2);
        musicVolInc.setOriginY(musicVolInc.getHeight()/ 2);
        musicVolInc.setAlignment(Align.center);
        musicVolInc.setPosition(lMusicVol.getX() + 40 * Options.aspectRatio, 470 * Options.aspectRatio);
        musicVolInc.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                increaseMusicVol();
            }
        
        });
        mainStage.addActor(musicVolInc);
        
        //Sound Options
        Label soundVol = new Label("Sound Volume", MainMenuScreen.buttonStyle);
        soundVol.setSize(soundVol.getWidth() * Options.aspectRatio, soundVol.getHeight() * Options.aspectRatio);
        soundVol.setFontScale(1f * Options.aspectRatio);
        soundVol.setOriginX(soundVol.getWidth() / 2);
        soundVol.setOriginY(soundVol.getHeight()/ 2);
        soundVol.setAlignment(Align.center);
        soundVol.setPosition(Gdx.graphics.getWidth() / 8,musicVolInc.getY() - musicVolInc.getHeight());
        mainStage.addActor(soundVol);
        
        
        Label soundVolDec = new Label("-", MainMenuScreen.buttonStyle);
        soundVolDec.setSize(soundVolDec.getWidth() * Options.aspectRatio, soundVolDec.getHeight() * Options.aspectRatio);
        soundVolDec.setFontScale(1f * Options.aspectRatio);
        soundVolDec.setOriginX(soundVolDec.getWidth() / 2);
        soundVolDec.setOriginY(soundVolDec.getHeight()/ 2);
        soundVolDec.setPosition(Gdx.graphics.getWidth() / 8,soundVol.getY() - soundVol.getHeight());
        soundVolDec.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                decreaseSoundVol();
            }
        
        });
        mainStage.addActor(soundVolDec);
        
        lSoundVol = new Label("", MainMenuScreen.buttonStyle);
        lSoundVol.setText(Integer.toString((int)(soundVolume * 10)));
        lSoundVol.setFontScale(1f * Options.aspectRatio);
        lSoundVol.setStyle(MainMenuScreen.buttonStyle);
        lSoundVol.setSize(lSoundVol.getWidth(), lSoundVol.getHeight());
        lSoundVol.setOriginX(lMusicVol.getWidth() / 2);
        lSoundVol.setOriginY(lMusicVol.getHeight()/ 2);
        lSoundVol.setAlignment(Align.center);
        lSoundVol.setPosition((((soundVolDec.getX() + soundVolDec.getWidth()) ) + (40* Options.aspectRatio)),soundVolDec.getY() + soundVolDec.getOriginY());
        mainStage.addActor(lSoundVol);
        
        Label soundVolInc = new Label("+", MainMenuScreen.buttonStyle);
        soundVolInc.setFontScale(1f * Options.aspectRatio);
        soundVolInc.setSize(soundVolInc.getWidth() * Options.aspectRatio, soundVolInc.getHeight() * Options.aspectRatio);
        soundVolInc.setOriginX(soundVolInc.getWidth() / 2);
        soundVolInc.setOriginY(soundVolInc.getHeight()/ 2);
        soundVolInc.setAlignment(Align.center);
        soundVolInc.setPosition(lMusicVol.getX() + 40 * Options.aspectRatio, 400 * Options.aspectRatio);
        soundVolInc.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                increaseSoundVol();
            }
        
        });
        mainStage.addActor(soundVolInc);
        
        Label lastDisplay = new Label("<", MainMenuScreen.buttonStyle);
        soundVolInc.setFontScale(1f * Options.aspectRatio);
        lastDisplay.setSize(lastDisplay.getWidth() * Options.aspectRatio, lastDisplay.getHeight() * Options.aspectRatio);
        lastDisplay.setOriginX(lastDisplay.getWidth() / 2);
        lastDisplay.setOriginY(lastDisplay.getHeight()/ 2);
        lastDisplay.setAlignment(Align.center);
        lastDisplay.setPosition(Gdx.graphics.getWidth() / 8,200 * Options.aspectRatio);
        lastDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lastDisplaySetting();
            }
        
        });
        mainStage.addActor(lastDisplay);
        
        Label displayText = new Label("", MainMenuScreen.buttonStyle);
        displayText.setText((displayType == DisplayType.FULLSCREEN)? "Fullscreen" :
                (displayType == DisplayType.WINDOWED)? "Windowed": "Borderless");
        displayText.setFontScale(1f * Options.aspectRatio);
        displayText.setSize(displayText.getWidth(), displayText.getHeight());
        displayText.setOriginX(lastDisplay.getWidth() / 2);
        displayText.setOriginY(lastDisplay.getHeight()/ 2);
        displayText.setAlignment(Align.center);
        displayText.setPosition(lastDisplay.getX() +(100 * Options.aspectRatio),220* Options.aspectRatio);
        mainStage.addActor(displayText);
        
        Label nextDisplay = new Label(">", MainMenuScreen.buttonStyle);
        soundVolInc.setFontScale(1f * Options.aspectRatio);
        nextDisplay.setSize(nextDisplay.getWidth() * Options.aspectRatio, nextDisplay.getHeight() * Options.aspectRatio);
        nextDisplay.setOriginX(nextDisplay.getWidth() / 2);
        nextDisplay.setOriginY(nextDisplay.getHeight()/ 2);
        nextDisplay.setAlignment(Align.center);
        nextDisplay.setPosition(displayText.getX()+ (100 * Options.aspectRatio),200 * Options.aspectRatio);
        nextDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextDisplaySetting();
            }
        
        });
        mainStage.addActor(nextDisplay);
        //mainmenu
        
        
        mainmenu = new Label("Main Menu", MainMenuScreen.buttonStyle);
        mainmenu.setFontScale(1f * Options.aspectRatio);
        mainmenu.setSize((mainmenu.getWidth() / 3) * Options.aspectRatio,(mainmenu.getHeight() /3) * Options.aspectRatio);
        mainmenu.setOriginX(mainmenu.getWidth() / 2);
        mainmenu.setOriginY(mainmenu.getHeight()/ 2);
        mainmenu.setAlignment(Align.center);
        mainmenu.setPosition(Gdx.graphics.getWidth() / 2,50);
        mainmenu.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.getListenerActor().remove();
                Progress.Save();
                BaseGame.setActiveScreen( new MainMenuScreen());
            }
        
        });
        mainStage.addActor(mainmenu);
        
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
                  Options.aspectRatio = (float)(Gdx.graphics.getHeight() /(float)baseHeight);
              }
          }
          else if(Gdx.graphics.supportsDisplayModeChange() && displayType == DisplayType.WINDOWED){
              Gdx.graphics.setUndecorated(false);
              if(Gdx.graphics.setWindowedMode((int)baseWidth, (int)baseHeight)){
                //System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                //Gdx.graphics.setUndecorated(true);
                  aspectRatio = Gdx.graphics.getHeight() /(float)baseHeight;
              }
          }
        else if(Gdx.graphics.supportsDisplayModeChange() && displayType == DisplayType.WINDOW_BORDERLESS){
            Gdx.graphics.setUndecorated(true);
              if(Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor()).width,
                      (int)Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor()).height)){
                aspectRatio = Gdx.graphics.getHeight() /(float)baseHeight;
                  
              }
          }
    }
    
}
