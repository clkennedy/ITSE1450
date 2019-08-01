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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Goblin.GoblinAssassin;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonArmored;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Font.FontLoader;
import com.dslayer.content.GameMessage.GameMessage;
import com.dslayer.content.Inventory.Items.BossKey;
import com.dslayer.content.LevelGenerator.LevelGenerator;
import com.dslayer.content.Inventory.Items.Potions.HealthPotion;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Forest.ForestRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Rooms.RoomDoor;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.Spawner.Spawner;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import com.dslayer.content.screens.HeroSelectionScreenDungeon;
import com.dslayer.content.screens.MainMenuScreen;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class DungeonCrawlGameMode extends GameMode{
    
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
    
    List<Room> nonBossRooms;
    Room bossRoom;
    BaseEnemy boss;
    
    Label points;
    private int dungeonWidth = 31;
    private int dungeonHeight = 31;
    private GameMessage gm;
    
    public DungeonCrawlGameMode(Stage s){
        super(s);
        //System.out.println(Gdx.graphics.getHeight());
    }
    
    public DungeonCrawlGameMode(){
        this(BaseActor.getMainStage());
    }
    @Override
    public void setup() {
        
        Multiplayer.host = true;
        
        LevelGenerator lg = new LevelGenerator(dungeonWidth, dungeonHeight);
        lg.setDefaultSize(100);
        if(Difficulty.RoomType == Difficulty.RoomTypes.Dungeon){
            lg.setRoom(new DungeonRoom());
        }else if(Difficulty.RoomType == Difficulty.RoomTypes.Forest){
            lg.setRoom(new ForestRoom());
        }
        
        lg.generateMap();
        
        Difficulty.worldHeight = lg.getPixelHeight();
        Difficulty.worldWidth = lg.getPixelWidth();
        System.out.println(Difficulty.worldWidth);
        System.out.println(Difficulty.worldHeight);
        Difficulty.newGame();
        
        //dr.Draw(mainStage);
        lg.draw(mainStage);
        BaseActor.setMainStage(mainStage);
        Table pointTable = new Table();
        //player = new Player(MathUtils.random(RoomPanels.defaultSize,Difficulty.worldWidth - RoomPanels.defaultSize), 
                       // MathUtils.random(RoomPanels.defaultSize,Difficulty.worldHeight - RoomPanels.defaultSize), mainStage);
        
        Room spawnRoom = lg.getRandomNonBossRoom();
        nonBossRooms = lg.getNonBossRooms();
        bossRoom = lg.getBossRooms();
        
        Vector2 pSpawn = Spawner.getSpawnLocation(spawnRoom);
        
        player = new Player(pSpawn.x,pSpawn.y, mainStage);
        player.setHero(HeroSelectionScreenDungeon.currentSelection);
        points = new Label("", FontLoader.pointStyle);
        points.setAlignment(Align.left);
        
        float width = points.getWidth();
        pointTable.setWidth(100);
        pointTable.add(points);
        pointTable.row();
        pointTable.setHeight(50);
        pointTable.setPosition(pointTable.getWidth() / 2, BaseActor.getUiStage().getHeight() - pointTable.getHeight());
        BaseActor.getUiStage().addActor(pointTable);
        
        for(int i = 0; i < nonBossRooms.size(); i++){
            Room r = nonBossRooms.get(i);
            int randNumOfEn = 0;
            if(r == spawnRoom){
                randNumOfEn = MathUtils.random(1, 4); 
            }else{
               randNumOfEn = MathUtils.random(2, 9); 
            }
            for(int j = 0; j < randNumOfEn; j ++){
                BaseEnemy b = Spawner.spawnRandomEnemy(r);
                if(MathUtils.randomBoolean(.6f)){
                    int randPots = MathUtils.random(0, 3);
                    for(int k = 0; k < randPots; k ++){
                        b.addToBackpack(new HealthPotion());
                    }
                }
            }
        }
       
        boss = Spawner.spawnRandomBoss(bossRoom);
        
        int rand = MathUtils.random(nonBossRooms.size() - 1);
        nonBossRooms.get(rand).getRandomEnemy().addToBackpack(new BossKey());
        
        gm = new GameMessage();
        RoomDoor.gm = gm;
        gm.AddMessage("Welcome");
        //player.addToBackpack(new BossKey());
    }
    @Override
    public void update(float dt) {
        if(player.isDead())
        {
            if(!goSent){
                gm.AddMessage("Game Over");
                gm.AddMessage("Total Points: " + player.getPoints());
                goSent = true;
            }
            if(player.isAnimationFinished() && gm.isEmpty()){
                gameOver = true;
            }
            return;
        }
        
        if(boss != null && boss.isDead())
        {
            if(!goSent){
                gm.AddMessage("Dungeon Cleared");
                gm.AddMessage("Congradulations");
                goSent = true;
            }
            if(boss.isAnimationFinished() && gm.isEmpty()){
                gameOver = true;
            }
            return;
        }
        if(player.inventoryContains(BossKey.class)){
            points.setText("You Have a Boss Key!");
        }
    }

    
    
}
