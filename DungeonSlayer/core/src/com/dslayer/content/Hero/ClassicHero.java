/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Hero;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;

/**
 *
 * @author ARustedKnight
 */
public class ClassicHero extends Hero{
    final static public String DefaultPlayerUP = "Player\\Deafult Player\\Character_Up.png";
    final static public String DefaultPlayerDown = "Player\\Deafult Player\\Character_Down.png";
    final static public String DefaultPlayerLeft = "Player\\Deafult Player\\Character_Left.png";
    final static public String DefaultPlayerRight = "Player\\Deafult Player\\Character_Right.png";

    public ClassicHero(Animation<TextureRegion> animation) {
        super(animation);
    }
    
    public ClassicHero(){
        super();
        Down = Avatars.load(DefaultPlayerDown, 1, 4, .2f, true);
        Up = Avatars.load(DefaultPlayerUP, 1, 4, .2f, true);
        Left = Avatars.load(DefaultPlayerLeft, 1, 4, .2f, true);
        Right = Avatars.load(DefaultPlayerRight, 1, 4, .2f, true);
        setAnimation(Right);
        setBoundaryRectangle();
        heroName = "Classic Hero";
    }

    @Override
    public void attack(float MouseWorldX,float MouseWorldY, Player player ) {
        System.out.println("Boo");
        float degrees = (float)Math.toDegrees( MathUtils.atan2((MouseWorldY - player.getY()), MouseWorldX - player.getX()));
                new ProjectileSpell(player.getX() - player.getWidth() ,player.getY() - player.getHeight() , BaseActor.getMainStage())
                        .fireBall()
                        .setProjectileSpeed(300).
                        setProjectileRotation(degrees).
                        setDirection(degrees)
                        .setFrom(ProjectileSpell.From.Player);
                canAttack = false;
        }
    
    
}
