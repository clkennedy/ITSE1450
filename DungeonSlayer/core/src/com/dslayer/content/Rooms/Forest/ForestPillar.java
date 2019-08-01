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
public class ForestPillar extends RoomWall{
    
    private static String pillar = "Rooms/Forest/TreeWall.png";
    private static String pillar1 = "Rooms/Forest/TreeOrange.png";
    private static String pillar2 = "Rooms/Forest/TreeApple.png";
    private static float tSize = 80;
    
    public ForestPillar(){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        loadTexture(getRandomPillar());
        //setSize(80,80);
        setSize(tSize * Options.aspectRatio, tSize * Options.aspectRatio);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
    }
    
    public ForestPillar(float size){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        loadTexture(getRandomPillar());
        //setSize(80,80);
        setSize(size * Options.aspectRatio, size * Options.aspectRatio);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
    }
    
    private String getRandomPillar(){
        String rtnVal = "";
        if(MathUtils.randomBoolean(.5f)){
            rtnVal = pillar;
        }else if(MathUtils.randomBoolean(.5f)){
            rtnVal = pillar1;
        }else{
            rtnVal = pillar2;
        }
        return rtnVal;
    }
    
    /*public DungeonPillar(float x, float y, Stage stage){
        //texture = new Texture(Gdx.files.internal(dHole));
        super(x,y,stage);
        loadTexture(dPillar);
        setPosition(x, y);
        //setSize(80,80);
        setSize(tSize * Options.aspectRatio, tSize * Options.aspectRatio);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
        
        stage.addActor(this);
    }*/
    
}
