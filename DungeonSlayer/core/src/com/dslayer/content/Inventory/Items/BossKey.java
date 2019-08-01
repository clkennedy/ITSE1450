/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Inventory.Items;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Player.Player;

/**
 *
 * @author ARustedKnight
 */
public class BossKey extends Items{
    private static String resource = "Icons/BossKey.png";
    
    
    public BossKey(float x, float y, Stage s){
        super(x, y, s);
        loadTexture(resource);
        getBoundaryPolygon();
        setScale(.5f);
        
    }
    
    public BossKey(){
        
    }
    
    @Override
    public void act(float dt){
        if(!spawned)
            return;
        super.act(dt);
        applyPhysics(dt);
        for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
            if(Intersector.overlapConvexPolygons(this.getBoundaryPolygon(), player.getBoundaryPolygon())){
                ((Player)player).addToBackpack(this);
                this.remove();
            }
        }
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.RoomWall")){
            if(wall.boundaryPolygon == null)
                continue;
            preventOverlap(wall);
        }
    }
}
