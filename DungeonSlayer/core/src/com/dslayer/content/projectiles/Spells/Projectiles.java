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
    private static final String IceNova = "particles/19_freezing_spritesheet.png";
    private static final String Slash = "particles/Slash.png";
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
    
    
}
