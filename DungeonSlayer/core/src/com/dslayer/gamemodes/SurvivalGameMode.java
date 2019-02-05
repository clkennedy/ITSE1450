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
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Objects.Potions.HealthPotion;
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
        
        //for(int i = 0; i < 20; i ++){
        //    if(MathUtils.randomBoolean()){
        //        new SkeletonMage(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
        //    }
        //    else{
        //         new SkeletonWarrior(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
        //    }
        //}
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
        List<BaseActor> hPots = BaseActor.getList(mainStage, "com.dslayer.content.objects.Potions.HealthPotion");
            
        if(hPots.size() < maxPotionsOnFeild){
            if(potionRespawnTimer > potionRespawnInterval){
                potionRespawnTimer = 0;
                new HealthPotion(MathUtils.random(Difficulty.worldWidth - (DungeonPanels.defaultSize * 2)) + DungeonPanels.defaultSize, 
                        MathUtils.random(Difficulty.worldHeight - (DungeonPanels.defaultSize * 2))+ DungeonPanels.defaultSize, 
                        mainStage).enableDespawnTimer(30);
            }
        }
        
    }

    
}
