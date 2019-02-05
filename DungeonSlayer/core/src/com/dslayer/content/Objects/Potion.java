/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Objects;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.options.Avatars;

/**
 *
 * @author cameron.kennedy
 */
public class Potion extends BaseActor{
    
    protected String potionsPath = "Potions/potions.png";
    protected TextureRegion[][] potionSprites;
    protected boolean despawnable = false;
    protected float despawnTimer = 0f;
    protected float despawnTime = 0f;
    
    public Potion(float x, float y, Stage s){
        super(x,y,s);
        potionSprites = Avatars.loadTextures(potionsPath, 10, 10);
    }
    
    public void enableDespawnTimer(float timer){
        despawnTime = timer;
        despawnable = true;
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        if(despawnable){
            despawnTimer += dt;
            if(despawnTimer > despawnTime){
                remove();
            }
        }
    }
}
