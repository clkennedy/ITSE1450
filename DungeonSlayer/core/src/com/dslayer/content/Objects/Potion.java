/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Objects;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Multiplayer;
import org.json.JSONObject;

/**
 *
 * @author cameron.kennedy
 */
public class Potion extends BaseActor{
    
    protected String potionsPath = "Potions/potions.png";
    protected Texture potions;
    protected TextureRegion[][] potionSprites;
    protected boolean despawnable = false;
    protected float despawnTimer = 0f;
    protected float despawnTime = 0f;
    
    public Potion(float x, float y, Stage s){
        super(x,y,s);
        
        potions = new Texture(Gdx.files.internal(potionsPath));
        potionSprites = Avatars.loadTextures(potions, 10, 10);
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
                if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            JSONObject data = new JSONObject();
            try{
                data.put("id",this.network_id);
                Multiplayer.socket.emit("healthPotionPickUp", data);
            }
            catch(Exception e){
                System.out.println("Failed to Push Health Potion Creation");
            }
        }
                remove();
            }
        }
    }
    
    @Override
    public boolean remove(){
        potions.dispose();
        
        return super.remove();
    }
}
