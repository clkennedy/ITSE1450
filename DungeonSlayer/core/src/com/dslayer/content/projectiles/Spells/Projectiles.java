/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.projectiles.Spells;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dslayer.content.options.Avatars;

/**
 *
 * @author cameron.kennedy
 */
public class Projectiles {
    
    private static final String FireBall = "particles/11_fire_spritesheet.png";
    public static Animation<TextureRegion> getFireBallAnim(){
        Animation<TextureRegion> anim = Avatars.load(FireBall, 8, 8, .05f, true);
        return anim;
    }
    
    
}
