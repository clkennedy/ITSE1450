/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy.Golem;

import com.dslayer.content.Enemy.Skeleton.*;
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
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.GroundSlam;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.LPC;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */

public class BlueGolem extends BaseGolem{
    
    private final String GolemAttak = "Enemy/Golem/golem-atk.png";
    private final String GolemWalk = "Enemy/Golem/golem-walk.png";
    private final String GolemDie = "Enemy/Golem/golem-die.png";
    
    public BlueGolem(float x, float y, Stage s){
        
        super(x,y,s);
        die = new Texture(Gdx.files.internal(GolemDie));
        walk = new Texture(Gdx.files.internal(GolemWalk));
        attack = new Texture(Gdx.files.internal(GolemAttak));
        
        pointsWorth = 20;
        
        maxHealth = 150;
        health = maxHealth;
        healthBar = new Rectangle(x, y, maxHealth , 5);
        
        size= 75 * Options.aspectRatio;
        setSize(size,size);
        setBoundaryPolygonLong(10);
        setMaxSpeed(25 * Options.aspectRatio);
        setOrigin(getWidth() /2, getHeight() / 2);
        attackDamage = 60;

        AttackRange = new Circle(x, y, 100* Options.aspectRatio);
        TargetRange = new Circle(x, y, 500* Options.aspectRatio);
        
        castAnimList = Avatars.loadMulti(attack, 4, 7, .2f, false);
        walkAnimList = Avatars.loadMulti(walk, 4, 7, .5f, true);
        dieAnim = Avatars.loadMulti(die, 2, 7, .3f, false).get(1);
        
        currentDirection = WalkDirection.down;
        setAnimation(this.walkAnimList.get(currentDirection.ordinal()));
        
        skill = new GroundSlam();
        skill.setDamage(attackDamage);
        skill.isEnemy(true);
        
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            this.network_id = Integer.toString(Multiplayer.getNextID());
            JSONObject data = new JSONObject();
            try{
                data.put("id",this.network_id);
                data.put("x", x);
                data.put("y", y);
                data.put("type", type.BlueGolem.ordinal());
                Multiplayer.socket.emit("enemyCreated", data);
            }
            catch(Exception e){
                
            }
        }
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        if(isDying)
            return;
        
        if(attacking){
            if(isAnimationFinished()){
                attacking = false;
                skill.cast(this, new Vector2(this.getX(), this.getY()), Skill.From.Enemy);
                canAttack = false;
            }
            return;
        }
        lookForTarget();
        moveTowardTarget2();
        lookForAttack();
        
        applyPhysics(dt);
    }
    
    
    
    @Override
    public void attack(BaseActor player){
        attacking = true;
        setSpeed(0);
        setAnimationWithReset(castAnimList.get(currentDirection.ordinal()));
        setSize(size, size);
        target = player;
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
                    System.out.println(player.network_id);
                    data.put("id", this.network_id);
                    data.put("target", player.network_id);
                    Multiplayer.socket.emit("enemyAttack", data);
                    }
                    catch(Exception e){
                           System.out.println("Failed to push enemy Attack: Blue Golem");
                    }
                }
            }
        }
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
        setSpeed(0);
        setAnimationWithReset(dieAnim);
        setSize(size, size);
        isDying = true;
    }
    
    
}
