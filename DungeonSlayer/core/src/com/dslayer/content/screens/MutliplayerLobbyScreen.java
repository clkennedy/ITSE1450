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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import com.dslayer.content.Hero.Hero;
import com.dslayer.content.Player.Menu.EscapeMenu;
import com.dslayer.content.Player.Player;
import static com.dslayer.content.screens.MultiplayerHeroSelectionScreen.HeroSelectionIndex;
import com.dslayer.gamemodes.MultiplayerSurvivalGameMode;
import com.dslayer.gamemodes.SurvivalGameMode;
import io.socket.client.IO;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author douglas.atkinson
 */
public class MutliplayerLobbyScreen extends BaseScreen {
    
    private HashMap<String, String> players; 
    private HashMap<String, Boolean> playersReady; 
    private HashMap<String, BaseActor> playersChecks; 
    private HashMap<String, BaseActor> heroAvatars; 
    private HashMap<String, Integer> playerHeros;
    private HashMap<String, Label> playerNames;
    
    public static boolean reload = false;
    
    public static boolean roomDestroyed = false;
    private boolean redraw = false;
    private boolean redrawHeros = false;
    
    private boolean isReady = false;
    
    private BaseActor scroll;
    BaseActor checkmark;
    BaseActor hero;
    private Table roomTable;
    
    Label ready;
    Label countDown;
    Label displayText;
    Label displayGMText;
    
    private float startTime = 5f;
    private float startTimeTimer = 0;
    
    public void initialize()
    {
       checkmark = new BaseActor();
       checkmark.loadTexture("check.png");
       checkmark.setSize(40,40);
       checkmark.setOrigin(checkmark.getWidth() / 2, checkmark.getHeight() / 2);
       
       hero = Hero.getNewHero(Hero.heros.ClassicHero);
       hero.setAnimation(((Hero)hero).playRight());
       hero.setSize(30, 30);
       hero.setOrigin(hero.getWidth() / 2, hero.getHeight() / 2);
       
       countDown = new Label(Integer.toString( (int)startTime), FontLoader.titleStyle);
       countDown.setFontScale(4);
       countDown.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()/ 2);
       countDown.setVisible(false);
       countDown.setZIndex(3000);
       uiStage.addActor(countDown);
       
       players = new HashMap<String, String>();
       playersReady = new HashMap<String, Boolean>();
       playerNames = new HashMap<String, Label>();
       playersChecks = new HashMap<String, BaseActor>();
       playerHeros = new HashMap<String, Integer>();
       heroAvatars = new HashMap<String, BaseActor>();
       
       players.put(Multiplayer.myID,Multiplayer.myUserName);
       playersReady.put(Multiplayer.myID,false);
       Label playerName = new Label(players.get(Multiplayer.myID).toString(), FontLoader.buttonStyle);
                playerName.setOriginX(playerName.getWidth() / 2);
                playerName.setOriginY(playerName.getHeight()/ 2);
                //roomName.setPosition(vet.x, vet.y);
                playerName.setAlignment(Align.center);
       playerNames.put(Multiplayer.myID, playerName);
       playersChecks.put(Multiplayer.myID, null);
       heroAvatars.put(Multiplayer.myID, null);
       playerHeros.put(Multiplayer.myID, MultiplayerHeroSelectionScreen.HeroSelectionIndex);
       redraw = true;
       redrawHeros = true;
       reload = true;
       
