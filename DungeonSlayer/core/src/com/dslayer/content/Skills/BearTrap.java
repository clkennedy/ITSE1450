/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Player;
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
public class BearTrap extends Skill{
    
    private String ico = "Icons/BearTrap.png";
    private String icoCD = "Icons/BearTrapCD.png";
    
    private String soundPath = "";
    
    private Sound sound;
    
    private int pierce = 3;
    
    private float stayArroundDureation = 2;
    private float stayTimer=0;
    
    List<BaseActor> alreadyHit;
    
    private float rechargeRate = 5f;
    private float rechargeTimer = 0f;
    
    private float trapDuration = 30;
    private float trapTimer = 0;
    
    private int maxCount = 3;
    private int currentCount=0;
    
    private float rootDuration = 3;
    private float rootTimer=0;
    
    BaseActor trappedEnemy;
    
    private boolean sprung = false;
    
    private boolean setCooldown = false;
    
    public BearTrap(){
        super();
        setup();
        
    }
    
    public BearTrap(float x, float y, Stage s){
        super(x,y,s);
        setOrigin(getWidth() / 2, getHeight() / 2);
        this.skillCooldown = 0;
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
        if(!isAction){
            
            if(currentCount < maxCount){
                rechargeTimer += dt;
                if(rechargeTimer > rechargeRate){
                    rechargeTimer = 0;
                    currentCount ++;
                    canCast = true;
                    skillCooldown = 0;
                    setCooldown = false;
                }
            }
            if(currentCount == 0 && !setCooldown){
                canCast = false;
                setCooldown = true;
                skillCooldown = rechargeRate - rechargeTimer;
            }
            if(canCast){
                if(cdl == null && baIcon != null){
                    cdl = new Label(Integer.toString( currentCount), BaseGame.labelStyle);
                    cdl.setPosition(baIcon.getX(), baIcon.getY());
                    BaseActor.getUiStage().addActor(cdl);
                }     
                else if(cdl != null)
                    cdl.setText(Integer.toString(currentCount));
            }
            
            
            return;
        }
            
        getBoundaryPolygon();
        accelerateAtAngle(direction);
        applyPhysics(dt);
        if(isAnimationFinished()){
            rootTimer += dt;
            if(rootTimer > rootDuration){
                trappedEnemy.setCanMove(true);
                remove();
            }
            return;
        }else{
            trapTimer += dt;
            if(trapTimer > trapDuration){
                remove();
            }
        }
        if(from == Skill.From.Enemy){
            for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                if(overlaps(player)&& !sprung){
                    sprung = true;
                    ((Player)player).takeDamage((int)damage);
                    if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                        skillHit.play(Options.soundVolume);
                    }
                    alreadyHit.add(player);
                    setAnimationPaused(false);
                }
            }
        }
        if(from == Skill.From.Player){
            for(BaseActor enemy: BaseActor.getList(this.getStage(), "com.dslayer.content.Enemy.BaseEnemy")){
                if(overlaps(enemy) && !sprung){
                    sprung = true;
                    ((BaseEnemy)enemy).takeDamage((int)damage, player);
                    if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                        skillHit.play(Options.soundVolume);
                    }
                    setAnimationPaused(false);
                    trappedEnemy = enemy;
                    enemy.setSpeed(0);
                    enemy.setCanMove(false);
                }
            }
        }
     }

    @Override
    public BaseActor cast(BaseActor caster, Vector2 target, Skill.From from) {
        float degrees = (float)(MathUtils.atan2((target.y - (caster.getY() + caster.getHeight()) )
                , target.x - (caster.getX() + caster.getWidth())) * 180.0d / Math.PI);
        
        BaseActor b = new BearTrap(caster.getX() + caster.getWidth() / 2,caster.getY() + caster.getHeight() / 2 , 
                BaseActor.getMainStage()).isProjectile()
                //.setProjectileSpeed(300)
                .setProjectileDeAcceleration(0)
                //.setProjectileRotation(degrees)
                //.setDirection(degrees)
                .setFrom(from);
                //canCast = false;
                b.setMotionAngle(degrees);
        if(from == Skill.From.Player){
            ((Skill)b).player = ((Player)caster);
        }
        
        currentCount--;
        if(currentCount == 0){
            canCast = false;
            skillCooldown = rechargeRate - rechargeTimer;
        }
        return b;
    }
    
    public BearTrap isProjectile(){
        isAction = true;
        loadAnimationFromSheet(BearTrap, 1, 4, .02f, false);
        setScale(1.5f* Options.aspectRatio);
        setAnimationPaused(true);
        //alreadyHit = new ArrayList<BaseActor>();
        setZIndex(1200);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setPosition(getX() - (getWidth() /2) , getY() - (getHeight()));
        setBoundaryPolygonWide(12);
        damage = 20;
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
    public boolean remove(){
        //if(trappedEnemy != null)
            //trappedEnemy.remove();
        
        return super.remove();
    }

    @Override
    public BaseActor cast(BaseActor arg0, BaseActor arg1, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
