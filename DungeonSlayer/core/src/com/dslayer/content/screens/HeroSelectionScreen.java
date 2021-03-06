package com.dslayer.content.screens;

import com.atkinson.game.engine.BaseScreen;
import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.atkinson.game.engine.Hover;

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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Font.FontLoader;
import com.dslayer.content.Hero.Hero;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.options.Avatar;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import com.dslayer.content.options.Unlocks;
import static com.dslayer.content.options.Unlocks.currentAvatar;
import static com.dslayer.content.screens.MainMenuScreen.backgroundMusic;
import static com.dslayer.content.screens.MainMenuScreen.musicPlaying;
import com.dslayer.gamemodes.DungeonCrawlGameMode;
import com.dslayer.gamemodes.GameMode;
import com.dslayer.gamemodes.SurvivalGameMode;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;

public class HeroSelectionScreen extends BaseScreen {
    
    BaseActor gameOverMessage;
    
    public static Hero hero;
    Music backgroundMusic;
    
    private boolean returnToMainScreen = false;
    
    public static Hero currentSelection = Hero.getNewHero(Hero.heros.ClassicHero);
    public static int HeroSelectionIndex = 0;
    
    private BaseActor selectionBox;
    
    Label displayText;
    Label displayGMText;

    public ArrayList<BaseActor> heroSelections;
    public void initialize() {
        
        BaseActor.setMainStage(mainStage);
        BaseActor.setUIStage(uiStage);
        heroSelections = new ArrayList<BaseActor>();
        //private Player player;
        
        currentSelection = Hero.getNewHero(Hero.heros.values()[HeroSelectionIndex]);
        
        Label l = new Label("Select Hero", FontLoader.titleStyle);
        l.setPosition((mainStage.getWidth()/ 2) - (l.getWidth()/2), mainStage.getHeight() - 100);
        mainStage.addActor(l);
        
        float w = 0;
        
        for(Hero.heros bActor : Hero.heros .values()){
            Hero h = Hero.getNewHero(bActor);
            BaseActor b = new BaseActor();
            b.setSize(100, 100);
            b.setOrigin(b.getWidth() / 2, b.getHeight() / 2);
            w += b.getWidth() + 50;
            b.remove();
        }
        float halfW = w / 2;
        int count = 0;
        for(Hero.heros bActor : Hero.heros .values()){
            Hero h = Hero.getNewHero(bActor);
            BaseActor b = new BaseActor();
            Label name = new Label(h.getName(), FontLoader.pointStyle);
            b.setAnimation(h.playRight());
            b.setSize(100, 100);
            b.setOrigin(b.getWidth() / 2, b.getHeight() / 2);
            b.centerAtPosition((Gdx.graphics.getWidth() /2) - halfW + (b.getWidth() /2) + (count * 50) , l.getY() - l.getHeight());
            
            name.setPosition((b.getX() + ((b.getWidth()/2)-(name.getWidth() /2))),b.getY() + b.getHeight() );
            name.setAlignment(Align.center);
            mainStage.addActor(name);
            if(HeroSelectionIndex == count){
                selectionBox = new BaseActor(0,0, mainStage);
                selectionBox.loadTexture( "Selections/selectionRed.png" );
                //selectionBox.setSize((140* scale)*Options.aspectRatio, (130 * scale)*Options.aspectRatio);
                selectionBox.setOrigin(selectionBox.getWidth() / 2, selectionBox.getHeight()/ 2);
                selectionBox.centerAtActor(b);
            }else{
                b.addListener(new Hover(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        updateSelected(event, x, y);
                    }
                });
            }
            halfW -= b.getWidth();
            count++;
            heroSelections.add(b);
        }
        count = 0;
        
