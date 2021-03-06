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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Enemy.Goblin.GoblinAssassin;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonArmored;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Font.FontLoader;
import com.dslayer.content.GameMessage.GameMessage;
import com.dslayer.content.Inventory.Items.Potions.HealthPotion;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Forest.ForestRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import com.dslayer.content.screens.MainMenuScreen;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class SurvivalGameMode extends GameMode{
    
    private float potionRespawnInterval = 3f;
    private float potionRespawnTimer = 0;
    private float maxPotionsOnFeild = 6;
    
    private float maxNumOfEnemies = 6;
    private float spawnedEnemies = 0;
    private float spawnTimer = 3f;
    private float spawnTime = 0;
    
    private float GolemSpawnTimer = 75f;
    private float GolemSpawnTime = 0;
    
    private float GoblinSpawn = 15f;
    private float GoblinSpawnTimer = 0;
    
    private float increaseEnemyTimer = 10f;
    private float increaseEnemyTime = 0f;
    
    private boolean goSent = false;
    
    Label points;
    
    private GameMessage gm;
    
    public SurvivalGameMode(Stage s){
        super(s);
        //System.out.println(Gdx.graphics.getHeight());
    }
    
    public SurvivalGameMode(){
        this(BaseActor.getMainStage());
    }
    @Override
    public void setup() {
        
        Multiplayer.host = true;
        Room dr = new DungeonRoom();
        if(Difficulty.RoomType == Difficulty.RoomTypes.Forest){
            dr = new ForestRoom();
        }
        
        dr.generateRoom(30,40);
        dr.fillRoomWithObjects(14);
        
        Difficulty.worldHeight = dr.getRoomHeightPixels();
        Difficulty.worldWidth = dr.getRoomWidthPixels();
        Difficulty.newGame();
        
        dr.Draw(mainStage);
        
        Table pointTable = new Table();
        player = new Player(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)), mainStage);
        Label u = new Label(player.hero.getName() +": ", FontLoader.pointStyle);
        u.setAlignment(Align.left);
        pointTable.add(u);
        points = new Label(Integer.toString(player.getPoints()), FontLoader.pointStyle);
        points.setAlignment(Align.right);
        
        float width = u.getWidth() + points.getWidth();
        pointTable.setWidth(width);
        pointTable.add(points);
        pointTable.row();
        pointTable.setHeight(u.getHeight());
        pointTable.setPosition(pointTable.getWidth() / 2, BaseActor.getUiStage().getHeight() - pointTable.getHeight());
        BaseActor.getUiStage().addActor(pointTable);
        gm = new GameMessage();
        gm.AddMessage("Welcome");
        
        playMusic("8BitDungSurvival.mp3");
    }
    @Override
    public void update(float dt) {
        if(player.isDead())
        {
            if(!goSent){
                gm.AddMessage("Game Over");
                gm.AddMessage("Total Points: " + player.getPoints());
                goSent = true;
                stopMusic();
                playMusicOnce("Death_Game_Over_.mp3");
            }
            if(player.isAnimationFinished() && gm.isEmpty()){
                gameOver = true;
            }
            return;
        }
        
        points.setText( Integer.toString(player.getPoints()));
        
        potionRespawnTimer += dt;
        List<BaseActor> hPots = BaseActor.getList(mainStage, "com.dslayer.content.Inventory.Items.Potions.HealthPotion");
        
        if(potionRespawnTimer > potionRespawnInterval){
            potionRespawnTimer = 0;
            if(hPots.size() < maxPotionsOnFeild && MathUtils.randomBoolean(.8f)){
                BaseActor a =new HealthPotion(MathUtils.random(Difficulty.worldWidth - (RoomPanels.defaultSize * 2)) + RoomPanels.defaultSize, 
                        MathUtils.random(Difficulty.worldHeight - (RoomPanels.defaultSize * 2))+ RoomPanels.defaultSize, 
                        mainStage);
                        ((HealthPotion)a).enableDespawnTimer(30);
            }
        }
        
        List<BaseActor> enemies = BaseActor.getList(mainStage, "com.dslayer.content.Enemy.BaseEnemy");
        
        spawnTime += dt;
        if(spawnTime > spawnTimer && enemies.size() < maxNumOfEnemies ){
            BaseActor b;
            if(MathUtils.randomBoolean(.4f)){
                b = new SkeletonMage(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)), mainStage);
           }
            else if(MathUtils.randomBoolean(.95f)){
                b = new SkeletonWarrior(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)), mainStage);
            }
            else{
                 b = new SkeletonArmored(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)), mainStage);
                 gm.AddMessage("An Armored Skeleton has Risen");
            }
            spawnTime= 0;
            while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                b.setPosition(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)));
            }
        }
        
        GolemSpawnTime += dt;
        if(GolemSpawnTime > GolemSpawnTimer ){
            BaseActor b = null;
            if(MathUtils.randomBoolean(.8f)){
                b = new BlueGolem(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)), mainStage);
                if(b != null){
                    gm.AddMessage("Blue Golem Appeared");
                    while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                        b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                        b.setPosition(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)));
                    }
                }
            }
            GolemSpawnTime = 0;
        }
        
        GoblinSpawnTimer += dt;
        if(GoblinSpawnTimer > GoblinSpawn){
            GoblinSpawnTimer = 0;
            BaseActor b = null;
            if(MathUtils.randomBoolean(.1f)){
                b = new GoblinAssassin(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)), mainStage);
            }
            if(b != null){
                    gm.AddMessage("Goblin Assassin has Scurried in");
                    while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                        b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                        b.setPosition(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - (RoomPanels.defaultSize)), 
                        MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - (RoomPanels.defaultSize)));
                    }
                }
        }
        
        increaseEnemyTime += dt;
        if(increaseEnemyTime > increaseEnemyTimer){
            maxNumOfEnemies += 2;
            increaseEnemyTime= 0;
        }
        
    }

    
    
}
