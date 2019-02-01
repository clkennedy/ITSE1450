/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * The Sign Object(Sign Post) Player Readable Object in the 
 * Atkinson Game Engine
 * <p>
 * 
 * @author Douglas Atkinson
 */
public class Sign extends BaseActor {
    
    // the text to be displayed
    private String text;
    // used to determine if sign text is currently being displayed
    private boolean viewing;
    
    /**
    * Constructor of the Sign Object
    * 
    * <p>
    * @param    x Spawn x location in pixels from the bottom left of the scene
    * @param    y Spawn y location in pixels from the bottom left of the scene
    * @param    s stage that the actor will attach to
    */
    public Sign(float x, float y, Stage s) {
        super(x, y, s);
        
        loadTexture("sign.png");
        text = " ";
        viewing = false;
    }
    
    /**
    * Sets the Text on the Sign
    * 
    * <p>
    * @param    t Text that Will Display when the sign is read
    */
    public void setText(String t) {
        text = t;  
    }
    
     /**
    * Returns the Text on the Sign
    * 
    * <p>
    * @return the Text on the sign
    */
    public String getText() {
        return text;  
    }
     /**
    * Sets whether the sign is being viewed or not
    * 
    * <p>
    * @param v true if the sign is being viewed, else false
    */
    public void setViewing(boolean v) {
        viewing = v;  
    }
     /**
    * Sets whether the sign is being viewed or not
    * 
    * <p>
    * @return true if the sign is being viewed, else false
    */
    public boolean isViewing() {
        return viewing;  
    }  
    
}
