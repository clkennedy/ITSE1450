/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms.Forest;

import com.dslayer.content.Rooms.Dungeon.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Rooms.RoomDoor;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class ForestDoor extends RoomDoor{
    
    private static String Door = "Rooms/Dungeon/Door.png";
    private static String DoorLeft = "Rooms/Dungeon/Door-left.png";
    private static String DoorRight = "Rooms/Dungeon/Door-right.png";
    private static String DoorTop = "Rooms/Dungeon/Door.png";
    //private static float tSize = 80;
    
    public ForestDoor(){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        //loadTexture(UpperLeft);
        //setSize(80,80);
        DefaultSize();
        //setSize(tSize * Options.aspectRatio, tSize * Options.aspectRatio);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
    }
    @Override
    public ForestDoor Bottom(){
        loadTexture(Door);
        DefaultSize();
        return this;
    }
    @Override
    public ForestDoor Left(){
        loadTexture(DoorLeft);
        DefaultSize();
        return this;
    }
    @Override
    public ForestDoor Right(){
        loadTexture(DoorRight);
        DefaultSize();
        return this;
    }
    @Override
    public ForestDoor Top(){
        loadTexture(DoorTop);
        DefaultSize();
        return this;
    }
    
}
