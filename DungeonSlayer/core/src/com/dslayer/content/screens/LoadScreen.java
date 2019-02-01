/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.screens;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.atkinson.game.engine.BaseScreen;

/**
 *
 * @author A1001672
 */
public class LoadScreen extends BaseScreen{
    
    public static boolean loaded = false;

    @Override
    public void initialize() {
        if(!loaded){
            BaseActor.setMainStage(mainStage);
            LoadScreen.loaded = true;
            //Options.setDisplay();
            
        }
    }

    @Override
    public void update(float dt) {
        BaseGame.setActiveScreen(new MainMenuScreen());
    }
}
