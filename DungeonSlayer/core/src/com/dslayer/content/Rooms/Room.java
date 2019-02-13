/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cameron.kennedy
 */
public abstract class Room {
    
    private Room _room;
    protected int[][] _layout;
    
    protected int roomWidth;
    protected int roomHeight;
    protected int roomX;
    protected int roomY;
    
    protected float roomWidthPixels;
    protected float roomHeightPixels;
    
    public Room(){
        
    }
    
    public Room(int x, int y, int width, int height){
        roomX = x;
        roomY = y;
        roomWidth = width;
        roomHeight = height;
    }
    
    protected List<BaseActor> roomObjects = new ArrayList();
    
    public enum Size{Small, Medium, Large};
    
    public abstract Room generateRoom();
    public abstract Room generateRoom(Size size);
    public abstract Room fillRoomWithObjects(int num);
    public abstract Room generateRoom(int length, int height);
    
    public abstract void Draw(Stage mainStage);
    
    public float getRoomWidthPixels(){
        return roomWidthPixels;
    }
    public float getRoomHeightPixels(){
        return roomHeightPixels;
    }
    
    public Room getRoom(){
        return _room;
    }
    public int[][] getLayout(){
        return _layout;
    }    
    
    public boolean overlaps(Room r){
        if((this.roomX + this.roomWidth > r.roomX)  && (this.roomX + this.roomWidth < r.roomX + r.roomWidth) 
                && (this.roomY + this.roomHeight > r.roomY)&& (this.roomY + this.roomHeight < r.roomY + r.roomHeight))
            return true;
        
        if((this.roomX > r.roomX)  && (this.roomX < r.roomX + r.roomWidth) 
                && (this.roomY> r.roomY)&& (this.roomY < r.roomY + r.roomHeight))
            return true;
        
        if((this.roomX + this.roomWidth  > r.roomX)  && (this.roomX + this.roomWidth  < r.roomX + r.roomWidth) 
                && (this.roomY> r.roomY)&& (this.roomY < r.roomY + r.roomHeight))
            return true;
        
        if((this.roomX> r.roomX)  && (this.roomX< r.roomX + r.roomWidth) 
                && (this.roomY + this.roomHeight> r.roomY)&& (this.roomY  + this.roomHeight< r.roomY + r.roomHeight))
            return true;
        
        return false;
    }
}
