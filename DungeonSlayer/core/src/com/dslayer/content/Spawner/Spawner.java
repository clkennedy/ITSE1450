/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Spawner;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Boss.Phantom.PhantomBoss;
import com.dslayer.content.Enemy.Skeleton.SkeletonArmored;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Inventory.Items.Items;
import com.dslayer.content.LevelGenerator.LevelGenerator;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */
public class Spawner {
    
    public static enum enemyTypes{Golem, Goblin, Skeleton};
    public static enum Bosses{};
    
    private static List<BaseActor> spawnedItems = new ArrayList();
    
    private static LevelGenerator _levelGen;
    
    private static Integer[][] mapLayout;
    
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
    
    public static List<BaseActor> getNewlySpawnedItems(){
        List<BaseActor> rtnList = new ArrayList();
        for(BaseActor b : spawnedItems){
            rtnList.add(b);
        }
        spawnedItems.clear();
        return rtnList;
    }
    
    public static BaseEnemy spawnRandomEnemy(Room room){
        return spawnRandomSkeleton(room);
    }
    
    public static List<Vector2> getPath(Vector2 to, Vector2 from){
        
        return null;
    }
    
    public static List<Vector2> getMoveTo(Vector2 from, int spaces){
        
        return null;
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
    
    public static Vector2 getSpawnLocation(LevelGenerator lg){
        //_levelGen = lg;
        mapLayout = new Integer[lg.getMapLayout().length][lg.getMapLayout()[0].length];
        for(int row = 0; row < lg.getMapLayout().length; row ++){
            for(int col = 0; col < lg.getMapLayout()[row].length; col ++){
                mapLayout[row][col] = lg.getMapLayout()[row][col];
            }
        }
        Vector2 spawnPos = new Vector2(0,0);
        float x = 0;
        float y = 0;
        Vector2 sp = _levelGen.getSpawnPosition();
        if(sp != null){
            return sp;
        }else{
            spawnPos = getSpawnLocation(lg.getRandomNonBossRoom());
        }
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
    
    public static void spawnItem(float x, float y, Class<? extends Items> cls){
        
        try{
            Items i = cls.getDeclaredConstructor(float.class,float.class,Stage.class, boolean.class).newInstance(x,y,BaseActor.getMainStage(), false);
            i.setAcceleration(0);
            i.setDeceleration(20 * Options.aspectRatio);
            i.setSpeed(50 * Options.aspectRatio);
            float degrees = (float)((MathUtils.random((float)360)));
            i.setMotionAngle(degrees); 
            i.setZIndex(1000);
            
            if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            i.network_id = Integer.toString(Multiplayer.getNextID());
            JSONObject data = new JSONObject();
            try{
                data.put("id", i.network_id);
                data.put("x", x);
                data.put("y", y);
                data.put("speed", 50);
                data.put("dec", 20);
                data.put("degrees", degrees);
                data.put("class", i.getClass().toString());
                Multiplayer.socket.emit("GameItemSpawned", data);
            }
            catch(Exception e){
                System.out.println("Failed to Push Game Item From Spawner Class");
            }
            spawnedItems.add(i);
        }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }      //System.out.println(String.format("%s %s %s %s %s %s", i.getClass(), i.getSpeed(), i.getX(), i.getY(), i.getMotionAngle(), ""));
    }
    
    public static BaseActor spawnItemMultiplayer(JSONObject data){
        String classString = "";
        try{
            classString= data.getString("class");
            classString =classString.replace("class ", "");
            Class<? extends Items> cls = (Class<? extends Items> )Class.forName(classString);
            int x = data.getInt("x");
            int y = data.getInt("y");
            String id = data.getString("id");
            int speed = data.getInt("speed");
            int dec = data.getInt("dec");
            double degrees = data.getDouble("degrees");
            Items i = cls.getDeclaredConstructor(float.class,float.class,Stage.class, boolean.class).newInstance(x * Options.aspectRatio,y * Options.aspectRatio,BaseActor.getMainStage(), false);
            
            i.network_id = id;
            i.setAcceleration(0);
            i.setDeceleration(dec * Options.aspectRatio);
            i.setSpeed(speed * Options.aspectRatio);
            i.setMotionAngle((float) degrees); 
            i.setZIndex(1000);
            
            return i;
         }
         catch(Exception e){
             String st = "";
             for(StackTraceElement se : e.getStackTrace()){
                 st += se.getLineNumber() + ": " + se.getMethodName() + ": " + "\r\n";
             }
             System.out.println("Failed to create Game Item from Spawner Class: "  + classString + ": "+ e.toString() + "\r\n " + st);
         }
        return null;
    }
    
}
