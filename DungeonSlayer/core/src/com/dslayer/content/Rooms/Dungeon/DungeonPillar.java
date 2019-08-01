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
public class DungeonPillar extends RoomWall{
    
    private static String dPillar = "Rooms/Dungeon/DungeonPillar.png";
    private static float tSize = 80;
    
    public DungeonPillar(){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        loadTexture(dPillar);
        //setSize(80,80);
        DefaultSize();
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
    }
    
    public DungeonPillar(float size){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        loadTexture(dPillar);
        //setSize(80,80);
        setSize(size, size);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryRectangle();
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
