/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Objects.Potions;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Objects.Spells.ProjectileSpell;
import com.dslayer.content.Player.Player;

/**
 *
 * @author cameron.kennedy
 */
public class HealthPotion extends Potion{
    
    protected float recoverAmount;
    
    public HealthPotion(float x, float y, Stage s) {
        super(x, y, s);
        recoverAmount = 50;
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
        
    }
    
}
