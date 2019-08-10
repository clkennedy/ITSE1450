/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy.Skeleton;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.MathUtils;
import com.dslayer.content.options.Difficulty;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.*;
import com.dslayer.content.options.LPC;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */

public class SkeletonArmored extends BaseSkeleton{
    
    private final String skeleArmored = "Enemy/Skeleton/ArmoredSkeleton.png";
    
    Skill skill;
    
    public SkeletonArmored(float x, float y, Stage s){
        
        
        super(x,y,s);
        texture = new Texture(Gdx.files.internal(skeleArmored));
        
        pointsWorth = 25;
        
        maxHealth = 175;
        health = maxHealth;
        healthBar = new Rectangle(x, y, maxHealth , 5);
        
        setSize(size,size);
        setBoundaryPolygonLong(10);
        setMaxSpeed(40);
        setOrigin(getWidth() /2, getHeight() / 2);
        attackDamage = 30;
        canGetAngry = true;
        AttackRange = new Circle(x, y, 40* Options.aspectRatio);
        searchTargetRange = 300* Options.aspectRatio;
        TargetRange = new Circle(x, y, searchTargetRange);
        
        castAnimList = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.slash);
        walkAnimList = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.walk);
        dieAnim = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.die).get(0);
        
        currentDirection = WalkDirection.down;
        setAnimation(this.walkAnimList.get(currentDirection.ordinal()));
        
        skill = new Slash();
        skill.setDamage(attackDamage);
        skill.isEnemy(true);
        
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            this.network_id = Integer.toString(Multiplayer.getNextID());
            JSONObject data = new JSONObject();
            try{
                data.put("id",this.network_id);
                data.put("x", x / Options.aspectRatio);
                data.put("y", y / Options.aspectRatio);
                data.put("type", type.ArmoredSkeleton.ordinal());
                Multiplayer.socket.emit("enemyCreated", data);
            }
            catch(Exception e){
                
            }
        }
    }
    @Override
    public void takeDamage(int damage, Player player){
        damage /= 2;
        super.takeDamage(damage, player);
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        if(isDying)
            return;
        if(attacking){
            if(isAnimationFinished()){
                attacking = false;
                skill.cast(this, new Vector2(target.getX(), target.getY()), Skill.From.Enemy);
                canAttack = false;
            }
            return;
        }
        lookForTarget();
        moveTowardTarget2();
        lookForAttack();
        
        applyPhysics(dt);
    }
    
    private void lookForAttack(){
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && !Multiplayer.host)
            return;
        if(!canAttack)
            return;
        ArrayList<BaseActor> boo = BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player");
        for(BaseActor player: boo){
            if(Intersector.overlaps(AttackRange, player.getBoundaryPolygon().getBoundingRectangle())){
                attacking = true;
                setSpeed(0);
                setAnimationWithReset(castAnimList.get(currentDirection.ordinal()));
                setSize(size, size);
                target = player;
                if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
                    JSONObject data = new JSONObject();
                    try{
                    //System.out.println(player.network_id);
                    data.put("id", this.network_id);
                    data.put("target", player.network_id);
                    Multiplayer.socket.emit("enemyAttack", data);
                    }
                    catch(Exception e){
                           System.out.println("Failed to push enemy Attack: Skeleton Warrior");
                    }
                }
            }
        }
    }
    @Override
    public void attack(BaseActor player){
        attacking = true;
        setSpeed(0);
        setAnimationWithReset(castAnimList.get(currentDirection.ordinal()));
        setSize(size, size);
        if(player != null)
            target = player;
    }
    
    private void moveTowardTarget(){
        
        setAcceleration(100);
        if(moveTo.x < getX()){
            accelerateAtAngle(180);
            currentDirection = WalkDirection.left;
        }
        if(moveTo.x > getX()){
            accelerateAtAngle(0);
            currentDirection = WalkDirection.right;
        }
        if(moveTo.y > getY()){
            accelerateAtAngle(90);
            currentDirection = WalkDirection.up;
        }
        if(moveTo.y < getY()){
            accelerateAtAngle(270);
            currentDirection = WalkDirection.down;
        }
        
        setAnimation(walkAnimList.get(currentDirection.ordinal()));
        setSize(size, size);
    }
     @Override
    public void die() {
        super.die();
        setSpeed(0);
        setAnimationWithReset(dieAnim);
        setSize(size, size);
        isDying = true;
    }
}
