/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
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
public class IceNova extends Skill{
    
    private String ico = "Icons/IceNova.png";
    private String icoCD = "Icons/IceNovaCD.png";
    
    private float duration = 16;
    private float durationTimer = 0;
    
    private float tick = 1f;
    private float ticktimer = 2;
    
    private float radius = 15;
    
    private BaseActor caster;
    
    public IceNova(){
        super();
        skillCooldown = 27f;
        setup();
    }
    
    public IceNova(float x, float y, Stage s){
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
        this.centerAtActor(caster);
        getBoundaryPolygon();
        setRotation( (getRotation() + 1 > 350)? 0 : getRotation() + 1);
        ticktimer += dt;
        if(ticktimer > tick){
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
            ticktimer = 0;
        }
        durationTimer += dt;
        if(durationTimer > duration){
            remove();
        }
        
     }
    
    @Override
    public void cast(BaseActor caster, Vector2 target, Skill.From from) {
        //float degrees = (float)(MathUtils.atan2((target.y - caster.y ), target.x - caster.x) * 180.0d / Math.PI);
                Skill b = new IceNova(caster.getX() ,caster.getY() , BaseActor.getMainStage()).isProjectile()
                        .setFrom(from);
                  ((IceNova)b).setCaster(caster);
                canCast = false;
                ((IceNova)b).caster = caster;
                if(from == Skill.From.Player){
                    ((Skill)b).player = ((Player)caster);
                }
        }
    
    public IceNova isProjectile(){
        isAction = true;
        setAnimation(Projectiles.getIceNovaAnim());
        setSize(400,400);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygon(12);
        setRotation(MathUtils.random(360));
        setZIndex(1500);
        damage = 20;
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
