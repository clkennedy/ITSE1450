/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms.Dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class DungeonHole extends DungeonObject{
    
    private static String dHole = "Rooms/Dungeon/DungeonHole.png";
    
    public DungeonHole(){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        loadTexture(dHole);
        setSize(getWidth() * Options.aspectRatio, getHeight() * Options.aspectRatio);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryPolygon(8);
        
    }
    
    public DungeonHole(float x, float y, Stage stage){
        //texture = new Texture(Gdx.files.internal(dHole));
        loadTexture(dHole);
        setSize(getWidth() * Options.aspectRatio, getHeight() * Options.aspectRatio);
        setPosition(x, y);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setBoundaryPolygon(8);
        
        stage.addActor(this);
    }
    
}
