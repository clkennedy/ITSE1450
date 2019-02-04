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

/**
 *
 * @author cameron.kennedy
 */
public class Avatars {
    
    //player avatars
    final static public String DefaultPlayerUP = "Player\\Deafult Player\\Character_Up.png";
    final static public String DefaultPlayerDown = "Player\\Deafult Player\\Character_Down.png";
    final static public String DefaultPlayerLeft = "Player\\Deafult Player\\Character_Left.png";
    final static public String DefaultPlayerRight = "Player\\Deafult Player\\Character_Right.png";
    
    
    public static Animation<TextureRegion> load(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return load(fileNames, 1, true);
    }
    public static Animation<TextureRegion> load(String fileName, int rows, int cols, float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                textureArray.add(temp[r][c]);
            }
        }
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);        
        if(loop) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        else {
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        }
        
        return anim;        
    }
    public static Animation<TextureRegion> load(String[] fileNames, float frameDuration, boolean loop) {
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        for(int n = 0; n < fileCount; n++) {
            String fileName = fileNames[n];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }
        
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        if(loop) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        else {
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        }
        
        return anim;
    }
}


