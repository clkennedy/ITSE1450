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
    final static private String[] greenPlane = {"planeGreen0.png", "planeGreen1.png", "planeGreen2.png", "planeGreen1.png"};
    final static private String[] blueBird = {"BlueBird\\frame-1.png", "BlueBird\\frame-2.png", "BlueBird\\frame-3.png", "BlueBird\\frame-4.png"
    , "BlueBird\\frame-5.png", "BlueBird\\frame-6.png", "BlueBird\\frame-7.png", "BlueBird\\frame-8.png"};
    final static private String[] nyancat = {"NyanCat\\costume1.png", "NyanCat\\costume2.png", 
        "NyanCat\\costume3.png", "NyanCat\\costume4.png", "NyanCat\\costume5.png"};
    final static private String dragon = "dragon.png";
    final static private String ufo = "ufo.png";
    
    final static public Avatar greenPlaneAvatar = new Avatar(greenPlane, .1f, true).setSize(88, 73);
    final static public Avatar blueBirdAvatar = new Avatar(blueBird, .1f, true).setSize(88, 73);
    final static public Avatar nyancatAvatar = new Avatar(nyancat, .1f, true).setSize(88, 73);
    final static public Avatar dragonAvatar = new Avatar(dragon,4,5, .02f, true).setSize(100, 150);
    final static public Avatar ufoAvatar = new Avatar(ufo,4,4, .05f, true).setSize(70, 70);
    
    
    //enemy avatars
    final static private String[] redPlane = {"planeRed0.png", "planeRed1.png", "planeRed2.png", "planeRed1.png"};
    final static private String[] redBee = {"Bee\\frame-1.png", "Bee\\frame-2.png"};
    final static private String[] crow = {"crow\\crow1.png", "crow\\crow2.png", "crow\\crow3.png", "crow\\crow4.png", "crow\\crow5.png", "crow\\crow6.png"};
    final static private String fireskull = "fire-skull.png";
    final static private String demon = "demon.png";
    
    final static public Avatar redPlaneAvatar = new Avatar(redPlane, .1f, true).setSize(88, 73);
    final static public Avatar redBeeAvatar = new Avatar(redBee, .1f, true).setSize(88, 73);
    final static public Avatar crowAvatar = new Avatar(crow, .15f, true).setSize(90, 90);
    final static public Avatar fireskullAvatar = new Avatar(fireskull,1,8, .1f, true).setSize(90, 90);
    final static public Avatar demonAvatar = new Avatar(demon,1,6, .1f, true).setSize(120, 120);
    
    //collectables
    final static private String star = "star.png";
    final static private String bronzecoin = "bronze_coin.png";
    final static private String bluegem = "bluegem.png";
    final static private String[] goldcoin = {"goldcoin\\goldcoin0000.png","goldcoin\\goldcoin0001.png","goldcoin\\goldcoin0002.png"
    ,"goldcoin\\goldcoin0003.png","goldcoin\\goldcoin0004.png","goldcoin\\goldcoin0005.png"};
    final static private String orangegem = "orangegem.png";
    final static private String goldring = "goldRingSpriteSheet.png";
            
    final static public Avatar starAvatar = new Avatar(star).setSize(30, 30);
    final static public Avatar bronzecoinAvatar = new Avatar(bronzecoin).setSize(30, 30);
    final static public Avatar bluegemAvatar = new Avatar(bluegem).setSize(30, 30);
    final static public Avatar goldcoinAvatar = new Avatar(goldcoin, .1f, true).setSize(30, 30);
    final static public Avatar orangegemAvatar = new Avatar(orangegem,1,8, .1f, true).setSize(30, 30);
    final static public Avatar goldringAvatar = new Avatar(goldring,1,6, .1f, true).setSize(30, 30);
    
    //bullets
    final static private String shortBullet = "bullet.png";
    final static private String sword = "sword.png";
    final static private String[] rocket = {"rockets\\1.png","rockets\\2.png","rockets\\3.png","rockets\\4.png","rockets\\3.png","rockets\\2.png"};
    final static private String[] axespin = {"axespin\\axespin0000.png","axespin\\axespin0001.png","axespin\\axespin0002.png",
        "axespin\\axespin0003.png","axespin\\axespin0004.png","axespin\\axespin0005.png","axespin\\axespin0006.png","axespin\\axespin0007.png"};
    final static private String fireball = "fireball.png";
            
    final static public Avatar shortBulletAvatar = new Avatar(shortBullet).setSize(30, 30);
    final static public Avatar rocketAvatar = new Avatar(rocket, .1f, true).setSize(30, 30);
    final static public Avatar axespinAvatar = new Avatar(axespin, .1f, true).setSize(30, 30);
    final static public Avatar swordAvatar = new Avatar(sword).setSize(30, 30);
    final static public Avatar fireballAvatar = new Avatar(fireball,1,8, .1f, true).setSize(30, 30);
    
    //killables
    final static private String chicken = "FlyingChickenLeft.png";
    final static public Avatar chickenAvatar = new Avatar(chicken, 5, 5, .1f, true).setSize(90, 90);
    
    //miscallanous anims
     final static private String[] bubblePop = {"bubblepopone\\bubble_pop_frame_01.png", "bubblepopone\\bubble_pop_frame_02.png", "bubblepopone\\bubble_pop_frame_03.png", 
        "bubblepopone\\bubble_pop_frame_04.png", "bubblepopone\\bubble_pop_frame_05.png", "bubblepopone\\bubble_pop_frame_06.png", "bubblepopone\\bubble_pop_frame_07.png"};
    final static private String sparkle = "sparkle.png";
    final static private String explosion = "explosion.png";
    final static private String splatter = "bloodSpatter.png";
     
     final static public Avatar bubblePopAnim = new Avatar(bubblePop, .1f, false).setSize(100, 100);
     final static public Avatar sparkleAnim = new Avatar(sparkle, 8, 8, .02f, false).setSize(100, 100);
     final static public Avatar explosionAnim = new Avatar(explosion, 6, 6, .02f, false).setSize(100, 100);
     final static public Avatar splatterAnim = new Avatar(splatter, 3, 3, .1f, false).setSize(100, 100);
    
    
    
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

class Avatar{
    
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
