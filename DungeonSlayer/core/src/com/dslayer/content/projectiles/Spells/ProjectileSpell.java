/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.projectiles.Spells;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Player;

/**
 *
 * @author cameron.kennedy
 */
public class ProjectileSpell extends BaseActor{
    
    private float direction;
    public enum From{Player, Enemy};
    
    public float damage;
    
    private From from;
    
    public ProjectileSpell(float x, float y, Stage s){
        super(x,y,s);
    }
    
    /*public ProjectileSpell fireBall(){
        //setAnimation(Projectiles.getFireBallAnim());
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygonHalfLong(12);
        setRotation(90);
        damage = 50;
        return this;
    }
    public ProjectileSpell setFrom(From f){
        from = f;
        return this;
    }
    
    public ProjectileSpell setDamage(float d){
        damage = d;
        return this;
    }
    
    public ProjectileSpell setProjectileSpeed(float speed){
        setAcceleration(speed);
        return this;
    }
    
    public ProjectileSpell setProjectileRotation(float degrees){
        setRotation(getRotation() + degrees);
        return this;
    }
     
    public ProjectileSpell setDirection(float degrees){
        direction = degrees;
        return this;
    }*/
}
