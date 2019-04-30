/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Options;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;

/**
 *
 * @author ARustedKnight
 */
public abstract class Skill extends BaseActor{
    
    public enum From{Player, Enemy};
    protected From from;
    
    protected float direction;
    protected float damage;
    
    protected Texture icon;
    protected Texture iconCD;
    protected BaseActor baIcon;
    protected float iconSize = 30f;
    protected Label cdl;
    
    protected float skillCooldown = 2f;
    protected float skillCooldownTime = 0f;
    protected boolean casting;
    protected boolean canCast = true;
    
    protected boolean isAction = false;
    
    protected boolean isEnemy = false;
    
    public boolean trackCD = true;
    
    public Player player;
    
    protected boolean isCast = false;
    protected Sound skillSound;
    protected Sound skillHit;
    
    public Skill(){
        super();
    }
    
    public Skill(float x, float y, Stage s){
        super(x,y,s);
    }
    
    public Skill setFrom(Skill.From f){
        from = f;
        return this;
    }
    public Skill isEnemy(boolean b){
        isEnemy = b;
        return this;
    }
    
    
    public Skill setProjectileAcceleration(float speed){
        setAcceleration(speed);
        return this;
    }
    
    public Skill setProjectileSpeed(float speed){
        setSpeed(speed);
        return this;
    }
    
    public Skill setProjectileDeAcceleration(float speed){
        setDeceleration(speed);
        return this;
    }
    
    public void setupIcon(float x, float y){
        baIcon  = new BaseActor(x,y,BaseActor.getUiStage());
    }
    
    public void setCoolDown(float cd){
        skillCooldown = cd;
    }
    public Skill setDamage(float damage){
        this.damage = damage;
        return this;
    }
    
    public float getIconWidth(){
        if(baIcon == null)
            return 0;
        return baIcon.getWidth();
    }
    
    public Skill setProjectileRotation(float degrees){
        setRotation(getRotation() + degrees);
        return this;
    }
     
    public Skill setDirection(float degrees){
        direction = degrees;
        return this;
    }
    
    public void checkAttack(float dt){
        if((skillCooldownTime >= skillCooldown) || !trackCD){
            canCast = true;
            skillCooldownTime = 0;
        }
        if(!canCast){
            skillCooldownTime += dt;
        }
    }
    
    public void setIconSize(int s1){
        iconSize = s1;
        baIcon.setSize(iconSize, iconSize);
    }
    
    public void setIconPosition(int s1, int s2){
        baIcon.centerAtPosition(s1, s2);
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        if(isAction)
            return;
        checkAttack(dt);
        if(!canCast && !isEnemy && trackCD){
            loadCDTexture();
            if(cdl == null){
                cdl = new Label(Integer.toString( (int)skillCooldown - (int)skillCooldownTime), BaseGame.labelStyle);
                cdl.setPosition(baIcon.getX(), baIcon.getY());
                BaseActor.getUiStage().addActor(cdl);
            }     
            else
                cdl.setText(Integer.toString( (int)skillCooldown - (int)skillCooldownTime));
            
        }
        else if(!isEnemy && trackCD) {
            loadCastTexture();
            if(cdl != null){
                cdl.remove();
                cdl = null;
            }
                
        }
    }
    
    public boolean canCast(){
        return canCast;
    }
    
    @Override
    public boolean remove(){
        if(baIcon != null)
            baIcon.remove();
        if(icon != null)
            icon.dispose();
        if(iconCD != null)
            iconCD.dispose();
        
        return super.remove();
    }
    
    protected abstract void loadCDTexture();
    protected abstract void loadCastTexture();
    public abstract void cast(BaseActor caster, Vector2 target, Skill.From from);
}
