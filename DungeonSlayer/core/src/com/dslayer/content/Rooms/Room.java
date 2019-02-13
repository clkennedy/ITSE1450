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
    
    protected float roomWidth;
    protected float roomHeight;
    
    protected List<BaseActor> roomObjects = new ArrayList();
    
    public enum Size{Small, Medium, Large};
    
    public abstract Room generateRoom();
    public abstract Room generateRoom(Size size);
    public abstract Room fillRoomWithObjects(int num);
    public abstract Room generateRoom(int length, int height);
    
    public abstract void Draw(Stage mainStage);
    
    public float getRoomWidth(){
        return roomWidth;
    }
    public float getRoomHeight(){
        return roomHeight;
    }
    
    public Room getRoom(){
        return _room;
    }
    public int[][] getLayout(){
        return _layout;
    }    
}
