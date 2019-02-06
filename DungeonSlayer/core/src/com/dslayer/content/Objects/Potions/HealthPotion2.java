/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Objects.Potions;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Objects.Potion;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.Player.Player;

/**
 *
 * @author cameron.kennedy
 */
public class HealthPotion2 extends Potion{
    
    protected float recoverAmount;
    
    
    public HealthPotion2(float x, float y, Stage s) {
        super(x, y, s);
        recoverAmount = 50;
        loadTexture(potionSprites[0][9]);
        setSize(20,20);
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        
        for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
            if(overlaps(player)){
                ((Player)player).recover((int)recoverAmount);
                remove();
            }
        }
        
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels")){
            if(wall.boundaryPolygon == null)
                continue;
            preventOverlap(wall);
        }
        
    }
    
}
