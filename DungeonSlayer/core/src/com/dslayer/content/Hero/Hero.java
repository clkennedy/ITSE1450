/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Avatar;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public abstract class Hero extends Avatar{
    
    protected float attackCooldown = 2f;
    protected float attackCooldownTime = 0f;
    protected boolean attacking;
    protected boolean canAttack = true;
    
    public static Hero[] heros = {new ClassicHero()};
    protected String heroName;
    
    public Hero(Animation<TextureRegion> animation) {
        super(animation);
    }
    public Hero() {
        super();
    }
    
    public String getName(){
        return heroName;
    }
    public Animation<TextureRegion> playRight(){
        return this.Right;
    }
    public Animation<TextureRegion> playLeft(){
        return this.Left;
    }
    public Animation<TextureRegion> playUp(){
        return this.Up;
    }
    public Animation<TextureRegion> playDown(){
        return this.Down;
    }
    
    public abstract void attack(float MouseWorldX,float MouseWorldY, Player player );
    
    public boolean canAttack(){
        return canAttack;
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
}
