/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.projectiles.Spells.Projectiles;

/**
 *
 * @author ARustedKnight
 */
public class GroundSlam extends Skill{
    
    private String ico = "Icons/Shatter.png";
    private String icoCD = "Icons/ShatterCD.png";
    
    private float duration = 7;
    private float durationTimer = 0;
    
    private float travelDuration = 1.5f;
    private float travelDurationTimer = 0;
    
    private float tick = .01f;
    private float ticktimer = 0;
    
    private float radius = 15;
    
    private boolean landed = false;
    
    public BaseActor caster;
    private float previousSpeed;
    private float oldMaxSpeed;
    private Vector2 location;
    
    public Circle deadzone;
    
    public GroundSlam(){
        super();
        skillCooldown = 10f;
        damage = 70;
        setup();
    }
    
    public GroundSlam(float x, float y, Stage s){
        super(x,y,s);
        skillCooldown = 32f;
        setup();
    }
    
    public void setCaster(BaseActor caster){
        this.caster = caster;
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
        if(!landed){
            this.centerAtActor(caster);
            caster.setAcceleration(600);
            caster.accelerateAtAngle(direction);
            if(player != null)
                player.setIgnoreRoomObjects(true);
        }
        if(landed){
            if(player != null)
                player.setIgnoreRoomObjects(false);
            setAnimationPaused(false);
            if(ticktimer < tick){
                if(from == Skill.From.Enemy){
                    for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                        if(overlaps(player)){
                            ((Player)player).takeDamage((int)damage);
                        }
                    }
                }
                if(from == Skill.From.Player){
                    for(BaseActor enemy: BaseActor.getList(this.getStage(), "com.dslayer.content.Enemy.BaseEnemy")){
                        if(overlaps(enemy)){
                            ((BaseEnemy)enemy).takeDamage((int)damage, player);
                        }
                    }
                }
            }
            ticktimer = 1;
            durationTimer += dt;
            if(durationTimer > duration){
                remove();
            }
        }
        travelDurationTimer += dt;
        if((travelDurationTimer > travelDuration || 
                Intersector.overlaps(deadzone, caster.getBoundaryPolygon().getBoundingRectangle())) 
                    && !landed){
            caster.setSpeed(0);
            caster.setMaxSpeed(oldMaxSpeed);
            caster.setCanMove(true);
            landed = true;
        }
        applyPhysics(dt);
    }
    
    @Override
    public void cast(BaseActor caster, Vector2 target, Skill.From from) {
        float degrees = (float)(MathUtils.atan2((target.y - (caster.getY() + caster.getHeight()) )
                , target.x - (caster.getX() + caster.getWidth())) * 180.0d / Math.PI);
                Skill b = new GroundSlam(caster.getX() ,caster.getY() , BaseActor.getMainStage()).isProjectile()
                        .setFrom(from);
                  ((GroundSlam)b).setCaster(caster);
                b.direction = degrees;
                ((GroundSlam)b).location = target;
                ((GroundSlam)b).deadzone = new Circle(target.x, target.y, 5);
                caster.setSpeed(0);
                caster.setAcceleration(0);
                caster.accelerateAtAngle(degrees);
                caster.setCanMove(false);
                ((GroundSlam)b).oldMaxSpeed = caster.getMaxSpeed();
                caster.setMaxSpeed(((GroundSlam)b).oldMaxSpeed  * 2);
                ((GroundSlam)b).caster = caster;
                ((GroundSlam)b).damage = this.damage;
                canCast = false;
                if(from == Skill.From.Player){
                    ((Skill)b).player = ((Player)caster);
                }
        }
    
    public GroundSlam isProjectile(){
        isAction = true;
        setAnimation(Projectiles.getShatterAnim());
        setAnimationPaused(true);
        setSize(300,300);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygon(12);
        setZIndex(1200);
        setRotation(MathUtils.random(0, 360));
        return this;
    }
    
    @Override
    protected void loadCDTexture() {
        //baIcon.loadTexture(icoCD);
        baIcon.setAnimation(Avatars.load(icoCD));
        baIcon.setSize(iconSize, iconSize);
    }
    @Override
    protected void loadCastTexture() {
        //baIcon.loadTexture(ico);
        baIcon.setAnimation(Avatars.load(ico));
        baIcon.setSize(iconSize, iconSize);
    }
        
}
