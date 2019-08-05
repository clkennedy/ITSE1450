/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Inventory.Items;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author ARustedKnight
 */
public class Items extends BaseActor{
    protected boolean spawned = false;
    public Items(float x, float y, Stage stage){
        super(x,y,stage);
        spawned = true;
    }
    public Items(float x, float y, Stage stage, boolean handleMultiplayer){
        super(x,y,stage);
        spawned = true;
    }
    
    public Items(){
        //spawned = true;
    }
    
    public void setSpawned(){
        spawned = true;
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        getBoundaryPolygon();
        applyPhysics(dt);
        //System.out.println(String.format("%s %s %s %s %s %s",network_id, getX(), getY(), getSpeed(), getVelocity(dt),getVelocityAngle() ));
    }
    
}