       Multiplayer.socket = multiplayerRoomScreen.getSocket();
       configSocket();
       Label mainMenu = new Label("Main Menu", FontLoader.buttonStyle);
        //createRoom.setFontScale(.5f);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        mainMenu.setOriginX(mainMenu.getWidth() / 2);
        mainMenu.setOriginY(mainMenu.getHeight()/ 2);
        mainMenu.setPosition((mainStage.getWidth()) - (mainMenu.getWidth() + 10), (30));
        mainMenu.setOrigin(mainMenu.getWidth()/2, mainMenu.getHeight()/2);
        mainMenu.setAlignment(Align.center);
        mainMenu.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToMainMenu();
            }
        
        });
        mainStage.addActor(mainMenu);
        
        Label back = new Label("Back", FontLoader.buttonStyle);
        //createRoom.setFontScale(.5f);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        back.setOriginX(back.getWidth() / 2);
        back.setOriginY(back.getHeight()/ 2);
        back.setPosition((mainStage.getWidth()) - (back.getWidth() + 10), mainMenu.getY() + mainMenu.getHeight());
        back.setOrigin(back.getWidth()/2, back.getHeight()/2);
        back.setAlignment(Align.center);
        back.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToRoomSelection();
            }
        
        });
        mainStage.addActor(back);
        
        ready = new Label("Ready", FontLoader.buttonStyle);
        //createRoom.setFontScale(.5f);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        ready.setOriginX(ready.getWidth() / 2);
        ready.setOriginY(ready.getHeight()/ 2);
        ready.setPosition((mainStage.getWidth()) - (ready.getWidth() + 10), back.getY() + back.getHeight() + 100);
        ready.setAlignment(Align.center);
        ready.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                readyUp();
            }
        
        });
        mainStage.addActor(ready);
        
        Label HeroSelect = new Label("Hero Selection", FontLoader.buttonStyle);
        //createRoom.setFontScale(.5f);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        HeroSelect.setOriginX(HeroSelect.getWidth() / 2);
        HeroSelect.setOriginY(HeroSelect.getHeight()/ 2);
        HeroSelect.setPosition((mainStage.getWidth()) - (HeroSelect.getWidth() + 10), ready.getY() + ready.getHeight() + 100);
        HeroSelect.setAlignment(Align.center);
        HeroSelect.addListener(new Hover(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HeroSelection();
            }
        });
        mainStage.addActor(HeroSelect);
        
        Label rooms = new Label("Players", FontLoader.buttonStyle);
        //createRoom.setFontScale(.5f);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        rooms.setOriginX(rooms.getWidth() / 2);
        rooms.setOriginY(rooms.getHeight()/ 2);
        rooms.setPosition(rooms.getWidth(), (mainStage.getHeight() - 50));
        rooms.setAlignment(Align.center);
        mainStage.addActor(rooms);
        
        scroll = new BaseActor();
        scroll.loadAnimationFromFiles(EscapeMenu.scroll, .1f, false);
        scroll.setSize(500, 500);
        scroll.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
        scroll.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50);
        
        mainStage.addActor(scroll);
        //------------------------------------------------------------------------------------------------------------------------------
        Label nextDisplay = new Label(">", FontLoader.buttonStyle);
        nextDisplay.setSize(nextDisplay.getWidth() * Options.aspectRatio, nextDisplay.getHeight() * Options.aspectRatio);
        nextDisplay.setOriginX(nextDisplay.getWidth() / 2);
        nextDisplay.setOriginY(nextDisplay.getHeight()/ 2);
        nextDisplay.setAlignment(Align.right);
        nextDisplay.setPosition((Gdx.graphics.getWidth() - nextDisplay.getWidth() - 20), Gdx.graphics.getHeight() - 50);
        nextDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextDungeon();
            }
        
        });
        displayText = new Label("", FontLoader.buttonStyle);
        displayText.setText((Difficulty.RoomType == Difficulty.RoomTypes.Dungeon)? "Dungeon" :
                "Forest");
        displayText.setSize(displayText.getWidth(), displayText.getHeight());
        displayText.setOriginX(displayText.getWidth() / 2);
        displayText.setOriginY(displayText.getHeight()/ 2);
        displayText.setAlignment(Align.right);
        displayText.setPosition((nextDisplay.getX() - displayText.getWidth() - 20),Gdx.graphics.getHeight() - 50);
        
        
        
        
        
        Label lastDisplay = new Label("<", FontLoader.buttonStyle);
        lastDisplay.setSize(lastDisplay.getWidth() * Options.aspectRatio, lastDisplay.getHeight() * Options.aspectRatio);
        lastDisplay.setOriginX(lastDisplay.getWidth() / 2);
        lastDisplay.setOriginY(lastDisplay.getHeight()/ 2);
        lastDisplay.setAlignment(Align.left);
        lastDisplay.setPosition((displayText.getX() - lastDisplay.getWidth() - 20),  Gdx.graphics.getHeight() - 50);
        lastDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousDungeon();
            }
        
        });
        mainStage.addActor(displayText);
        if(Multiplayer.host){
            mainStage.addActor(lastDisplay);
            mainStage.addActor(nextDisplay);
        }
        
        
        //------------------------------------------------------------------------------------------------------------------------------
        
        //------------------------------------------------------------------------------------------------------------------------------
        Label nextGMDisplay = new Label(">", FontLoader.buttonStyle);
        nextGMDisplay.setSize(nextGMDisplay.getWidth() * Options.aspectRatio, nextGMDisplay.getHeight() * Options.aspectRatio);
        nextGMDisplay.setOriginX(nextGMDisplay.getWidth() / 2);
        nextGMDisplay.setOriginY(nextGMDisplay.getHeight()/ 2);
        nextGMDisplay.setAlignment(Align.right);
        nextGMDisplay.setPosition((Gdx.graphics.getWidth() - nextGMDisplay.getWidth() - 20), Gdx.graphics.getHeight() - 150);
        nextGMDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextGMMode();
            }
        
        });
        displayGMText = new Label("", FontLoader.buttonStyle);
        displayGMText.setText((Multiplayer.GameModeType == Multiplayer.GameModeTypes.Survival)? "Survial" :
                "Crawl");
        displayGMText.setSize(displayGMText.getWidth(), displayGMText.getHeight());
        displayGMText.setOriginX(displayGMText.getWidth() / 2);
        displayGMText.setOriginY(displayGMText.getHeight()/ 2);
        displayGMText.setAlignment(Align.right);
        displayGMText.setPosition((nextDisplay.getX() - displayGMText.getWidth() - 20),Gdx.graphics.getHeight() - 150);
        
        
        
        
        
        Label lastGMDisplay = new Label("<", FontLoader.buttonStyle);
        lastGMDisplay.setSize(lastDisplay.getWidth() * Options.aspectRatio, lastGMDisplay.getHeight() * Options.aspectRatio);
        lastGMDisplay.setOriginX(lastGMDisplay.getWidth() / 2);
        lastGMDisplay.setOriginY(lastGMDisplay.getHeight()/ 2);
        lastGMDisplay.setAlignment(Align.left);
        lastGMDisplay.setPosition((displayText.getX() - lastGMDisplay.getWidth() - 20),  Gdx.graphics.getHeight() - 150);
        lastGMDisplay.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousGMMode();
            }
        
        });
        mainStage.addActor(displayGMText);
        if(Multiplayer.host){
            mainStage.addActor(lastGMDisplay);
            mainStage.addActor(nextGMDisplay);
        }
        
        
        //------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    public void update(float dt) {
        if(!Multiplayer.connected && Multiplayer.socket != null){
            Multiplayer.socket.close();
            Multiplayer.socket = null;
            BaseGame.setActiveScreen(new MainMenuScreen());
        }
        
        if(roomDestroyed){
            roomDestroyed = false;
            multiplayerRoomScreen.rejoined = true;
            BaseGame.setActiveScreen(Multiplayer.baseScreen);
        }
        
        //redraw names
        if(redraw){
            playerNames.clear();
            scroll.replayAnimation();
            if(roomTable != null){
                roomTable.remove();
            }
            roomTable = new Table();
            Vector2 vet = new Vector2(30, mainStage.getHeight() - 100);
            for(String player: players.keySet()){
                Label playerName = new Label(players.get(player).toString(), FontLoader.buttonStyle);
                playerName.setOriginX(playerName.getWidth() / 2);
                playerName.setOriginY(playerName.getHeight()/ 2);
                //roomName.setPosition(vet.x, vet.y);
                playerName.setAlignment(Align.center);
                
                //redraw checkmarks ---------------------------------------------
                playerNames.put(player, playerName);
                if(playersChecks.get(player) != null){
                       checkmark = playersChecks.get(player);
                }else{
                    checkmark = new BaseActor();
                    checkmark.loadTexture("check.png");
                    checkmark.setSize(40,40);
                    checkmark.setOrigin(checkmark.getWidth() / 2, checkmark.getHeight() / 2);
                }
                if(playersReady.get(player)){
                    checkmark.setVisible(true);
                }else{
                    checkmark.setVisible(false);
                }
                
                playersChecks.put(player, checkmark);
                if(heroAvatars.get(player) != null)
                        heroAvatars.get(player).remove();
                hero = Hero.getNewHero(Hero.heros.values()[playerHeros.get(player)]);
                BaseActor b = new BaseActor();
                b.setAnimationWithReset(((Hero)hero).playRight());
                b.setSize(30, 30);
                b.setOrigin(hero.getWidth() /2, hero.getHeight() / 2);
                heroAvatars.put(player, b);
                //mainStage.addActor(hero);
                //uiStage.addActor(hero);
                
                roomTable.add(checkmark);
                roomTable.add(playerName);
                roomTable.add(b);
                
                roomTable.row();
                roomTable.setPosition(0, 0);
            }
            ScrollPane sp = new ScrollPane(roomTable);
            sp.setSize(500, 500);
            sp.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
            sp.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50);
            mainStage.addActor(sp);
            redraw = false;
            
        }
        
        boolean gamelobbyready = true;
        for(boolean b : playersReady.values()){
            gamelobbyready = gamelobbyready && b;
        }
        if(gamelobbyready){
            countDown.setVisible(true);
            
            countDown.setText(Integer.toString((int)startTime - (int)startTimeTimer));
            startTimeTimer += dt;
        }
        else{
            countDown.setVisible(false);
            startTimeTimer = 0;
        }
        
        if(startTimeTimer > startTime){
            //start game
            if(Multiplayer.host){
                Multiplayer.socket.emit("startGame");
            }
            Multiplayer.levelScreen = new LevelScreen();
            if(Multiplayer.GameModeType == Multiplayer.GameModeTypes.Survival){
                Multiplayer.levelScreen.setGameMode(new MultiplayerSurvivalGameMode());
            }else if(Multiplayer.GameModeType == Multiplayer.GameModeTypes.Crawl){
                Multiplayer.levelScreen.setGameMode(new MultiplayerSurvivalGameMode());
            }
            
            BaseGame.setActiveScreen(Multiplayer.levelScreen);
        }
        if(reload){
            reload = false;
            Multiplayer.socket.emit("getRoomPlayers");
            redraw = true;
        }
    }
    
    public void backToMainMenu(){
        if(Multiplayer.connected && Multiplayer.socket != null){
            Multiplayer.socket.close();
            Multiplayer.socket = null;
        }
        BaseGame.setActiveScreen(new MainMenuScreen());
    }
    
    public void backToRoomSelection(){
        if(Multiplayer.connected && Multiplayer.socket != null){
            multiplayerRoomScreen.rejoined = true;
            Multiplayer.socket.emit("leaveRoom");
            Multiplayer.host = false;
        }
            BaseGame.setActiveScreen(new multiplayerRoomScreen());
    }
    
    public void readyUp(){
        if(isReady){
            isReady = false;
            Multiplayer.socket.emit("flagUnReady");
            ready.setText("Ready");
        }else{
            isReady = true;
            Multiplayer.socket.emit("flagReady");
            ready.setText("UnReady");
        }
            
    }
    
    public void HeroSelection(){
        BaseGame.setActiveScreen(new MultiplayerHeroSelectionScreen());
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
                "Forest");;
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
public void configSocket(){
        
    Multiplayer.socket.on("newPlayerJoinedRoom", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id");
                    players.put(id, data.getString("userName"));
                    playersReady.put(id,false);
                    playersChecks.put(id, null);
                    heroAvatars.put(id, null);
                    playerHeros.put(id, data.getInt("hero"));
                    if(isReady){
                        readyUp();
                    }
                    redraw = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms" + e.getMessage());
                }
            }
        }).on("getRoomPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONArray array = (JSONArray) os[0];
                try{
                    for(int i = 0; i < array.length(); i++){
                       JSONObject data = array.getJSONObject(i);
                       String id = data.getString("id");
                       players.put(id, data.getString("userName")); 
                       playersReady.put(id,data.getBoolean("ready"));
                       playersChecks.put(id, null);
                       heroAvatars.put(id, null);
                        playerHeros.put(data.getString("id"), data.getInt("hero"));
                    }
                    redraw = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms" + e.getMessage());
                }
            }
        }).on("roomDestroyed", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    Multiplayer.socket.emit("roomDestroyed", data);
                    roomDestroyed = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("playerDisconnectedFromRoom", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id").toString();
                    players.remove(id);
                    playersReady.remove(id);
                    for(BaseActor b : playersChecks.values()){
                        if( b != null)
                        b.remove();
                    }
                    for(BaseActor b : heroAvatars.values()){
                        if( b != null)
                        b.remove();
                    }
                    playersChecks.remove(id);
                    heroAvatars.remove(id);
                    if(isReady){
                        readyUp();
                    }
                    redraw = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("playerFlaggedReady", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id").toString();
                    playersReady.put(id,true);
                    redraw = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("hostChangedGameMode", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    int id = data.getInt("gameMode");
                    Multiplayer.GameModeType = Multiplayer.GameModeTypes.values()[id];
                    displayGMText.setText((Multiplayer.GameModeType == Multiplayer.GameModeTypes.Survival)? "Survial" :
                        "Crawl");
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "hostChangedGameMode");
                }
            }
        }).on("hostChangedMapType", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    int id = data.getInt("mapType");
                    Difficulty.RoomType = Difficulty.RoomTypes.values()[id];
                    displayText.setText((Difficulty.RoomType == Difficulty.RoomTypes.Dungeon)? "Dungeon" :
                        "Forest");
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "hostChangedMapType");
                }
            }
        }).on("playerFlaggedUnReady", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id").toString();
                    playersReady.put(id,false);
                    redraw = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("playerFlaggedReady", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    System.out.println("player flagged ready");
                    String id = data.getString("id").toString();
                    playersReady.put(id,true);
                    redraw = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("updateHero", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id").toString();
                    //System.out.println("hero Changes");
                    playersReady.put(id,false);
                    playerHeros.put(id, data.getInt("hero"));
                    //System.out.println(data.getInt("hero"));
                    redraw = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        });
    }
    
}