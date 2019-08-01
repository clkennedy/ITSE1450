/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy.Skeleton;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class BaseSkeleton extends BaseEnemy{
    
    protected List<Animation<TextureRegion>> walkAnimList;
    protected List<Animation<TextureRegion>> slashAnimList;
    protected List<Animation<TextureRegion>> castAnimList;
    protected Animation<TextureRegion> dieAnim;

    
    
    @Override
    public void attack(BaseActor player) {
    }

    
    protected enum WalkDirection{up,left,down,right};
    protected WalkDirection currentDirection;

    public BaseSkeleton() {
        
    }
    
    public BaseSkeleton(float x, float y, Stage s){
        super(x,y,s);
        
        AttackRange = new Circle(x, y, 100* Options.aspectRatio);
        TargetRange = new Circle(x, y, 100* Options.aspectRatio);
        
        moveTo = new Vector2();
        moveTo.x = MathUtils.random(Difficulty.worldWidth);
        moveTo.y = MathUtils.random(Difficulty.worldHeight);
        
        moveToRange = new Circle(moveTo.x, moveTo.y, 30);
        
    }
    
    public void draw(Batch batch, float parentAlpha) {
                
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if(debug){
               batch.end();
                sRend.setProjectionMatrix(this.getStage().getCamera().combined);
                sRend.setColor(Color.WHITE);
                sRend.begin(ShapeRenderer.ShapeType.Line);
                //this.getBoundaryPolygon();
                sRend.circle(TargetRange.x, TargetRange.y, TargetRange.radius);
                sRend.setColor(Color.RED);
                sRend.circle(AttackRange.x, AttackRange.y, AttackRange.radius);
                sRend.setColor(Color.BLUE);
                sRend.circle(moveToRange.x, moveToRange.y, moveToRange.radius);
                sRend.end();
                batch.begin();
            }
        super.draw(batch, parentAlpha);
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        updateRanges();
        canAttack(dt);
    }
    private void updateRanges(){
        TargetRange.setPosition(new Vector2(getX() + (getWidth()/2), getY()+(getHeight()/2)));
        AttackRange.setPosition(new Vector2(getX() + (getWidth()/2), getY()+(getHeight()/2)));
        moveToRange.setPosition(moveTo);
    }
    @Override
    public void die() {
        super.die();
        setSpeed(0);
        setAnimationWithReset(dieAnim);
        setSize(size, size);
        isDying = true;
    }
    
    private void canAttack(float dt){
        if(attackCooldownTime >= attackCooldown){
            canAttack = true;
            attackCooldownTime = 0;
        }
        if(!canAttack){
            attackCooldownTime += dt;
        }
    }
    protected void moveTowardTarget2(){
        if(!canMove)
            return;
        setAcceleration(100 * Options.aspectRatio);
        if(Intersector.overlaps(moveToRange, getBoundaryPolygon().getBoundingRectangle())){
            return;
        }
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
    
    @Override
    public boolean remove(){
        
        if(dieAnim != null){
            for(int i = 0; i < (int)(dieAnim.getAnimationDuration() / dieAnim.getFrameDuration()); i++){
                if(dieAnim.getKeyFrame(i) != null)
                 dieAnim.getKeyFrame(i).getTexture().dispose();
            }
        }
        
        if(slashAnimList != null){
            for(int h = 0; h < slashAnimList.size(); h ++){
                for(int i = 0; i < (int)(slashAnimList.get(h).getAnimationDuration() / slashAnimList.get(h).getFrameDuration()); i++){
                    if(slashAnimList.get(h).getKeyFrame(i) != null)
                     slashAnimList.get(h).getKeyFrame(i).getTexture().dispose();
                }
            }
        }
        
        if( castAnimList!= null){
            for(int h = 0; h < castAnimList.size(); h ++){
                for(int i = 0; i < (int)(castAnimList.get(h).getAnimationDuration() / castAnimList.get(h).getFrameDuration()); i++){
                    if(castAnimList.get(h).getKeyFrame(i) != null)
                     castAnimList.get(h).getKeyFrame(i).getTexture().dispose();
                }
            }
        }
        
        if( walkAnimList!= null){
            for(int h = 0; h < walkAnimList.size(); h ++){
                for(int i = 0; i < (int)(walkAnimList.get(h).getAnimationDuration() / walkAnimList.get(h).getFrameDuration()); i++){
                    if(walkAnimList.get(h).getKeyFrame(i) != null)
                     walkAnimList.get(h).getKeyFrame(i).getTexture().dispose();
                }
            }
        }
        
        return super.remove();
    }
}
