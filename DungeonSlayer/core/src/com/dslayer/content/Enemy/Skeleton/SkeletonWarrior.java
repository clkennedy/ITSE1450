/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy.Skeleton;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.MathUtils;
import com.dslayer.content.options.Difficulty;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.*;

/**
 *
 * @author ARustedKnight
 */

public class SkeletonWarrior extends BaseSkeleton{
    
    Skill skill;
    
    public SkeletonWarrior(float x, float y, Stage s){
        
        super(x,y,s);
        maxHealth = 50;
        health = maxHealth;
        healthBar = new Rectangle(x, y, maxHealth , 5);
        currentDirection = WalkDirection.down;
        setAnimation(this.walkAnimList.get(currentDirection.ordinal()));
        setSize(size,size);
        setBoundaryPolygon(8);
        setMaxSpeed(50);
        
        attackDamage = 30;

        AttackRange = new Circle(x, y, 40);
        TargetRange = new Circle(x, y, 300);
        
        skill = new Slash();
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
        if(!canAttack)
            return;
        if(target != null){
            if(Intersector.overlaps(AttackRange, target.getBoundaryPolygon().getBoundingRectangle())){
                attacking = true;
                setSpeed(0);
                setAnimationWithReset(slashAnimList.get(currentDirection.ordinal()));
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
