/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.gamemodes;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.GameMessage.GameMessage;
import com.dslayer.content.Objects.Potions.HealthPotion2;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.DungeonPanels;
import com.dslayer.content.Rooms.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import io.socket.emitter.Emitter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */
public class MultiplayerSurvivalGameMode extends GameMode{
    
    private List<BaseActor> gameObjects;
    
    private float potionRespawnInterval = 3f;
    private float potionRespawnTimer = 0;
    private float maxPotionsOnFeild = 6;
    
    private float maxNumOfEnemies = 6;
    private float spawnedEnemies = 0;
    private float spawnTimer = 3f;
    private float spawnTime = 0;
    
    private float GolemSpawnTimer = 120f;
    private float GolemSpawnTime = 0;
    
    private float increaseEnemyTimer = 10f;
    private float increaseEnemyTime = 0f;
    
    private boolean goSent = false;
    
    private GameMessage gm;
    
    public MultiplayerSurvivalGameMode(Stage s){
        super(s);
        //System.out.println(Gdx.graphics.getHeight());
    }
    
    public MultiplayerSurvivalGameMode(){
        this(BaseActor.getMainStage());
        gameObjects= new ArrayList<BaseActor>();
        setupSocketListeners();
    }
    @Override
    public void setup() {
        Room dr = new DungeonRoom();
        dr.generateRoom(30,40);
        
        Difficulty.worldHeight = dr.getRoomHeight();
        Difficulty.worldWidth = dr.getRoomWidth();
        Difficulty.newGame();
        
        dr.Draw(mainStage);
        
        player = new Player(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
        
        gm = new GameMessage();
        gm.AddMessage("Welcome");
    }
    @Override
    public void update(float dt) {
        if(player.isDead())
        {
            if(!goSent){
                gm.AddMessage("Game Over");
                goSent = true;
            }
            if(player.isAnimationFinished()){
                gameOver = true;
            }
            return;
        }
        
        potionRespawnTimer += dt;
        List<BaseActor> hPots = BaseActor.getList(mainStage, "com.dslayer.content.Objects.Potions.HealthPotion2");
            
        if(hPots.size() < maxPotionsOnFeild){
            if(potionRespawnTimer > potionRespawnInterval){
                potionRespawnTimer = 0;
                new HealthPotion2(MathUtils.random(Difficulty.worldWidth - (DungeonPanels.defaultSize * 2)) + DungeonPanels.defaultSize, 
                        MathUtils.random(Difficulty.worldHeight - (DungeonPanels.defaultSize * 2))+ DungeonPanels.defaultSize, 
                        mainStage).enableDespawnTimer(30);
            }
        }
        
        List<BaseActor> enemies = BaseActor.getList(mainStage, "com.dslayer.content.Enemy.BaseEnemy");
        
        spawnTime += dt;
        if(spawnTime > spawnTimer && enemies.size() < maxNumOfEnemies ){
            BaseActor b;
            if(MathUtils.randomBoolean()){
                b = new SkeletonMage(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
           }
            else{
                b = new SkeletonWarrior(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
            }
            spawnTime= 0;
            while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                   b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                b.centerAtPosition(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight));
            }
        }
        
        GolemSpawnTime += dt;
        if(GolemSpawnTime > GolemSpawnTimer ){
            BaseActor b = null;
            if(MathUtils.randomBoolean(.8f)){
                b = new BlueGolem(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
           }
            if(b != null){
                gm.AddMessage("Blue Golem Appeared");
                while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                        b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                     b.centerAtPosition(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight));
                }
            }
            GolemSpawnTime = 0;
        }
        
        increaseEnemyTime += dt;
        if(increaseEnemyTime > increaseEnemyTimer){
            maxNumOfEnemies += 2;
            increaseEnemyTime= 0;
        }
        
    }

    private void setupSocketListeners() {
        Multiplayer.socket.on("gameObjectCreated", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                }
                            }).on("gameObjectUpdated", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                }
                            }).on("gameObjectSkillCast", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        int netID = data.getInt("networkID");
                                        int tarX = data.getInt("targetX");
                                        int tarY = data.getInt("targetY");
                                        int from = data.getInt("from");
                                        for(BaseActor actor : gameObjects){
                                            if(actor.getNetworkID() == netID){
                                                //((BaseEnemy)actor).cast();
                                            }
                                        }
                                    }catch(Exception e){
                                        
                                    }
                                }
                            });
    }

    
    
}
