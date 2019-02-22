/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.graphics.Texture;

/**
 *  The Base for Setting up  the Game for the <br>
 * Atkinson Game Engine
 * @author Douglas Atkinson
 */
public abstract class BaseGame extends Game {
    
    private static BaseGame game;
    
    public static LabelStyle labelStyle;
    public static TextButtonStyle textButtonStyle;
        
    /**
    * Constructor for BaseGame
    * Sets the static variable of game to this
    * <p>
    */
    public BaseGame() {
        game = this;
    }
    
    /**
    * Basic setup for the game<br>
    *
    * instantiates the input Multiplexer, and sets the InputProcessor for the game.
    * <p>
    */
    public void create() {
        // prepare for multiple classes/stages to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        
       
        
        // Text button style configuration
        //textButtonStyle = new TextButtonStyle();
        //Texture   buttonTex   = new Texture( Gdx.files.internal("button.png") );
        //NinePatch buttonPatch = new NinePatch(buttonTex, 24,24,24,24);
        //textButtonStyle.up    = new NinePatchDrawable( buttonPatch );
        //textButtonStyle.fontColor = Color.GRAY;
    }
    
    /**
        * Changes the active scene of the game<br>
        *
        * Sets the new scene on the stage
        * <p>
        * @param    s the new scene to display
        */
    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
        if(game.getScreen() != null){
            BaseScreen.toDispose.add(((BaseScreen)game.getScreen()));
        }
    }
}
