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
public class ArrowShot extends Skill{
    
    private String ico = "Icons/ArrowShotIcon.png";
    private String icoCD = "Icons/ArrowShotIconCD.png";
    
    private String soundPath = "";
    
    private Sound sound;
    
    private int pierce = 3;
    
    private float stayArroundDureation = 2;
    private float stayTimer=0;
    
    List<BaseActor> alreadyHit;
    
    private float baseSpeed;
    
    public ArrowShot(){
        super();
        setup();
        
    }
    
    public ArrowShot(float x, float y, Stage s){
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
        
        if(getSpeed() <=0){
            stayTimer+=dt;
            if(stayTimer > stayArroundDureation){
                remove();
            }
            return;
        }
        
        if(from == Skill.From.Enemy){
            for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                if(overlaps(player) && !alreadyHit.contains(player)){
                    ((Player)player).takeDamage((int)damage);
                    if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                        skillHit.play(Options.soundVolume);
                    }
                    alreadyHit.add(player);
                    pierce--;
                    if(pierce == 0)
                        remove();
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
                    pierce--;
                    if(pierce == 0)
                        remove();
                }
            }
        }
        
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.RoomWall")){
            if(wall.boundaryPolygon == null || (wall instanceof DungeonObject))
                continue;
            if(overlaps(wall)){
                setSpeed(0);
            }
            preventOverlap(wall);
            
        }
     }

    @Override
    public BaseActor cast(BaseActor caster, Vector2 target, Skill.From from) {
        float degrees = (float)(MathUtils.atan2((target.y - (caster.getY() + caster.getHeight()) )
                , target.x - (caster.getX() + caster.getWidth())) * 180.0d / Math.PI);
        
        BaseActor b = new ArrowShot(caster.getX() + caster.getWidth() / 2,caster.getY() + caster.getHeight() / 2 , 
                BaseActor.getMainStage()).isProjectile()
                //.setProjectileSpeed(300)
                .setProjectileDeAcceleration(600)
                .setProjectileRotation(degrees)
                .setDirection(degrees)
                .setFrom(from);
                canCast = false;
                b.setMotionAngle(degrees);
        if(from == Skill.From.Player){
            ((Skill)b).player = ((Player)caster);
        }
        return b;   
    }
    
    public ArrowShot isProjectile(){
        isAction = true;
        loadTexture(Arrow);
        setScale(.1f * Options.aspectRatio);
        baseSpeed = 700 * Options.aspectRatio;
        alreadyHit = new ArrayList<BaseActor>();
        setProjectileSpeed(baseSpeed);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setPosition(getX() - (getWidth() /2) , getY() - (getHeight() / 2));
        setBoundaryPolygonLong(12);
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

    @Override
    public BaseActor cast(BaseActor arg0, Vector2 target,float degrees, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BaseActor cast(BaseActor arg0, BaseActor arg1, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
