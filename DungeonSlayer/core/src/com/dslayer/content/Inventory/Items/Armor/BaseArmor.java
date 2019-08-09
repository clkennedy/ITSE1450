/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Inventory.Items.Armor;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Hero.Hero;
import com.dslayer.content.Inventory.Items.Items;
import com.dslayer.content.Player.Player;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class BaseArmor extends Items{
    
    protected List<Animation<TextureRegion>> walkAnimList;
    protected List<Animation<TextureRegion>> castAnimList;
    protected List<Animation<TextureRegion>> castBasicAnimList;
    protected List<Animation<TextureRegion>> castAltAnimList;
    protected Animation<TextureRegion> dieAnim;
    
    protected Hero equppedTo;
    
    public BaseArmor(float x, float y, Stage stage){
        super(x,y,stage);
    }
    public BaseArmor(float x, float y, Stage stage, boolean handleMultiplayer){
        super(x,y,stage);
    }
    public BaseArmor(float x, float y, Stage stage, Hero equippedto){
        super(x,y,stage);
        this.equppedTo = equippedto;
        spawned = false;
    }
    public BaseArmor(float x, float y, Stage stage, Hero equippedto, boolean handleMultiplayer){
        super(x,y,stage);
        this.equppedTo = equippedto;
        spawned = false;
    }
    public BaseArmor(){
        //spawned = true;
    }
    
    
    
    @Override
    public void act(float dt){
        
    }
    
    
    public Animation<TextureRegion> playRight(){
        
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(equppedTo.isAttacking()){
                return castAnimList.get(3);
            }
            else
                return walkAnimList.get(3);  
        }
        return null;
    }
    public Animation<TextureRegion> playLeft(){
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(equppedTo.isAttacking()){
                return castAnimList.get(1);
            }
            else
                return walkAnimList.get(1);  
        }
        return null;
    }
    public Animation<TextureRegion> playUp(){
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(equppedTo.isAttacking()){
                return castAnimList.get(0);
            }
            else
                return walkAnimList.get(0);  
        }
        return null;
    }
    public Animation<TextureRegion> playDown(){
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(equppedTo.isAttacking()){
                return castAnimList.get(2);
            }
            else
                return walkAnimList.get(2);  
        }
        return null;
    }
    
    public Animation<TextureRegion> playDie(){
        if(dieAnim != null)
            return dieAnim;
        return null;
    }
    
}
