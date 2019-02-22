/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms.Dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Rooms.RoomFloor;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class DungeonFloor extends RoomFloor{
    
    private static String dPillar = "Rooms/Dungeon/Floor.png";
    
    public DungeonFloor(){
        super();
        //texture = new Texture(Gdx.files.internal(dHole));
        loadTexture(dPillar);
        DefaultSize();
        setBoundaryRectangle();
        
    }
    
}
