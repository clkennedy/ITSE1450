/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Inventory.Items.Potions;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Inventory.Items.Potion;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import org.json.JSONObject;

/**
 *
 * @author cameron.kennedy
 */
public class HealthPotion extends Potion{
    
    protected float recoverAmount;
    
    private Sound gulp = Gdx.audio.newSound(Gdx.files.internal("Sounds/Gulp.mp3"));
    public HealthPotion(float x, float y, Stage s) {
        super(x, y, s);
        recoverAmount = 50;
        loadTexture(potionSprites[0][9]);
        setSize(20* Options.aspectRatio,20* Options.aspectRatio);
        //enableDespawnTimer(30);
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            this.network_id = Integer.toString(Multiplayer.getNextID());
            JSONObject data = new JSONObject();
            try{
                data.put("id",this.network_id);
                data.put("x", x);
                data.put("y", y);
                Multiplayer.socket.emit("healthPotionCreated", data);
            }
            catch(Exception e){
                System.out.println("Failed to Push Health Potion Creation");
            }
        }
    }
    
    public HealthPotion(float x, float y, Stage s, boolean handleMultiplayer) {
        super(x, y, s);
        recoverAmount = 50;
        loadTexture(potionSprites[0][9]);
        setSize(20* Options.aspectRatio,20* Options.aspectRatio);
        //enableDespawnTimer(30);
        if(handleMultiplayer && Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            this.network_id = Integer.toString(Multiplayer.getNextID());
            JSONObject data = new JSONObject();
            try{
                data.put("id",this.network_id);
                data.put("x", x);
                data.put("y", y);
                Multiplayer.socket.emit("healthPotionCreated", data);
            }
            catch(Exception e){
                System.out.println("Failed to Push Health Potion Creation");
            }
        }
    }
    
    public HealthPotion(){
        super();
        recoverAmount = 50;
        loadTexture(potionSprites[0][9]);
        setSize(20* Options.aspectRatio,20* Options.aspectRatio);
    }
    
    @Override
    public void act(float dt){
        if(!spawned)
            return;
        super.act(dt);
        //applyPhysics(dt);
        for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
            if(overlaps(player)){
                gulp.play(Options.soundVolume * 2f);
                if(((Player)player).isLocalPlayer){
                    ((Player)player).recover((int)recoverAmount);
                    remove();
                    if(Multiplayer.socket != null && Multiplayer.socket.connected()){
                        JSONObject data = new JSONObject();
                        try{
                            data.put("id",this.network_id);
                            Multiplayer.socket.emit("healthPotionPickUp", data);
                        }
                        catch(Exception e){
                            System.out.println("Failed to Push Health Potion Pick Up");
                        }
                    }
                }
            }
        }
        
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.RoomWall")){
            if(wall.boundaryPolygon == null)
                continue;
            preventOverlap(wall);
        }
        
    }
    
}
