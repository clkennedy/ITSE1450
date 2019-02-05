/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.gamemodes;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Player.Player;

/**
 *
 * @author ARustedKnight
 */
public abstract class GameMode {
   
    protected boolean gameOver;
    
    protected Player player;
    protected Stage mainStage;
    
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
    
    public abstract void setup();
    public abstract void update(float dt);
}
