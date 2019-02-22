/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms.Dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Rooms.RoomWall;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class DungeonWall extends RoomWall{
    
    private static String UpperLeft = "Rooms/Dungeon/UpperLeft.png";
    private static String Upper = "Rooms/Dungeon/Upper.png";
    private static String UpperRight = "Rooms/Dungeon/UpperRight.png";
    private static String Right = "Rooms/Dungeon/Right.png";
    private static String LowerRight = "Rooms/Dungeon/LowerRight.png";
    private static String Lower = "Rooms/Dungeon/Lower.png";
    private static String LowerLeft = "Rooms/Dungeon/LowerLeft.png";
    private static String Left = "Rooms/Dungeon/Left.png";
    //private static float tSize = 80;
    
    public DungeonWall(){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        //loadTexture(UpperLeft);
        //setSize(80,80);
        DefaultSize();
        //setSize(tSize * Options.aspectRatio, tSize * Options.aspectRatio);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
    }
    
    public DungeonWall UpperLeft(){
        loadTexture(UpperLeft);
        DefaultSize();
        return this;
    }
    public DungeonWall Upper(){
        loadTexture(Upper);
        DefaultSize();
        return this;
    }
    public DungeonWall UpperRight(){
        loadTexture(UpperRight);
        DefaultSize();
        return this;
    }
    public DungeonWall Right(){
        loadTexture(Right);
        DefaultSize();
        return this;
    }
    public DungeonWall LowerRight(){
        loadTexture(LowerRight);
        DefaultSize();
        return this;
    }
    public DungeonWall Lower(){
        loadTexture(Lower);
        DefaultSize();
        return this;
    }
    public DungeonWall LowerLeft(){
        loadTexture(LowerLeft);
        DefaultSize();
        return this;
    }
    public DungeonWall Left(){
        loadTexture(Left);
        DefaultSize();
        return this;
    }
    
}
