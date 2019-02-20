/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Objects.Potions;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Objects.Potion;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import org.json.JSONObject;

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
    
    @Override
    public void act(float dt){
        super.act(dt);
        
        for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
            if(overlaps(player)){
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
        
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.RoomPanels")){
            if(wall.boundaryPolygon == null)
                continue;
            preventOverlap(wall);
        }
        
    }
    
}
