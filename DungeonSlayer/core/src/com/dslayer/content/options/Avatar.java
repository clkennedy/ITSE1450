/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author ARustedKnight
 */
public class Avatar{
    
    private Animation<TextureRegion> anim;
    private float w;
    private float h;
    
    public Avatar(Animation<TextureRegion> animation){
        this.anim = animation;
    }
    
    public Avatar(String[] fileNames, float frameDuration, boolean loop){
        anim = Avatars.load(fileNames, frameDuration, loop);
    }
    
    public Avatar(String fileName){
        anim = Avatars.load(fileName);
    }
    public Avatar(String fileName,int rows, int cols, float frameDuration, boolean loop){
        anim = Avatars.load(fileName,rows, cols, frameDuration, loop );
    }
    
    public Avatar setSize(float w, float h){
        this.w = w;
        this.h = h;
        return this;
    }
    
    public Animation<TextureRegion> getAnim(){
        return this.anim;
    }
    
    public float getWidth(){
        return this.w;
    }
    public float getHeight(){
        return this.h;
    }
}