        Label ready = new Label("Ready", FontLoader.menuStyle);
        ready.setPosition((mainStage.getWidth()/ 2) - (ready.getWidth()/2), ready.getHeight() + 10);
        ready.setOrigin(ready.getWidth()/2, ready.getHeight()/2);
        ready.setAlignment(Align.center);
        ready.addListener(new Hover(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        startGame();
                    }
                });
        mainStage.addActor(ready);
        
        Label cancel = new Label("Cancel", FontLoader.menuStyle);
        cancel.setPosition((mainStage.getWidth()/ 2) - (cancel.getWidth()/2),  10);
        cancel.setOrigin(cancel.getWidth()/2, cancel.getHeight()/2);
        cancel.setAlignment(Align.center);
        cancel.addListener(new Hover(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        cancelGame();
                    }
                });
        mainStage.addActor(cancel);
        
        
        //------------------------------------------------------------------------------------------------------------------------------
        Label lastDisplay = new Label("<", FontLoader.buttonStyle);
        lastDisplay.setSize(lastDisplay.getWidth() * Options.aspectRatio, lastDisplay.getHeight() * Options.aspectRatio);
        lastDisplay.setOriginX(lastDisplay.getWidth() / 2);
        lastDisplay.setOriginY(lastDisplay.getHeight()/ 2);
        lastDisplay.setAlignment(Align.left);
        lastDisplay.setPosition(50,  50);
        lastDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousDungeon();
            }
        
        });
        
        displayText = new Label("", FontLoader.buttonStyle);
        displayText.setText((Difficulty.RoomType == Difficulty.RoomTypes.Dungeon)? "Dungeon" :
                "Forest");
        displayText.setSize(displayText.getWidth(), displayText.getHeight());
        displayText.setOriginX(displayText.getWidth() / 2);
        displayText.setOriginY(displayText.getHeight()/ 2);
        displayText.setAlignment(Align.left);
        displayText.setPosition((lastDisplay.getX() + lastDisplay.getWidth() + 20),50);
        
        Label nextDisplay = new Label(">", FontLoader.buttonStyle);
        nextDisplay.setSize(nextDisplay.getWidth() * Options.aspectRatio, nextDisplay.getHeight() * Options.aspectRatio);
        nextDisplay.setOriginX(nextDisplay.getWidth() / 2);
        nextDisplay.setOriginY(nextDisplay.getHeight()/ 2);
        nextDisplay.setAlignment(Align.left);
        nextDisplay.setPosition((displayText.getX() + displayText.getWidth() + 20), 50);
        nextDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextDungeon();
            }
        
        });
        mainStage.addActor(displayText);
        mainStage.addActor(lastDisplay);
        mainStage.addActor(nextDisplay);
        
        
        
        //------------------------------------------------------------------------------------------------------------------------------
        
        //------------------------------------------------------------------------------------------------------------------------------
        Label lastGMDisplay = new Label("<", FontLoader.buttonStyle);
        lastGMDisplay.setSize(lastDisplay.getWidth() * Options.aspectRatio, lastGMDisplay.getHeight() * Options.aspectRatio);
        lastGMDisplay.setOriginX(lastGMDisplay.getWidth() / 2);
        lastGMDisplay.setOriginY(lastGMDisplay.getHeight()/ 2);
        lastGMDisplay.setAlignment(Align.left);
        lastGMDisplay.setPosition(50,  150);
        lastGMDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousGMMode();
            }
        
        });
        
        displayGMText = new Label("", FontLoader.buttonStyle);
        displayGMText.setText((Multiplayer.GameModeType == Multiplayer.GameModeTypes.Survival)? "Survial" :
                "Crawl");
        displayGMText.setSize(displayGMText.getWidth(), displayGMText.getHeight());
        displayGMText.setOriginX(displayGMText.getWidth() / 2);
        displayGMText.setOriginY(displayGMText.getHeight()/ 2);
        displayGMText.setAlignment(Align.left);
        displayGMText.setPosition((lastGMDisplay.getX() + lastGMDisplay.getWidth() + 20),150);
        
        
        Label nextGMDisplay = new Label(">", FontLoader.buttonStyle);
        nextGMDisplay.setSize(nextGMDisplay.getWidth() * Options.aspectRatio, nextGMDisplay.getHeight() * Options.aspectRatio);
        nextGMDisplay.setOriginX(nextGMDisplay.getWidth() / 2);
        nextGMDisplay.setOriginY(nextGMDisplay.getHeight()/ 2);
        nextGMDisplay.setAlignment(Align.left);
        nextGMDisplay.setPosition((displayText.getX() + displayGMText.getWidth() + 20),  150);
        nextGMDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextGMMode();
            }
        
        });
        
        mainStage.addActor(displayGMText);
        
        mainStage.addActor(lastGMDisplay);
        mainStage.addActor(nextGMDisplay);
        
        
    }
    public void nextDungeon(){
        int d = Difficulty.RoomType.ordinal();
        d++;
        if(d >= Difficulty.RoomTypes.values().length){
            d = 0;
        }
        Difficulty.RoomType = Difficulty.RoomTypes.values()[d];
        displayText.setText((Difficulty.RoomType == Difficulty.RoomTypes.Dungeon)? "Dungeon" :
                "Forest");
        if(Multiplayer.host){
            JSONObject data = new JSONObject();
            try{
                data.put("mapType", d);
                Multiplayer.socket.emit("hostChangedMapType", data);
            }catch(Exception e){

            }
        } 
    }
    public void previousDungeon(){
        int d = Difficulty.RoomType.ordinal();
        d--;
        if(d < 0){
            d = Difficulty.RoomTypes.values().length - 1;
        }
        Difficulty.RoomType = Difficulty.RoomTypes.values()[d];
        displayText.setText((Difficulty.RoomType == Difficulty.RoomTypes.Dungeon)? "Dungeon" :
                "Forest");
        if(Multiplayer.host){
            JSONObject data = new JSONObject();
            try{
                data.put("mapType", d);
                Multiplayer.socket.emit("hostChangedMapType", data);
            }catch(Exception e){

            }
        }       
    }
    public void nextGMMode(){
        int d = Multiplayer.GameModeType.ordinal();
        d++;
        if(d >= Multiplayer.GameModeTypes.values().length){
            d = 0;
        }
        Multiplayer.GameModeType = Multiplayer.GameModeTypes.values()[d];
        displayGMText.setText((Multiplayer.GameModeType == Multiplayer.GameModeTypes.Survival)? "Survial" :
                "Crawl");
        if(Multiplayer.host){
            JSONObject data = new JSONObject();
            try{
                data.put("gameMode", d);
                Multiplayer.socket.emit("hostChangedGameMode", data);
            }catch(Exception e){

            }
        }
    }
    public void previousGMMode(){
        int d = Multiplayer.GameModeType.ordinal();;
        d--;
        if(d < 0){
            d = Multiplayer.GameModeTypes.values().length - 1;
        }
        Multiplayer.GameModeType = Multiplayer.GameModeTypes.values()[d];
        displayGMText.setText((Multiplayer.GameModeType == Multiplayer.GameModeTypes.Survival)? "Survial" :
                "Crawl");
        if(Multiplayer.host){
            JSONObject data = new JSONObject();
            try{
                data.put("gameMode", d);
                Multiplayer.socket.emit("hostChangedGameMode", data);
            }catch(Exception e){

            }
        }
    }
    public void updateSelected(InputEvent event, float x, float y){
        for(int i = 0; i < heroSelections.size(); i++){
            if(heroSelections.get(i) == event.getListenerActor()){
                currentSelection = Hero.getNewHero(Hero.heros.values()[i]);
                HeroSelectionIndex = i;
            }
        }
        BaseGame.setActiveScreen( new HeroSelectionScreen());
    }
	
    public void startGame(){
        LevelScreen ls = new LevelScreen();
        if(MainMenuScreen.musicPlaying){
            MainMenuScreen.backgroundMusic.stop();
            MainMenuScreen.musicPlaying = false;
        }
        if(Multiplayer.GameModeType == Multiplayer.GameModeTypes.Survival){
                ls.setGameMode(new SurvivalGameMode());
            }else if(Multiplayer.GameModeType == Multiplayer.GameModeTypes.Crawl){
                ls.setGameMode(new DungeonCrawlGameMode());
            }
        cleanUpResources();
        BaseGame.setActiveScreen(ls);
    }
    public void cancelGame(){
        cleanUpResources();
        BaseGame.setActiveScreen( new MainMenuScreen());
    }
    
    
    public void update(float dt) {
    }
    
    private void cleanUpResources(){
    }
}
