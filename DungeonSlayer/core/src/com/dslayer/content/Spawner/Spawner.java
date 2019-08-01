/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Spawner;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Boss.Phantom.PhantomBoss;
import com.dslayer.content.Enemy.Skeleton.SkeletonArmored;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class Spawner {
    
    public static enum enemyTypes{Golem, Goblin, Skeleton};
    public static enum Bosses{};
    
    private Spawner(){}
    
    public static BaseEnemy spawnRandomEnemy(){
        return null;
    }
    
    public static BaseEnemy spawnRandomBoss(Room room){
        Vector2 spawnPos = getSpawnLocation(room);
        
        BaseEnemy b = null;
        b = new PhantomBoss(spawnPos.x, spawnPos.y, BaseActor.getMainStage());
        if(MathUtils.randomBoolean(.1f)){
            
        }
        if(room != null){
            b.setRoom(room);
            room.addEnemy(b);
        }
        return b;
    }
    
    public static BaseEnemy spawnRandomEnemy(Room room){
        return spawnRandomSkeleton(room);
    }
    
    public static Vector2 getSpawnLocation(Room room){
        Vector2 spawnPos = new Vector2(0,0);
        float x = 0;
        float y = 0;
        if(room != null){
            x = MathUtils.random((room.getRoomX() + 2) * RoomPanels.defaultSize, 
                    (room.getRoomX() + room.getRoomWidth() - 2) * RoomPanels.defaultSize);
            y = MathUtils.random((Difficulty.worldHeight - ((room.getRoomY() + 2)) * RoomPanels.defaultSize), 
                    Difficulty.worldHeight - ((room.getRoomY() + room.getRoomHeight() - 2) * RoomPanels.defaultSize));
            //System.out.println(x + " | " + y);
        }else{
            x = MathUtils.random(RoomPanels.defaultSize, 
                    Difficulty.worldWidth - RoomPanels.defaultSize);
            y = MathUtils.random(RoomPanels.defaultSize, 
                    Difficulty.worldHeight - RoomPanels.defaultSize);
        }
        spawnPos = new Vector2(x, y);
        return spawnPos;
    }
    
    private static BaseEnemy spawnRandomSkeleton(Room room){
        Vector2 spawnPos = getSpawnLocation(room);
        
        BaseEnemy b = null;
        if(MathUtils.randomBoolean(.6f)){
            b = new SkeletonWarrior(spawnPos.x, spawnPos.y, BaseActor.getMainStage());
        }else if(MathUtils.randomBoolean(.6f)){
            b = new SkeletonMage(spawnPos.x, spawnPos.y, BaseActor.getMainStage());
        }else{
            b = new SkeletonArmored(spawnPos.x, spawnPos.y, BaseActor.getMainStage());
        }
        if(room != null){
            b.setRoom(room);
            room.addEnemy(b);
        }
        return b;
    }
    
}
