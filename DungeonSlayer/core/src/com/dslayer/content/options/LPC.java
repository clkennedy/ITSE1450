/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class LPC {
    public enum LPCAnims{castUp,castLeft,castDown,castRight,
    thrustUp,thrustLeft,thrustDown,thrustRight,
    walkUp,walkLeft,walkDown,walkRight,
    slashUp,slashLeft,slashDown,slashRight,
    shootUp,shootLeft,shootDown,shootRight,
    die
    };
    
    public enum LPCGroupAnims{cast,
    thrust,
    walk,
    slash,
    shoot,
    die
    };
    public static List<Animation<TextureRegion>> LoadGroupFromFullSheet(Texture texture,LPCGroupAnims anim){
        return LoadGroupFromFullSheet(texture, anim, .1f);
    }
    
    public static List<Animation<TextureRegion>> LoadGroupFromFullSheet(Texture texture,LPCGroupAnims anim, float duration){
        //List<Animation<TextureRegion>> anims = Avatars.loadMulti(filename, 21, 13, .5f, true);
        List<Animation<TextureRegion>> rtnAnim = new ArrayList<Animation<TextureRegion>>();
        int trim=0;
        int start=0;
        int stop=0;
        int rows = 21, cols = 13;
        float frameDuration = duration;
        boolean loop = false;
        switch (anim) {
            case cast:
                trim = 13 - 7;
                start = 0;
                stop= start+4;
                break;
            case thrust:
                trim = 13 - 8;
                start = 4;
                stop= start+4;
                break;
            case walk:
                trim = 13 - 9;
                start = 8;
                stop= start+4;
                loop = true;
                break;
            case slash:
                trim = 13 - 6;
                start = 12;
                stop= start+4;
                break;
            case shoot:
                trim = 13 - 13;
                start = 16;
                stop= start+4;
                break;
            case die:
                trim = 13 - 6;
                start = 20;
                stop= start+1;
                break;
            default:
                throw new AssertionError();
        }
        
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        List<Animation<TextureRegion>> animList = new ArrayList<Animation<TextureRegion>>();
        Animation<TextureRegion> tempAnim = null;
        for(int r = start; r < stop; r++) {
            textureArray.clear();
            for(int c = 0; c < cols - trim; c++) {
                textureArray.add(temp[r][c]);
                tempAnim = new Animation<TextureRegion>(frameDuration, textureArray);
                if(loop) {
                    tempAnim.setPlayMode(Animation.PlayMode.LOOP);
                }
                else {
                    tempAnim.setPlayMode(Animation.PlayMode.NORMAL);
                }
            }
            animList.add(tempAnim);
        }
        return animList;
    }
    
    public static Animation<TextureRegion> LoadFromFullSheet(Texture filename,LPCAnims anim){
        List<Animation<TextureRegion>> anims = loadMulti(filename, 21, 13, .5f, true);
        Animation<TextureRegion> rtnAnim = anims.get(anim.ordinal());
        int trim=0;
        switch (anim) {
            case castUp:
            case castLeft:
            case castDown:
            case castRight:
                trim = 13 - 7;
                break;
            case thrustUp:
            case thrustLeft:
            case thrustDown:
            case thrustRight:
                break;
            case walkUp:
            case walkLeft:
            case walkDown:
            case walkRight:
                trim = 13 - 9;
                break;
            case slashUp:
            case slashLeft:
            case slashDown:
            case slashRight:
                trim = 13 - 6;
                break;
            case shootUp:
            case shootLeft:
            case shootDown:
            case shootRight:
                break;
            case die:
                break;
            default:
                throw new AssertionError();
        }
        
        TextureRegion[] trimAnim = rtnAnim.getKeyFrames();
        Array<TextureRegion> tArray =  new Array<TextureRegion>();
        for(int i = 0; i < trim; i++){
            tArray.add(trimAnim[i]);
        }
        
        rtnAnim = new Animation<TextureRegion>(.5f, tArray);
        return rtnAnim;
    }
    
    
    public static List<Animation<TextureRegion>> loadMulti(Texture texture, int rows, int cols, float frameDuration, boolean loop) {
        
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        List<Animation<TextureRegion>> animList = new ArrayList<Animation<TextureRegion>>();
        Animation<TextureRegion> anim = null;
        for(int r = 0; r < rows; r++) {
            textureArray.clear();
            for(int c = 0; c < cols; c++) {
                textureArray.add(temp[r][c]);
                anim = new Animation<TextureRegion>(frameDuration, textureArray);
                if(loop) {
                    anim.setPlayMode(Animation.PlayMode.LOOP);
                }
                else {
                    anim.setPlayMode(Animation.PlayMode.NORMAL);
                }
            }
            animList.add(anim);
        }
        return animList;        
    }
}
