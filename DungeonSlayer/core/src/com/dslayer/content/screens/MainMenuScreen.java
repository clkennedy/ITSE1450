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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import java.awt.Desktop.Action;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import sun.font.TrueTypeFont;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Font.FontLoader;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.gamemodes.SurvivalGameMode;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import static java.lang.System.gc;
//import org.json.JSONObject;

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
    
    public static Music backgroundMusic;
    
    public static boolean loaded = false;
    public static boolean musicPlaying = false;
    Label multi;
    
    public void initialize()
    {
        BaseScreen.cleanUp();
        gc();
        RoomPanels.resetDefaultSize();
        
        Multiplayer.restartNetworkid();
        paused = false;
        
       if(Multiplayer.socket != null && Multiplayer.socket.connected()){
           Multiplayer.socket.disconnect();
           Multiplayer.socket.close();
           Multiplayer.socket = null;
       }
        
        BaseActor.setMainStage(mainStage);
        
        BaseActor b = new BaseActor(0,0,mainStage);
        String[] bgFrames = new String[500];
        int gifOffset = 0;
        if(MathUtils.randomBoolean(.4f)){
            gifOffset = 0 + 455;
        }else if(MathUtils.randomBoolean(.4f)){
            gifOffset = 0 + 1137;
        }
        for(int i = 0; i < bgFrames.length; i ++){
            bgFrames[i] = "Background/Frames/b " + (i + gifOffset) + ".png";
        }
        b.loadAnimationFromFiles(bgFrames,.05f, true);
        b.setOrigin(0,0);
        b.setScale(Options.aspectRatio);
        if(Options.displayType != Options.DisplayType.WINDOWED ){
            b.setPosition((Gdx.graphics.getWidth()/ 2) - (b.getWidth()), 0);
        }
        
        if(!musicPlaying){
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/8BitDungTitle.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(Options.musicVolume);
            backgroundMusic.play();
            musicPlaying = true;
        }
        //BaseGame.labelStyle = FontLoader.menuStyle;
        
        Label l = new Label("Ironside", FontLoader.titleStyle);
        l.setPosition((mainStage.getWidth()/ 2) - (l.getWidth()/2), mainStage.getHeight() - 100);
        
        mainStage.addActor(l);
        //s.font.getData().setScale(.8f);
        //TextButtonStyle textButtonStyle = new TextButtonStyle(, scoreDrawable, scoreDrawable, font );

        //TextButton tb = new TextButton("Play", );
        
        Label playDungeon = new Label("Solo", FontLoader.menuStyle);
        playDungeon.setSize((playDungeon.getWidth() * 1.2f) * Options.aspectRatio, (playDungeon.getHeight() *1.2f) * Options.aspectRatio);
        playDungeon.setOriginX(playDungeon.getWidth() / 2);
        playDungeon.setOriginY(playDungeon.getHeight()/ 2);
        playDungeon.setPosition((mainStage.getWidth()/ 2) - (playDungeon.getWidth()/2), (l.getY() - l.getHeight()/2) - 100);
        playDungeon.setOrigin(playDungeon.getWidth()/2, playDungeon.getHeight()/2);
        playDungeon.setAlignment(Align.center);
        playDungeon.addListener(new Hover(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameDungeon();
            }
        
        });
        mainStage.addActor(playDungeon);
        
        Label play = new Label("Heros", FontLoader.menuStyle);
        play.setSize((play.getWidth() * 1.2f) * Options.aspectRatio, (play.getHeight() *1.2f) * Options.aspectRatio);
        play.setOriginX(play.getWidth() / 2);
        play.setOriginY(play.getHeight()/ 2);
        play.setPosition((mainStage.getWidth()/ 2) - (play.getWidth()/2), (playDungeon.getY() - playDungeon.getHeight()/2) - 30);
        play.setOrigin(play.getWidth()/2, play.getHeight()/2);
        play.setAlignment(Align.center);
        play.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                heroScreen();
            }
        
        });
        mainStage.addActor(play);
        
        multi = new Label("Multiplayer", FontLoader.menuStyle);
        multi.setSize((multi.getWidth() * 1.2f) * Options.aspectRatio, (multi.getHeight() *1.2f) * Options.aspectRatio);
        multi.setOriginX(multi.getWidth() / 2);
        multi.setOriginY(multi.getHeight()/ 2);
        multi.setPosition((mainStage.getWidth()/ 2) - (multi.getWidth()/2), (play.getY() - play.getHeight()/2) - 30);
        multi.setOrigin(multi.getWidth()/2, multi.getHeight()/2);
        multi.setAlignment(Align.center);
        
        multi.addListener(new Hover(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                multiplayerGame();
            }

        });
        
        mainStage.addActor(multi);
        
        mainStage.addActor(play);
        
        Label options = new Label("Options", FontLoader.menuStyle);
        options.setSize((options.getWidth() * 1.2f) * Options.aspectRatio, (options.getHeight() *1.2f) * Options.aspectRatio);
        options.setOriginX(options.getWidth() / 2);
        options.setOriginY(options.getHeight()/ 2);
        options.setPosition((mainStage.getWidth()/ 2) - (options.getWidth()/2), (multi.getY() - multi.getHeight()/2) - 30);
        options.setOrigin(options.getWidth()/2, options.getHeight()/2);
        options.setAlignment(Align.center);
        options.addListener(new Hover(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                optionScreen();
            }
        });
        
        mainStage.addActor(options);
        
        
        Label quit = new Label("Quit", FontLoader.menuStyle);
        quit.setSize((quit.getWidth() * 1.2f) * Options.aspectRatio, (quit.getHeight() *1.2f) * Options.aspectRatio);
        quit.setOriginX(quit.getWidth() / 2);
        quit.setOriginY(quit.getHeight()/ 2);
        quit.setPosition((mainStage.getWidth()/ 2) - (quit.getWidth()/2), (options.getY() - options.getHeight()));
        quit.setAlignment(Align.center);
        quit.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quitGame();
            }
        
        });
        mainStage.addActor(quit);
       
       currentMenuIndex = -1;
       this.show();
        
    }
    
    public void hero(){
        BaseGame.setActiveScreen(new HeroSelectionScreen());
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
        
    }
    
    public void multiplayerGame(){
        removeButtons();
        backgroundMusic.stop();
        musicPlaying = false;
        //multiplayerRoomScreen.rejoined = true;
        //Multiplayer.baseScreen = new multiplayerRoomScreen();
        //Multiplayer.lobbyScreen = new MutliplayerLobbyScreen();
        BaseGame.setActiveScreen(new multiplayerRoomScreen());   
    }
    public void heroScreen(){
        removeButtons();
        //backgroundMusic.stop();
        //musicPlaying = false;
        //BaseGame.setActiveScreen(new HeroSelectionScreen());   
    }
    public void startGameDungeon(){
        removeButtons();
        //backgroundMusic.stop();
        //musicPlaying = false;
        BaseGame.setActiveScreen(new HeroSelectionScreen());   
    }
    public void quitGame(){
        removeButtons();
        backgroundMusic.stop();
        musicPlaying = false;
        Progress.Save();
        FontLoader.dispose();
        gc();
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
                heroScreen();
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
    
    
    @Override
    public void dispose(){
        Progress.Save();
        
        super.dispose();
    }
}