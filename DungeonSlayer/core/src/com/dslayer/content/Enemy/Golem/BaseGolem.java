/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy.Golem;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Difficulty;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class BaseGolem extends BaseEnemy{
    
    private final String SkeletonBody = "Enemy/Skeleton/BODY_skeleton.png";
    private final String SkeletonBodySlash = "Enemy/Skeleton/BODY_skeletonSlash.png";
    private final String SkeletonBodyCast = "Enemy/Skeleton/BODY_skeletonCast.png";
    private final String DieAnimPath = "Enemy/Skeleton/BODY_skeletonDeath.png";
    protected List<Animation<TextureRegion>> walkAnimList;
    protected List<Animation<TextureRegion>> slashAnimList;
    protected List<Animation<TextureRegion>> castAnimList;
    protected Animation<TextureRegion> dieAnim;
    
    protected enum WalkDirection{up,left,down,right};
    protected WalkDirection currentDirection;
    
    protected float attackDamage;
    
    protected BaseActor target = null;
    protected Circle AttackRange;
    protected Circle TargetRange;
    
    protected float attackCooldown = 5f;
    protected float attackCooldownTime = 0f;
    protected boolean attacking;
    protected boolean canAttack = true;
    
    protected Vector2 moveTo;
    protected Circle moveToRange;
    
    Skill skill;
    
    public BaseGolem() {
        
    }
    
    public BaseGolem(float x, float y, Stage s){
        super(x,y,s);
        
        walkAnimList = Avatars.loadMulti(SkeletonBody, 4, 9, .1f, true);
        slashAnimList = Avatars.loadMulti(SkeletonBodySlash, 4, 6, .1f, false);
        castAnimList = Avatars.loadMulti(SkeletonBodyCast, 4, 7, .1f, false);
        dieAnim = Avatars.load(DieAnimPath, 1, 6, .2f, false);
        
        AttackRange = new Circle(x, y, 100);
        TargetRange = new Circle(x, y, 100);
        
        moveTo = new Vector2();
        moveTo.x = MathUtils.random(Difficulty.worldWidth);
        moveTo.y = MathUtils.random(Difficulty.worldHeight);
        
        moveToRange = new Circle(moveTo.x, moveTo.y, 30);
        
    }
    
    @Override
    public void takeDamage(int damage){
        damage /= 2;
        super.takeDamage(damage);
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
        setSpeed(0);
        setAnimationWithReset(Avatars.load(DieAnimPath, 1, 6, .2f, false));
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
}
