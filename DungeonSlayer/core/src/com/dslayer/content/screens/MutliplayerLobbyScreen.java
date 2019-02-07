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
import com.dslayer.content.Rooms.DungeonPanels;
import com.dslayer.content.Rooms.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import java.awt.Desktop.Action;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import sun.font.TrueTypeFont;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Player.Menu.EscapeMenu;
import com.dslayer.content.Player.Player;
import com.dslayer.gamemodes.SurvivalGameMode;
import io.socket.client.IO;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<Label> playerNames;
    
    private boolean roomDestroyed = false;
    private boolean redrawPlayers = false;
    private boolean redrawReady = false;
    
    private BaseActor scroll;
    BaseActor checkmark;
    private Table roomTable;
    public void initialize()
    {
       checkmark = new BaseActor();
       checkmark.setAnimation(Avatars.load("check.png"));
       checkmark.setSize(40,40);
       checkmark.setOrigin(checkmark.getWidth() / 2, checkmark.getHeight() / 2);
       
       
       players = new HashMap<String, String>();
       playersReady = new HashMap<String, Boolean>();
       playerNames = new ArrayList();
       playersChecks = new HashMap<String, BaseActor>();
       
       Multiplayer.socket = multiplayerRoomScreen.getSocket();
       configSocket();
       Label mainMenu = new Label("Main Menu", MainMenuScreen.buttonStyle);
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
        
        Label back = new Label("Back", MainMenuScreen.buttonStyle);
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
        
        Label ready = new Label("Ready", MainMenuScreen.buttonStyle);
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
        
        Label rooms = new Label("Players", MainMenuScreen.buttonStyle);
        //createRoom.setFontScale(.5f);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        rooms.setOriginX(rooms.getWidth() / 2);
        rooms.setOriginY(rooms.getHeight()/ 2);
        rooms.setPosition(rooms.getWidth(), (mainStage.getHeight() - 50));
        rooms.setAlignment(Align.center);
        mainStage.addActor(rooms);
        
        scroll = new BaseActor();
        scroll.setAnimation(Avatars.load(EscapeMenu.scroll, .1f, false));
        scroll.setSize(500, 500);
        scroll.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
        scroll.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50);
        
        mainStage.addActor(scroll);
    }

    @Override
    public void update(float dt) {
        if(!Multiplayer.connected){
            Multiplayer.socket.close();
            Multiplayer.socket = null;
            BaseGame.setActiveScreen(new MainMenuScreen());
        }
        
        if(roomDestroyed){
            roomDestroyed = false;
            multiplayerRoomScreen.rejoined = true;
            BaseGame.setActiveScreen(Multiplayer.baseScreen);
        }
        
        if(redrawPlayers){
            playerNames.clear();
            scroll.setAnimationWithReset(Avatars.load(EscapeMenu.scroll, .1f, false));
            scroll.setSize(500, 500);
            scroll.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
            scroll.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50);
            if(roomTable != null){
                roomTable.remove();
            }
            roomTable = new Table();
            //System.out.println("Rooms Refreshed");
            MainMenuScreen.buttonStyle.fontColor = Color.WHITE;
            Label.LabelStyle roomStyle = new Label.LabelStyle(MainMenuScreen.buttonStyle);
            MainMenuScreen.buttonStyle.fontColor = Color.BROWN;
            Vector2 vet = new Vector2(30, mainStage.getHeight() - 100);
            for(String player: players.keySet()){
                Label roomName = new Label(players.get(player).toString(), roomStyle);
                roomName.setOriginX(roomName.getWidth() / 2);
                roomName.setOriginY(roomName.getHeight()/ 2);
                //roomName.setPosition(vet.x, vet.y);
                roomName.setAlignment(Align.center);
                
                //vet.y -= roomName.getHeight();
                playerNames.add(roomName);
                roomTable.add(roomName);
                roomTable.row();
                roomTable.setPosition(0, 0);
            }
            ScrollPane sp = new ScrollPane(roomTable);
            sp.setSize(500, 500);
            sp.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
            sp.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50);
            mainStage.addActor(sp);
            redrawPlayers = false;
        }
        
        if(redrawReady){
            System.out.println("Redraw Ready Marks");
            for(String id : playersReady.keySet()){
                if(playersReady.get(id)){
                    if(playersChecks.get(id) != null)
                        playersChecks.get(id).remove();
                    System.out.println(playerNames.indexOf(players.get(id).toString()));
                    Label player = playerNames.get(playerNames.indexOf(players.get(id)));
                    checkmark = new BaseActor();
                    checkmark.setAnimation(Avatars.load("check.png"));
                    checkmark.setSize(40,40);
                    checkmark.setOrigin(checkmark.getWidth() / 2, checkmark.getHeight() / 2);
                    checkmark.setPosition(player.getX() + player.getWidth(), player.getY());
                    mainStage.addActor(checkmark);
                    playersChecks.put(id, checkmark);
                }else{
                    if(playersChecks.get(id) != null)
                        playersChecks.get(id).remove();
                }
            }
        }
    }
    
    public void backToMainMenu(){
            Multiplayer.socket.close();
            Multiplayer.socket = null;
            BaseGame.setActiveScreen(new MainMenuScreen());
    }
    
    public void backToRoomSelection(){
            multiplayerRoomScreen.rejoined = true;
            Multiplayer.socket.emit("leaveRoom");
            BaseGame.setActiveScreen(Multiplayer.baseScreen);
    }
    
    public void readyUp(){
            Multiplayer.socket.emit("flagReady");
    }
    
public void configSocket(){
        
    Multiplayer.socket.on("newPlayerJoinedRoom", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    //System.out.println("yeeHaw");
                    players.put(data.getString("id"), data.getString("userName"));
                    playersReady.put(data.getString("id"),false);
                    playersChecks.put(data.getString("id"), null);
                    redrawPlayers = true;
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
                       players.put(data.getString("id"), data.getString("userName")); 
                       playersReady.put(data.getString("id"),data.getBoolean("ready"));
                       playersChecks.put(data.getString("id"), null);
                    }
                    redrawPlayers = true;
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
                    playersChecks.remove(id);
                    redrawPlayers = true;
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
                    redrawReady = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("playerFlaggedUnReady", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id").toString();
                    playersReady.put(id,false);
                    redrawReady = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        });
    }
    
}