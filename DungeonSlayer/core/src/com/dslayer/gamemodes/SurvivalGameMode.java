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
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Objects.Potions.HealthPotion2;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.DungeonPanels;
import com.dslayer.content.Rooms.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.options.Difficulty;
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
    
    private float GolemSpawnTimer = 3f;
    private float GolemSpawnTime = 0;
    
    private float increaseEnemyTimer = 10f;
    private float increaseEnemyTime = 0f;
    
    public SurvivalGameMode(Stage s){
        super(s);
        //System.out.println(Gdx.graphics.getHeight());
    }
    
    public SurvivalGameMode(){
        this(BaseActor.getMainStage());
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
        
        new BlueGolem(player.getX() - 100, player.getY() - 100, mainStage);
    }
    @Override
    public void update(float dt) {
        if(player.isDead())
        {
            gameOver = true;
            return;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)){
            //debugEnemy.takeDamage(20);
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
        
        increaseEnemyTime += dt;
        if(increaseEnemyTime > increaseEnemyTimer){
            maxNumOfEnemies += 2;
            increaseEnemyTime= 0;
        }
        
    }

    
}
