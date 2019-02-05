/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    Label cdl;
    
    protected float skillCooldown = 2f;
    protected float skillCooldownTime = 0f;
    protected boolean casting;
    protected boolean canCast = true;
    
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
    
    public Skill setProjectileSpeed(float speed){
        setAcceleration(speed);
        return this;
    }
    
    public void setupIcon(float x, float y){
        baIcon  = new BaseActor(x,y,BaseActor.getUiStage());
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
        if(skillCooldownTime >= skillCooldown){
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
        checkAttack(dt);
        
        if(!canCast){
            loadCDTexture();
            if(cdl == null){
                cdl = new Label(Integer.toString( (int)skillCooldown - (int)skillCooldownTime), BaseGame.labelStyle);
                cdl.setPosition(baIcon.getX(), baIcon.getY());
                BaseActor.getUiStage().addActor(cdl);
            }     
            else
                cdl.setText(Integer.toString( (int)skillCooldown - (int)skillCooldownTime));
            
        }
        else{
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
    
    protected abstract void loadCDTexture();
    protected abstract void loadCastTexture();
    public abstract void cast(Vector2 caster, Vector2 target, ProjectileSpell.From from);
}
