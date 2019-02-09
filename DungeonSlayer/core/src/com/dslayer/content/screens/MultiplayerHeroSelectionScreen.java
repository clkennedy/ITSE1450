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
import com.dslayer.content.Hero.Hero;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.options.Avatar;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import com.dslayer.content.options.Unlocks;
import static com.dslayer.content.options.Unlocks.currentAvatar;
import static com.dslayer.content.screens.HeroSelectionScreen.HeroSelectionIndex;
import static com.dslayer.content.screens.HeroSelectionScreen.currentSelection;
import com.dslayer.gamemodes.GameMode;
import com.dslayer.gamemodes.SurvivalGameMode;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;

public class MultiplayerHeroSelectionScreen extends BaseScreen {
    
    BaseActor gameOverMessage;
    
    public static Hero hero;
    Music backgroundMusic;
    
    private boolean returnToMainScreen = false;
    
    public static Hero currentSelection = Hero.getNewHero(Hero.heros.ClassicHero);
    public static int HeroSelectionIndex = 0;
    private BaseActor selectionBox;

    public ArrayList<BaseActor> heroSelections;
    
    public void initialize() {
        
        BaseActor.setMainStage(mainStage);
        BaseActor.setUIStage(uiStage);
        heroSelections = new ArrayList<BaseActor>();
        //private Player player;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("HumbleFonts/compass/CompassPro.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 0f;
        BitmapFont fontTitle = generator.generateFont(parameter); // font size 12 pixels
        
        parameter.size = 20;
        BitmapFont fontnames = generator.generateFont(parameter);
        
        parameter.size = 60;
        BitmapFont fontMenus = generator.generateFont(parameter);
        generator.dispose();
        
        Label.LabelStyle title = new Label.LabelStyle(fontTitle, Color.BROWN);
        Label.LabelStyle names = new Label.LabelStyle(fontnames, Color.BROWN);
        Label.LabelStyle menu = new Label.LabelStyle(fontMenus, Color.BROWN);
        
        Label l = new Label("Select Hero", title);
        l.setPosition((mainStage.getWidth()/ 2) - (l.getWidth()/2), mainStage.getHeight() - 100);
        mainStage.addActor(l);
        
        float w = 0;
        
        for(Hero.heros bActor : Hero.heros .values()){
            Hero b = Hero.getNewHero(bActor);
            b.setAnimation(b.playRight());
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
            Label name = new Label(h.getName(), names);
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
        
        Label ready = new Label("Ready", menu);
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
        
        Label cancel = new Label("Cancel", menu);
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
    }
    
    public void updateSelected(InputEvent event, float x, float y){
        for(int i = 0; i < heroSelections.size(); i++){
            if(heroSelections.get(i) == event.getListenerActor()){
                currentSelection = Hero.getNewHero(Hero.heros.values()[i]);
                HeroSelectionIndex = i;
            }
        }
        BaseGame.setActiveScreen(new MultiplayerHeroSelectionScreen());
    }
	
    public void startGame(){
        JSONObject data = new JSONObject();
        try{
            data.put("hero", HeroSelectionIndex);
            Multiplayer.socket.emit("updateHero", data);
        }catch(Exception e){
            
        }
        
        BaseGame.setActiveScreen( Multiplayer.lobbyScreen);
    }
    public void cancelGame(){
        BaseGame.setActiveScreen( Multiplayer.lobbyScreen);
    }
    
    
    public void update(float dt) {
    }
}
