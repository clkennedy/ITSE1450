/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author ARustedKnight
 */
public class DungeonPanels {
    
    public static TextureRegion[][] Panels = load("Rooms/Dungeon/DungeonStarter.png",17,6);
    
    private static TextureRegion[][] load(String fileName, int rows, int cols) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                textureArray.add(temp[r][c]);
            }
        }
        
        return temp;        
    }
    
    private static BaseActor DefaultSize(BaseActor t){
        t.setSize(75, 75);
        return t;
    }
    
    public static BaseActor UpperLeftWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[0][0]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor UpperWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[0][1]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor UpperRightWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[0][2]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor LeftWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[1][0]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor Floor(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[1][1]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor RightWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[1][2]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor LowerLeftWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[2][0]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor LowerWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[2][1]);
        DefaultSize(t);
        return t;
    }
    public static BaseActor LowerRightWall(){
        BaseActor t = new BaseActor();
        t.loadTexture(Panels[2][2]);
        DefaultSize(t);
        return t;
    }
}
