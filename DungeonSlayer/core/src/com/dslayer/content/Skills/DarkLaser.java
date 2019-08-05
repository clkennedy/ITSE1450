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
public class DarkLaser extends Skill{
    
    private String ico = "Icons/ArrowShotIcon.png";
    private String icoCD = "Icons/ArrowShotIconCD.png";
    
    private String indicator = "particles/Laser idicator.png";
    
    protected float degrees = 0;
    
    private String soundPath = "";
    
    private Sound sound;
    
    private int pierce = 3;
    
    private float stayArroundDuration = 2;
    private float stayTimer=0;
    
    List<BaseActor> alreadyHit;
    
    private float baseSpeed;
    
    public DarkLaser(){
        super();
        setup();
        
    }
    
    public DarkLaser(float x, float y, Stage s){
        super(x,y,s);
        //setOrigin(getWidth() / 2, getHeight() / 2);
        setup();
    }
    
    private void setup(){
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
        
        
        if(!showIndicator){
            stayTimer+=dt;
            if(stayTimer > stayArroundDuration){
                skillDone = true;
                remove();
            }
                
        }else{
            indCooldownTime += dt;
            if(indCooldownTime > indCooldown){
                showIndicator = false;
                loadAnimationFromSheet(DarkLaser, 11, 1, .02f, true);
                setOrigin(0,0);
                setBoundaryPolygonWide(12);
                //setRotation(0);
                
            }else{
                return;
            }
        }
        
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
                    float d = damage * (getSpeed() /  baseSpeed) * ((float)pierce / 3f);
                    ((BaseEnemy)enemy).takeDamage((int)d, player);
                    if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                        skillHit.play(Options.soundVolume);
                    }
                    alreadyHit.add(enemy);
                }
            }
        }
     }

    @Override
    public BaseActor cast(BaseActor caster, Vector2 target, Skill.From from) {
        
        float degrees = MathUtils.random(360);
        Vector2 distance = new Vector2(30, 0);
        distance.setAngle(degrees);
        target.add(distance);
        BaseActor b = new DarkLaser(caster.getX(),caster.getY(), 
                BaseActor.getMainStage());
        if(from == Skill.From.Enemy){
            ((Skill)b).showIndicator = true;
        }
                ((DarkLaser)b).isProjectile()
                .setFrom(from);
                b.setOrigin(0,0);
                b.setPosition(target.x, target.y);
                b.setRotation(degrees);
                b.setZIndex(1000);
                ((DarkLaser)b).degrees = degrees;
                canCast = false;
        if(from == Skill.From.Player){
            ((Skill)b).player = ((Player)caster);
        }
        return b;   
    }
    
    @Override
    public BaseActor cast(BaseActor caster, Vector2 target,float degrees, From from) {
        
        Vector2 distance = new Vector2(30, 0);
        distance.setAngle(degrees);
        target.add(distance);
        BaseActor b = new DarkLaser(caster.getX(),caster.getY(), 
                BaseActor.getMainStage());
        if(from == Skill.From.Enemy){
            ((Skill)b).showIndicator = true;
        }
                ((DarkLaser)b).isProjectile()
                .setFrom(from);
                b.setOrigin(0,0);
                b.setPosition(target.x, target.y);
                b.setRotation(degrees);
                b.setZIndex(1000);
                ((DarkLaser)b).degrees = degrees;
                canCast = false;
        if(from == Skill.From.Player){
            ((Skill)b).player = ((Player)caster);
        }
        return b;   
    }
    
    public DarkLaser isProjectile(){
        isAction = true;
        if(!showIndicator){
            loadAnimationFromSheet(DarkLaser, 11, 1, .02f, true);
        }else{
            loadTexture(indicator);
        }
        setScale(2f * Options.aspectRatio, 1f * Options.aspectRatio);
        //baseSpeed = 700 * Options.aspectRatio;
        alreadyHit = new ArrayList<BaseActor>();
        //setProjectileSpeed(baseSpeed);
        //setOriginX(getWidth() / 2);
        //setOriginY(getHeight() / 2);
        //setPosition(getX() - getWidth(), getY() - getHeight());
        setBoundaryPolygon(12);
        damage = 100;
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

    

    @Override
    public BaseActor cast(BaseActor arg0, BaseActor arg1, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
