/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Hero;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.options.Avatar;
import com.dslayer.content.options.Options;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author ARustedKnight
 */
public abstract class Hero extends Avatar{
    
    protected float attackCooldown = 2f;
    protected float attackCooldownTime = 0f;
    protected boolean attacking;
    protected boolean canAttack = true;
    
    protected Skill basicSkill;
    protected Skill altSkill;
    
    protected boolean waitToCast = false;
    protected Skill skillWaitedToCast;
    protected BaseActor caster;
    protected Vector2 target;
    
    protected float size = 32 * Options.aspectRatio;
    
    public static enum heros{ClassicHero, VikingHero, ArcherHero}
    protected String heroName;
    
    protected List<Animation<TextureRegion>> walkAnimList;
    protected List<Animation<TextureRegion>> castAnimList;
    protected List<Animation<TextureRegion>> castBasicAnimList;
    protected List<Animation<TextureRegion>> castAltAnimList;
    protected Animation<TextureRegion> dieAnim;
    
    protected boolean isDying;
    protected boolean isAttacking;
    
    public static Hero getNewHero(Hero.heros hero){
        switch (hero) {
            case ClassicHero:
               return new ClassicHero();
            case VikingHero:
                return new VikingHero();
            case ArcherHero:
                return new ArcherHero();
            default:
                throw new AssertionError();
        }
    }
    
    public Hero(Animation<TextureRegion> animation) {
        super(animation);
    }
    public Hero() {
        super();
        setOrigin(getWidth() /2, getHeight() / 2);
    }
    
    public float getDSize(){
        return this.size;
    }
    
    public String getName(){
        return heroName;
    }
    public Animation<TextureRegion> playRight(){
        
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(isAttacking){
                isAttacking = false;
                return castAnimList.get(3);
            }
            else
                return walkAnimList.get(3);  
        }
            
        return this.Right;
    }
    public Animation<TextureRegion> playLeft(){
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(isAttacking){
                isAttacking = false;
                return castAnimList.get(1);
            }
            else
                return walkAnimList.get(1);  
        }
        return this.Left;
    }
    public Animation<TextureRegion> playUp(){
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(isAttacking){
                isAttacking = false;
                return castAnimList.get(0);
            }
            else
                return walkAnimList.get(0);  
        }
        return this.Up;
    }
    public Animation<TextureRegion> playDown(){
        if(walkAnimList != null && walkAnimList.size() > 0){
            if(isAttacking){
                isAttacking = false;
                return castAnimList.get(2);
            }
            else
                return walkAnimList.get(2);  
        }
        return this.Down;
    }
    
    public Animation<TextureRegion> playDie(){
        if(dieAnim != null)
            return dieAnim;
        return null;
    }
    
    public boolean isDying(){
        return isDying;
    }
    public boolean isAttacking(){
        return isAttacking;
    }
    public void isDying(boolean b){
        isDying = b;
    }
    
    public void trackCD(boolean b){
        basicSkill.trackCD = b;
        altSkill.trackCD = b;
    }
    
    public abstract void attack(float MouseWorldX,float MouseWorldY, Player player );
    public abstract void altAttack(float MouseWorldX,float MouseWorldY, Player player );
    public abstract void setup(Player player);
    
    public boolean canBasicAttack(){
        return basicSkill.canCast();
    }
    public boolean canAltAttack(){
        return altSkill.canCast();
    }
    
    public void checkAttack(float dt){
        if(attackCooldownTime >= attackCooldown){
            canAttack = true;
            attackCooldownTime = 0;
        }
        if(!canAttack){
            attackCooldownTime += dt;
        }
    }
    
    @Override
    public void act(float dt){
       super.act(dt);
       //System.out.println("Boo");
       if(waitToCast){
           if(caster.isAnimationFinished()){
               skillWaitedToCast.cast(caster, target, Skill.From.Player);
               caster = null;
               target = null;
               skillWaitedToCast = null;
               waitToCast = false;
           }
        }
    }
    
    public void removeSkills(){
        basicSkill.remove();
        altSkill.remove();
    }
}
    
