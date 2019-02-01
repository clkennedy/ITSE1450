/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

/**
 *  The DialogBox Object in the <br>
 *  Atkinson Game Engine
 * @author Douglas Atkinson
 */
public class DialogBox extends BaseActor {
    
    private Label dialogLabel;
    private float padding = 16;
    
    
    /**
    * Constructor for a DialogBox<br>
    *
    * <p>
    * loads texture "dialog-translucent"
    * adds itself to the Provided Stage
    * @param    x Spawn x location in pixels from the bottom left of the scene
    * @param    y Spawn y location in pixels from the bottom left of the scene
    * @param    s stage that the actor will attach to
    */
    public DialogBox(float x, float y, Stage s) {
        super(x, y, s);
        
        loadTexture("dialog-translucent.png");
        dialogLabel = new Label(" ", BaseGame.labelStyle);
        dialogLabel.setWrap(true);
        dialogLabel.setAlignment( Align.topLeft );
        dialogLabel.setPosition( padding, padding );
        this.setDialogSize( getWidth(), getHeight() );
        this.addActor(dialogLabel);
    }
    
    /**
    * sets the Size of the Dialog Box<br>
    *
    * <p>
    * calls setSize(width, height)
    * @param    width Width in Pixels of the Dialog Box
    * @param    height height in Pixels of the Dialog Box
    */
    public void setDialogSize(float width, float height) {
        this.setSize(width, height);
        dialogLabel.setWidth( width - 2 * padding );
        dialogLabel.setHeight( height - 2 * padding );
    }
    
    /**
    * sets the Text of the Dialog Box<br>
    *
    * <p>
    * calls setText(width, height)
    * @param    text text to change the Dialog Box to
    */
    public void setText(String text) {
        dialogLabel.setText(text);  
    }
    
    /**
    * sets the scale of the font of the Dialog Box<br>
    *
    * <p>
    * calls setFontScale(scale)
    * @param    scale size of the font
    */
    public void setFontScale(float scale) {  
        dialogLabel.setFontScale(scale);  
    }
    
    /**
    * sets the color of the font of the Dialog Box<br>
    *
    * <p>
    * calls setColor(color)
    * @param    color color of the font
    */
    public void setFontColor(Color color) {
        dialogLabel.setColor(color);  
    }
    
    /**
    * sets the background color of the font of the Dialog Box<br>
    *
    * <p>
    * calls setColor(color)
    * @param    color color of the background
    */
    public void setBackgroundColor(Color color) {
        this.setColor(color);  
    }
    
    /**
    * Aligns all the text within the label to the top left<br>
    *
    * <p>
    * setAlignment( Align.topLeft )
    */
    public void alignTopLeft() {
        dialogLabel.setAlignment( Align.topLeft );  
    }
    
    /**
    * Aligns all the text within the label to the Center<br>
    *
    * <p>
    * setAlignment( Align.topLeft )
    */
    public void alignCenter() {
        dialogLabel.setAlignment( Align.center );  
    }
    
}
