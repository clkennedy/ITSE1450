/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.gamemodes;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public abstract class GameMode {
   
    protected boolean gameOver;
    
    protected Player player;
    protected Stage mainStage;
    
    private static boolean musicPlaying;
    private static Music gameMusic;
    
    public GameMode(Stage s){
        mainStage = s;
    }
    public GameMode(){
        mainStage = BaseActor.getMainStage();
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public void updateMainStage(Stage s){
        mainStage = s;
    }
    public void updateMainStage(){
        mainStage = BaseActor.getMainStage();
    }
    
    public boolean isGameOver(){
        return gameOver;
    }
    
    public static void playMusic(String songName){
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/" + songName));
        gameMusic.setLooping(true);
        gameMusic.setVolume(Options.musicVolume);
        gameMusic.play();
        musicPlaying = true;
    }
    
    public static void playMusicOnce(String songName){
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/" + songName));
        gameMusic.setLooping(false);
        gameMusic.setVolume(Options.musicVolume);
        gameMusic.play();
        //musicPlaying = true;
    }
    
    public static void stopMusic(){
        if(musicPlaying){
            gameMusic.stop();
            musicPlaying = false;
        }
    }
    
    public abstract void setup();
    public abstract void update(float dt);
}
