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
    
    public static final String FireBall = "particles/11_fire_spritesheet.png";
    public static final String Arrow = "particles/Arrow.png";
    public static final String BearTrap = "Traps/Bear_Trap.png";
    public static final String IceNova = "particles/19_freezing_spritesheet.png";
    public static final String Slash = "particles/Slash.png";
    public static final String[] Shatter = {"particles/Shatter/Shatter0.png","particles/Shatter/Shatter1.png", "particles/Shatter/Shatter2.png",
    "particles/Shatter/Shatter3.png","particles/Shatter/Shatter4.png"};
    
    public static Animation<TextureRegion> getFireBallAnim(){
        Animation<TextureRegion> anim = Avatars.load(FireBall, 8, 8, .05f, true);
        return anim;
    }
    
    public static Animation<TextureRegion> getIceNovaAnim(){
        Animation<TextureRegion> anim = Avatars.loadWithTrim(IceNova, 10, 10, .05f, true, 14);
        return anim;
    }
    
    public static Animation<TextureRegion> getSlashAnim(){
        Animation<TextureRegion> anim = Avatars.load(Slash);
        return anim;
    }
    public static Animation<TextureRegion> getShatterAnim(){
        Animation<TextureRegion> anim = Avatars.load(Shatter, .1f, false);
        return anim;
    }
    
    
}
