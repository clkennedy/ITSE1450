/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy.Golem;

import com.dslayer.content.Enemy.Skeleton.*;
import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
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
        maxHealth = 150;
        health = maxHealth;
        healthBar = new Rectangle(x, y, maxHealth , 5);
        currentDirection = WalkDirection.down;
        setAnimation(this.walkAnimList.get(currentDirection.ordinal()));
        size= 75;
        setSize(size,size);
        setBoundaryPolygon(8);
        setMaxSpeed(25);
        setOrigin(getWidth() /2, getHeight() / 2);
        attackDamage = 60;

        AttackRange = new Circle(x, y, 150);
        TargetRange = new Circle(x, y, 500);
        
        castAnimList = Avatars.loadMulti(GolemAttak, 4, 7, .3f, false);
        walkAnimList = Avatars.loadMulti(GolemWalk, 4, 7, .5f, true);
        dieAnim = Avatars.loadMulti(GolemDie, 2, 7, .3f, false).get(1);
        
        skill = new GroundSlam();
        skill.setDamage(attackDamage);
        skill.isEnemy(true);
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
    
    private void lookForTarget(){
        for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
            if(player.boundaryPolygon == null)
                continue;
            if(Intersector.overlaps(TargetRange,player.getBoundaryPolygon().getBoundingRectangle())){
                if(target == null)
                    target = player;
            }
            else{
                target = null;
            }
        }
        
        if(target == null && (hitWall || Intersector.overlaps(moveToRange, getBoundaryPolygon().getBoundingRectangle()))){
            moveTo.x = MathUtils.random(Difficulty.worldWidth);
            moveTo.y = MathUtils.random(Difficulty.worldHeight);
            if(hitWall)
                setSpeed(0);
            hitWall = false;
        }
        else if(target != null){
            moveTo.x = target.getX() + (target.getWidth()/2);
            moveTo.y = target.getY() + (target.getHeight()/2);
        }
        
    }
    
    private void lookForAttack(){
        if(!skill.canCast())
            return;
        if(target != null){
            if(Intersector.overlaps(AttackRange, target.getBoundaryPolygon().getBoundingRectangle())){
                attacking = true;
                setSpeed(0);
                setAnimationWithReset(castAnimList.get(currentDirection.ordinal()));
                setSize(size, size);
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
    private void moveTowardTarget2(){
        
        setAcceleration(100);
        float degrees = (float)Math.toDegrees( MathUtils.atan2((moveTo.y - getY()), moveTo.x - getX()));
        accelerateAtAngle(degrees);
        
        if(degrees > -45 && degrees <= 45)
            currentDirection = WalkDirection.right;
        if(degrees > 45 && degrees <= 135)
            currentDirection = WalkDirection.up;
        if(degrees > -135 && degrees <= -45)
            currentDirection = WalkDirection.down;
        if((degrees >= -180 && degrees <= -135) || (degrees >= 135 && degrees <= 180))
            currentDirection = WalkDirection.left;
        
        setAnimation(walkAnimList.get(currentDirection.ordinal()));
        setSize(size, size);
    }
    
}
