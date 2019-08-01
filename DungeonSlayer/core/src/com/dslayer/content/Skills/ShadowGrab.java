/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.Dungeon.DungeonObject;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Options;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.projectiles.Spells.Projectiles;
import static com.dslayer.content.projectiles.Spells.Projectiles.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class ShadowGrab extends Skill{
    
    private String ico = "Icons/ArrowShotIcon.png";
    private String icoCD = "Icons/ArrowShotIconCD.png";
    
    private String soundPath = "";
    
    private Sound sound;
    
    List<BaseActor> alreadyHit;
    
    private float baseSpeed;
    
    Vector2 targetPos;
    BaseActor targetActor;
    boolean followTarget = false;
    
    float trackingTime = 1.3f;
    float trackingTimeTimer = 0f;
    
    public ShadowGrab(){
        super();
        setup();
        
    }
    
    public ShadowGrab(float x, float y, Stage s){
        super(x,y,s);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setup();
    }
    
    private void setup(){
        /*setAnimation(Projectiles.getFireBallAnim());
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygonHalfWidth(12);
        setRotation(90);
        damage = 50;*/
    }
    @Override
    public void setupIcon(float x, float y){
        super.setupIcon(x, y);
        loadCastTexture();
    }
    public void act(float dt){
        
        super.act(dt);
        if(!isAction)
            return;
        getBoundaryPolygon();
        //accelerateAtAngle(direction);
        applyPhysics(dt);
        
        if(trackingTimeTimer < trackingTime && followTarget){
            trackingTimeTimer += dt;
            this.setX(targetActor.getX());
            this.setY(targetActor.getY() - (targetActor.getHeight() / 2));
            setZIndex(900);
            return;
        }else if(trackingTimeTimer < trackingTime){
            trackingTimeTimer += dt;
            this.setX(targetPos.x);
            this.setY(targetPos.y);
            setZIndex(900);
            return;
        }
        
        setAnimationPaused(false);
        if(isAnimationFinished()){
            remove();
            return;
        }
        setZIndex(15000);
        if(isAnimationPastHalfway()){
            if(from == Skill.From.Enemy){
                for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                    if(overlaps(player) && !alreadyHit.contains(player)){
                        ((Player)player).takeDamage((int)damage);
                        if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                            skillHit.play(Options.soundVolume);
                        }
                        alreadyHit.add(player);
                    }
                }
            }
            if(from == Skill.From.Player){
                for(BaseActor enemy: BaseActor.getList(this.getStage(), "com.dslayer.content.Enemy.BaseEnemy")){
                    if(overlaps(enemy) && !alreadyHit.contains(enemy)){
                        ((BaseEnemy)enemy).takeDamage((int)damage, player);
                        if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                            skillHit.play(Options.soundVolume);
                        }
                        alreadyHit.add(enemy);
                    }
                }
            }
            
        }
     }

    @Override
    public BaseActor cast(BaseActor caster, Vector2 target, Skill.From from) {
        //float degrees = (float)(MathUtils.atan2((target.y - (caster.getY() + caster.getHeight()) )
                //, target.x - (caster.getX() + caster.getWidth())) * 180.0d / Math.PI);
        
        BaseActor b = new ShadowGrab(caster.getX() + caster.getWidth() / 2,caster.getY() + caster.getHeight() / 2 , 
                BaseActor.getMainStage()).isProjectile()
                //.setProjectileSpeed(300)
                //.setProjectileDeAcceleration(600)
                //.setProjectileRotation(degrees)
                //.setDirection(degrees)
                .setFrom(from);
                canCast = false;
                //b.setMotionAngle(degrees);
                ((ShadowGrab)b).targetPos = target;
        if(from == Skill.From.Player){
            ((Skill)b).player = ((Player)caster);
        }      
        return b;
    }
    
    public BaseActor cast(BaseActor caster, BaseActor target, Skill.From from) {
        BaseActor b = new ShadowGrab(caster.getX() + caster.getWidth() / 2,caster.getY() + caster.getHeight() / 2 , 
                BaseActor.getMainStage()).isProjectile()
                .setFrom(from);
                canCast = false;
                ((ShadowGrab)b).targetActor = target;
                ((ShadowGrab)b).followTarget = true;
        if(from == Skill.From.Player){
            ((Skill)b).player = ((Player)caster);
        }  
        return b;
    }
    
    public ShadowGrab isProjectile(){
        isAction = true;
        loadAnimationFromSheet(ShadowHand,1,8,.15f, false);
        setAnimationPaused(true);
        setScale(.9f * Options.aspectRatio);
        alreadyHit = new ArrayList<BaseActor>();
        setProjectileSpeed(baseSpeed);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setPosition(getX() - (getWidth() /2) , getY() - (getHeight() / 2));
        setBoundaryPolygon(12);
        damage = 50;
        return this;
    }
    
   @Override
    protected void loadCDTexture() {
        //baIcon.loadTexture(icoCD);
        baIcon.loadTexture(icoCD);
        baIcon.setSize(iconSize* Options.aspectRatio, iconSize* Options.aspectRatio);
    }
    @Override
    protected void loadCastTexture() {
        //baIcon.loadTexture(ico);
        baIcon.loadTexture(ico);
        baIcon.setSize(iconSize* Options.aspectRatio, iconSize* Options.aspectRatio);
    }

}
