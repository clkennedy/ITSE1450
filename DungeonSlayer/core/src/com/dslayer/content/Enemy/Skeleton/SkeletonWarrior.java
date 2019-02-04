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

/**
 *
 * @author ARustedKnight
 */

public class SkeletonWarrior extends BaseSkeleton{
    
    public SkeletonWarrior(float x, float y, Stage s){
        
        super(x,y,s);
        maxHealth = 100;
        health = maxHealth;
        setAnimation(this.walkAnimList.get(WalkDirection.down.ordinal()));
        setSize(size,size);
        setBoundaryRectangle();
        setMaxSpeed(50);
        
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        if(isDying)
            return;
        lookForTarget();
        moveTowardTarget();
        
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
            moveTo.x = target.getX();
            moveTo.y = target.getY();
        }
        
    }
    
    private void moveTowardTarget(){
        
        setAcceleration(100);
        if(moveTo.x < getX()){
            accelerateAtAngle(180);
            setAnimation(walkAnimList.get( WalkDirection.left.ordinal()));
        }
        if(moveTo.x > getX()){
            accelerateAtAngle(0);
            setAnimation(walkAnimList.get( WalkDirection.right.ordinal()));
        }
        if(moveTo.y > getY()){
            accelerateAtAngle(90);
            setAnimation(walkAnimList.get( WalkDirection.up.ordinal()));
        }
        if(moveTo.y < getY()){
            accelerateAtAngle(270);
            setAnimation(walkAnimList.get( WalkDirection.down.ordinal()));
        }
    }
    
}
