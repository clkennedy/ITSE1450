/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.screens;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.atkinson.game.engine.BaseScreen;
import com.atkinson.game.engine.Hover;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Font.FontLoader;
import com.dslayer.content.Player.Menu.EscapeMenu;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */
public class multiplayerRoomScreen extends BaseScreen implements Input.TextInputListener {

    private static String playerUserNameText;
    
    private Label playerUserName;
    private boolean changingName = false;
    private boolean creatingRoom = false;
    private boolean successfullRoomCreation = false;
    private boolean joinedRoom = false;
    public static boolean rejoined = false;
    
    private boolean updateUserName;
    
    private float start = 0;
    
    private List<String> rooms;
    private boolean refreshRooms = true;
    Table roomTable;
    private BaseActor scroll;
    
    @Override
    public void initialize() {
        
        rooms = new ArrayList<String>();
        this.playerUserNameText = "default";
        
        if(Multiplayer.socket == null || !Multiplayer.socket.connected()){
            connectSocket();
        }
        else if(Multiplayer.socket.connected()){
            Multiplayer.socket.emit("joinMultiplerAgain");
            Multiplayer.socket.emit("requestRooms");
            this.rejoined = true;
        }
        configSocketEvents();
        
        Label createRoom = new Label("Create Room", FontLoader.buttonStyle);
        createRoom.setFontScale(1f * Options.aspectRatio);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        createRoom.setOriginX(createRoom.getWidth() / 2);
        createRoom.setOriginY(createRoom.getHeight()/ 2);
        createRoom.setPosition((mainStage.getWidth()) - (createRoom.getWidth() + 50 * Options.aspectRatio), (mainStage.getHeight() - 100 * Options.aspectRatio));
        createRoom.setAlignment(Align.center);
        createRoom.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createRoom();
            }
        
        });
        mainStage.addActor(createRoom);
        
        Label mainMenu = new Label("Back", FontLoader.buttonStyle);
        mainMenu.setFontScale(1f * Options.aspectRatio);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        mainMenu.setOriginX(mainMenu.getWidth() / 2);
        mainMenu.setOriginY(mainMenu.getHeight()/ 2);
        mainMenu.setPosition((mainStage.getWidth()) - (createRoom.getWidth() + 30 * Options.aspectRatio), (createRoom.getY() - 30 * Options.aspectRatio));
        mainMenu.setOrigin(mainMenu.getWidth()/2, mainMenu.getHeight()/2);
        mainMenu.setAlignment(Align.center);
        mainMenu.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToMainMenu();
            }
        
        });
        
        mainStage.addActor(mainMenu);
        
        
        Label refreshRoomsButton = new Label("Refresh", FontLoader.buttonStyle);
        refreshRoomsButton.setFontScale(1f * Options.aspectRatio);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        refreshRoomsButton.setOriginX(refreshRoomsButton.getWidth() / 2);
        refreshRoomsButton.setOriginY(refreshRoomsButton.getHeight()/ 2);
        refreshRoomsButton.setPosition((mainStage.getWidth()) - (createRoom.getWidth() + 30 * Options.aspectRatio), (mainMenu.getY() - 200 * Options.aspectRatio));
        refreshRoomsButton.setAlignment(Align.center);
        refreshRoomsButton.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshRooms();
            }
        
        });
        
        mainStage.addActor(refreshRoomsButton);
        
        Label userName = new Label("UserName", FontLoader.buttonStyle);
        userName.setFontScale(1f * Options.aspectRatio);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        userName.setOriginX(userName.getWidth() / 2);
        userName.setOriginY(userName.getHeight()/ 2);
        userName.setPosition((mainStage.getWidth()) - (createRoom.getWidth() + 30 * Options.aspectRatio), (refreshRoomsButton.getY() - 200 * Options.aspectRatio));
        userName.setAlignment(Align.center);
        mainStage.addActor(userName);
        
        Label.LabelStyle usernStyle = new Label.LabelStyle(FontLoader.buttonStyle);
        playerUserName = new Label(playerUserNameText, usernStyle);
        playerUserName.setFontScale(1f * Options.aspectRatio);
        playerUserName.setOriginX(playerUserName.getWidth() / 2);
        playerUserName.setOriginY(playerUserName.getHeight()/ 2);
        playerUserName.setPosition((mainStage.getWidth()) - (createRoom.getWidth() + 30 * Options.aspectRatio), (userName.getY() - 30 * Options.aspectRatio));
        playerUserName.setAlignment(Align.center);
        mainStage.addActor(playerUserName);
        
        Label changeUserName = new Label("Change", FontLoader.buttonStyle);
        changeUserName.setFontScale(1f * Options.aspectRatio);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        changeUserName.setOriginX(changeUserName.getWidth() / 2);
        changeUserName.setOriginY(changeUserName.getHeight()/ 2);
        changeUserName.setPosition((mainStage.getWidth()) - (createRoom.getWidth() + 30 * Options.aspectRatio), (playerUserName.getY() - 30 * Options.aspectRatio));
        changeUserName.setAlignment(Align.center);
        changeUserName.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeUserName();
            }
        
        });
        mainStage.addActor(changeUserName);
        
        //--------------------------------------------------------------------------------------------
        
        Label rooms = new Label("Rooms", FontLoader.buttonStyle);
        rooms.setFontScale(1f * Options.aspectRatio);
        //createRoom.setSize((createRoom.getWidth() * 1.2f) * Options.aspectRatio, (createRoom.getHeight() *1.2f) * Options.aspectRatio);
        rooms.setOriginX(createRoom.getWidth() / 2);
        rooms.setOriginY(createRoom.getHeight()/ 2);
        rooms.setPosition(rooms.getWidth(), (mainStage.getHeight() - 50 * Options.aspectRatio));
        rooms.setAlignment(Align.center);
        mainStage.addActor(rooms);
        
        scroll = new BaseActor();
        scroll.loadAnimationFromFiles(EscapeMenu.scroll, .05f, false);
        scroll.setSize(500 * Options.aspectRatio, 500 * Options.aspectRatio);
        scroll.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
        scroll.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50 * Options.aspectRatio);
        
        mainStage.addActor(scroll);
        
    }

    public void createRoom(){
        if(!creatingRoom){
            Gdx.input.getTextInput(this, "Create Room", playerUserName.getText().toString() + "'s Room", "");
            creatingRoom = true;
        }
    }
    public void changeUserName(){
        if(!changingName){
            Gdx.input.getTextInput(this, "Change Username", playerUserName.getText().toString(), "");
            changingName = true;
        }
        
    }
    public void refreshRooms(){
        Multiplayer.socket.emit("requestRooms");
    }
    private void connectToRoom(InputEvent event, float x, float y){
        for(String room : rooms){
            String roomName = room.substring(0, room.length() - 4);
            if(room.equals(((Label)event.getListenerActor()).getText().toString())){
                JSONObject obj = new JSONObject();
                try{
                    obj.put("roomName", roomName);
                    Multiplayer.socket.emit("joinRoom", obj);
                }catch(Exception e){
                    System.out.println("com.dslayer.content.screens.multiplayerRoomScreen.input() 312: " + e.getMessage());
                }
            }
        }
    }
    public void backToMainMenu(){
            Multiplayer.socket.close();
            Multiplayer.socket = null;
            BaseGame.setActiveScreen(new MainMenuScreen());
    }
    
    @Override
    public void update(float dt) {
        
        if(start == 0){
            System.out.println("started");
        }
        start += dt;
        if(!Multiplayer.connected){
            if(Multiplayer.socket != null)
                Multiplayer.socket.close();
            Multiplayer.socket = null;
            BaseGame.setActiveScreen(new MainMenuScreen());
        }
        if(rejoined){
            rejoined = false;
            if(Multiplayer.socket == null){
                connectSocket();
            }
            //Multiplayer.lobbyScreen = new MutliplayerLobbyScreen();
            Multiplayer.socket.emit("joinMultiplerAgain");
            Multiplayer.socket.emit("requestRooms");
        }
        
        if(updateUserName){
            System.out.println(playerUserNameText);
           playerUserName.setText(playerUserNameText);
           updateUserName= false;
        }
        
        if(refreshRooms){
            scroll.replayAnimation();
            //scroll.setSize(500 * Options.aspectRatio, 500 * Options.aspectRatio);
            //scroll.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
            //scroll.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50);
            if(roomTable != null){
                roomTable.remove();
            }
            roomTable = new Table();
            //System.out.println("Rooms Refreshed");
            Label.LabelStyle roomStyle = new Label.LabelStyle(FontLoader.buttonStyle);
            Vector2 vet = new Vector2(30, mainStage.getHeight() - 100);
            for(String room: rooms){
                Label roomName = new Label(room, roomStyle);
                roomName.setOriginX(roomName.getWidth() / 2);
                roomName.setOriginY(roomName.getHeight()/ 2);
                //roomName.setPosition(vet.x, vet.y);
                roomName.setAlignment(Align.center);
                roomName.addListener(new Hover(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        connectToRoom(event, x, y);
                    }
                });
                //vet.y -= roomName.getHeight();
                
                roomTable.add(roomName);
                roomTable.row();
                roomTable.setPosition(0, 0);
            }
            ScrollPane sp = new ScrollPane(roomTable);
            sp.setSize(500, 500);
            sp.setOrigin(scroll.getWidth()/2, scroll.getHeight() / 2);
            sp.setPosition(15, mainStage.getHeight() - scroll.getHeight() - 50);
            mainStage.addActor(sp);
            refreshRooms = false;
            
        }
        
        if(successfullRoomCreation){
            successfullRoomCreation = false;
            Multiplayer.host = true;
            BaseGame.setActiveScreen(new MutliplayerLobbyScreen());
        }
        if(joinedRoom){
            joinedRoom = false;
            BaseGame.setActiveScreen(new MutliplayerLobbyScreen());
        }
    }
    
    private void connectSocket(){
        try{
            //External: 75.81.145.66
            //internal: 192.168.1.46
            //ec2-18-191-138-40.us-east-2.compute.amazonaws.com
            //Multiplayer.socket = IO.socket("http://75.81.145.66:8080");
            Multiplayer.socket = IO.socket("http://18.191.138.40:3000");
            Multiplayer.socket.connect();
            //Multiplayer.socket.emit("Connected", null);
            //System.out.println("Boo");
            Multiplayer.connected = true;
        }catch(Exception e){
            System.out.println("Failed Connecting");
        }
    }
    private void configSocketEvents() {
        Multiplayer.socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Multiplayer.connected = false;
                System.out.println("Connect Error");
                }
        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Multiplayer.connected = false;
                System.out.println("Timeout");
                }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                Multiplayer.connected = false;
                System.out.println("Disconnected");
                }
        }).on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... os) {
                Gdx.app.log("SocketIO", "Connected");
                Multiplayer.socket.emit("requestRooms");
                Multiplayer.connected = true;
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "My Id: " + id);
                    Multiplayer.myID = id;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "new Player Connected: " + id);
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting New Player ID");
                }
            }
        }).on("requestRooms", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                try{
                    //String id = data.getString("id");
                    Gdx.app.log("SocketIO", "RequestedRooms");
                    refreshRooms = true;
                    JSONArray array = (JSONArray) os[0];
                    rooms.clear();
                    for(int i = 0; i< array.length(); i++){
                        int players = array.getJSONObject(i).getInt("numOfPlayer");
                        int maxP = array.getJSONObject(i).getInt("maxPlayer");
                        if(players < maxP){
                            rooms.add(array.getJSONObject(i).getString("name") + " " + players + "/" + maxP);   
                        }
                    }
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("createRoom", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                try{
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("failedRoomCreation", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                try{
                    Gdx.app.log("SocketIO", " Failed Room Creation");
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "");
                }
            }
        }).on("successfulRoomCreation", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                try{
                    successfullRoomCreation = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("pushDefaultUserName", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    updateUserName = true;
                    String userName = data.getString("userName");
                    if(Multiplayer.myUserName == ""){
                        Multiplayer.myUserName = userName;
                    }else{
                        userName = Multiplayer.myUserName;
                        JSONObject obj = new JSONObject();
                        try{
                            obj.put("userName", userName);
                            Multiplayer.socket.emit("updateUserName", obj);
                        }catch(Exception e){
                            System.out.println(" " + e.getMessage());
                        }
                    }
                    if(playerUserName != null){
                        Vector2 olPos = new Vector2(playerUserName.getX(), playerUserName.getY());
                        playerUserName.remove();
                        playerUserNameText = userName;
                        playerUserName = new Label(playerUserNameText, FontLoader.buttonStyle);
                        //createRoom.setFontScale(.5f);
                        playerUserName.setOriginX(playerUserName.getWidth() / 2);
                        playerUserName.setOriginY(playerUserName.getHeight()/ 2);
                        playerUserName.setPosition(olPos.x, olPos.y);
                        playerUserName.setAlignment(Align.center);
                        mainStage.addActor(playerUserName);
                    }else{
                        playerUserNameText = userName;
                    }
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("joinRoom", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                JSONObject data = (JSONObject) os[0];
                try{
                    String userName = data.getString("userName");
                    if(playerUserName != null){
                        playerUserName.setText(userName);
                    }else{
                        playerUserNameText = userName;
                    }
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error getting Rooms");
                }
            }
        }).on("successfulRoomJoin", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                try{
                    joinedRoom = true;
                }catch(Exception e){
                    Gdx.app.log("SocketIO", "Error in successfulRoomJoin");
                }
            }
        });
    }

    @Override
    public void input(String text) {
        if(changingName){
            playerUserName.setText(text);
            JSONObject obj = new JSONObject();
            try{
                obj.put("userName", text);
                Multiplayer.socket.emit("updateUserName", obj);
                Multiplayer.myUserName = text;
            }catch(Exception e){
                System.out.println("com.dslayer.content.screens.multiplayerRoomScreen.input() 312: " + e.getMessage());
            }
        }
        
        if(creatingRoom){
            JSONObject obj = new JSONObject();
            try{
                obj.put("roomName", text);
                Multiplayer.socket.emit("createRoom", obj);
            }catch(Exception e){
                System.out.println("com.dslayer.content.screens.multiplayerRoomScreen.input() 312: " + e.getMessage());
            }
        }
        creatingRoom = false;
        changingName = false;
    }

    @Override
    public void canceled() {
        creatingRoom = false;
        changingName =false;
    }
    
    public static Socket getSocket(){
        return Multiplayer.socket;
    }
}
