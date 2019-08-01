/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms.Forest;

import com.dslayer.content.Rooms.Dungeon.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Rooms.RoomWall;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class ForestWall extends RoomWall{
    
    private static String UpperLeft = "Rooms/Forest/TreeWallGrass.png";
    private static String Upper = "Rooms/Forest/TreeWallGrass.png";
    private static String UpperRight = "Rooms/Forest/TreeWallGrass.png";
    private static String Right = "Rooms/Forest/TreeWallGrass.png";
    private static String LowerRight = "Rooms/Forest/TreeWallGrass.png";
    private static String Lower = "Rooms/Forest/TreeWallGrass.png";
    private static String LowerLeft = "Rooms/Forest/TreeWallGrass.png";
    private static String Left = "Rooms/Forest/TreeWallGrass.png";
    //private static float tSize = 80;
    private static String tree = "Rooms/Forest/TreeWallGrass.png";
    private static String tree1 = "Rooms/Forest/TreeOrangeGrass.png";
    private static String tree2 = "Rooms/Forest/TreeAppleGrass.png";
    
    public ForestWall(){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        //loadTexture(UpperLeft);
        //setSize(80,80);
        DefaultSize();
        //setSize(tSize * Options.aspectRatio, tSize * Options.aspectRatio);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
    }
    
    private String getRandomPillar(){
        String rtnVal = "";
        if(MathUtils.randomBoolean(.5f)){
            rtnVal = tree;
        }else if(MathUtils.randomBoolean(.5f)){
            rtnVal = tree1;
        }else{
            rtnVal = tree2;
        }
        return rtnVal;
    }
    
    public ForestWall UpperLeft(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    public ForestWall Upper(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    public ForestWall UpperRight(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    public ForestWall Right(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    public ForestWall LowerRight(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    public ForestWall Lower(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    public ForestWall LowerLeft(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    public ForestWall Left(){
        loadTexture(getRandomPillar());
        DefaultSize();
        return this;
    }
    
}
