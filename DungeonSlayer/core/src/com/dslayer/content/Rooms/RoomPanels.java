/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dslayer.content.options.Options;

/**
 *
 * @author cameron.kennedy
 */
public class RoomPanels extends BaseActor{
    private static final float originalDefaultSize = 50;
    public static float defaultSize = 50 * Options.aspectRatio;
    
    protected BaseActor DefaultSize(){
        setScale(1f);
        setSize(defaultSize, defaultSize);
        setZIndex(0);
        return this;
    }
    
    public static void setDefaultSize(float size){
        defaultSize = size* Options.aspectRatio;
    }
    public static float getDefaultSize(){
        return defaultSize;
    }
     public static void resetDefaultSize(){
        defaultSize = originalDefaultSize * Options.aspectRatio;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
    @Override
    public void act(float dt){
         super.act(dt);
    }
     
}
