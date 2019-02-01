/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.dslayer.content.screens.LevelScreen;

/**
 *
 * @author ARustedKnight
 */
public class Difficulty {
    public static int STAR_INTERVAL = 4;
    public static float ENEMY_INTERVAL = 3.5f;
    public static int ENEMY_SPEED = 130;
    public static int CHICKEN_INTERVAL = 15;
    public static int CHICKEN_SPEED = 80;
    public static int LEVEL_SPEED = 100;
    public static int POWERUP_SPEED = 200;
    
    public static LevelScreen lScreen = null;
    
    public static int worldWidth = 800;
    public static int worldHeight = 600;
    
    public static String DIFFICULTY = "Default";
    
    public static void NORMAL(){
        DIFFICULTY = "Normal";
        STAR_INTERVAL = 4;
        ENEMY_INTERVAL = 3;

        worldWidth = 800;
        worldHeight = 600;
    }
    
    public static void newGame(){
        lScreen.enemySpawnInterval = ENEMY_INTERVAL  * (int)Options.aspectRatio;
        lScreen.enemySpeed = ENEMY_SPEED  * (int)Options.aspectRatio;
        
    }
    
    public static void Reset(){
        lScreen.enemySpawnInterval = ENEMY_INTERVAL  * (int)Options.aspectRatio;
        lScreen.enemySpeed = ENEMY_SPEED  * (int)Options.aspectRatio;
    }
    
    public static void passInLevelScreen(LevelScreen l){
        lScreen = l;
    }
}
