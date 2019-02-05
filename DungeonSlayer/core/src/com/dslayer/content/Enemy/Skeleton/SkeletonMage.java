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
import com.dslayer.content.Objects.Spells.ProjectileSpell;
import com.dslayer.content.Player.Player;

/**
 *
 * @author ARustedKnight
 */

public class SkeletonMage extends BaseSkeleton{
    
    public SkeletonMage(float x, float y, Stage s){
        
        super(x,y,s);
        maxHealth = 100;
        health = maxHealth;
        currentDirection = WalkDirection.down;
        setAnimation(this.walkAnimList.get(currentDirection.ordinal()));
        setSize(size,size);
        setBoundaryRectangle();
        setMaxSpeed(50);
        setOrigin(getWidth() /2, getHeight() / 2);
        attackDamage = 30;

        AttackRange = new Circle(x, y, 300);
        TargetRange = new Circle(x, y, 500);
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        if(isDying)
            return;
        
        if(attacking){
            if(isAnimationFinished()){
                attacking = false;
                float degrees = (float)Math.toDegrees( MathUtils.atan2((moveTo.y - getY()), moveTo.x - getX()));
                new ProjectileSpell(getX() - getWidth()/2 ,getY() - getHeight() /2, getStage())
                        .fireBall()
                        .setProjectileSpeed(300).
                        setProjectileRotation(degrees).
                        setDirection(degrees)
                        .setFrom(ProjectileSpell.From.Enemy);
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
                //System.out.println("Target Aquired");
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
