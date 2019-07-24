/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cameron.kennedy
 */
public abstract class Room {
    
    protected boolean bossRoom = false;
    protected ArrayList<BaseEnemy> enemies;
    protected int roomRegion;
    
    private Room _room;
    protected int[][] _layout;
    
    protected int roomWidth;
    protected int roomHeight;
    protected int roomX;
    protected int roomY;
    
    protected float roomWidthPixels;
    protected float roomHeightPixels;
    
    public Room(){
        enemies = new ArrayList();
    }
    
    public Room(int x, int y, int width, int height){
        roomX = x;
        roomY = y;
        roomWidth = width;
        roomHeight = height;
    }
    
    public static void setDefaultSize(float size){
        RoomPanels.setDefaultSize(size);
    }
    public static float getDefaultSize(){
         return RoomPanels.getDefaultSize();
    }
    
    protected List<BaseActor> roomObjects = new ArrayList();
    
    public enum Size{Small, Medium, Large};
    
    public abstract BaseActor Map(int i);
    public abstract int getFillerObjectKey();
    public abstract int getDoor();
    public abstract int getDoorLeft();
    public abstract int getDoorRight();
    public abstract int getDoorTop();
    public abstract Room generateRoom();
    public abstract Room generateRoom(Size size);
    public abstract Room fillRoomWithObjects(int num);
    public abstract Room generateRoom(int length, int height);
    public abstract Room generateNewRoom(int x, int y, int length, int height);
    public abstract String getFloorTexture();
    
    public abstract void Draw(Stage mainStage);
    
    public float getRoomWidthPixels(){
        return roomWidthPixels;
    }
    public float getRoomHeightPixels(){
        return roomHeightPixels;
    }
    public float getRoomWidth(){
        return roomWidth;
    }
    public float getRoomHeight(){
        return roomHeight;
    }
    public float getRoomX(){
        return roomX;
    }
    public float getRoomY(){
        return roomY;
    }
    public void setRoomRegion(int region){
        this.roomRegion = region;
    }
    public int getRoomRegion(){
        return this.roomRegion;
    }
    public boolean setBossRoom(boolean isBossRoom){
        return bossRoom = isBossRoom;
    }
    public boolean isBossRoom(){
        return bossRoom;
    }
    public Room getRoom(){
        return _room;
    }
    public int[][] getLayout(){
        return _layout;
    }    
    
    public boolean overlaps(Room r){
       /* if((this.roomX + this.roomWidth >= r.roomX)  && (this.roomX + this.roomWidth <= r.roomX + r.roomWidth) 
                && (this.roomY + this.roomHeight >= r.roomY)&& (this.roomY + this.roomHeight <= r.roomY + r.roomHeight))
            return true;
        
        if((this.roomX >= r.roomX)  && (this.roomX <= r.roomX + r.roomWidth) 
                && (this.roomY>= r.roomY)&& (this.roomY <= r.roomY + r.roomHeight))
            return true;
        
        if((this.roomX + this.roomWidth  >= r.roomX)  && (this.roomX + this.roomWidth  <= r.roomX + r.roomWidth) 
                && (this.roomY>= r.roomY)&& (this.roomY <= r.roomY + r.roomHeight))
            return true;
        
        if((this.roomX>= r.roomX)  && (this.roomX<= r.roomX + r.roomWidth) 
                && (this.roomY + this.roomHeight>= r.roomY)&& (this.roomY  + this.roomHeight<= r.roomY + r.roomHeight))
            return true;*/
        
        for(int row = this.roomX; row < this.roomX+this.roomWidth; row++){
            for(int col = this.roomY; col < this.roomY+this.roomHeight; col++){
                if(row >= r.roomX && row <= r.roomX + r.roomWidth && col >= r.roomY && col <= r.roomY + r.roomHeight){
                    return true;
                }
            }
        }
        
        return false;
    }
}
