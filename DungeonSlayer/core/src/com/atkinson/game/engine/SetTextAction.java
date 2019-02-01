/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 *  Action Class to change the Text of a Dialog Box<br>
 *  Updates the Text every frame
 * @author Douglas Atkinson
 */
public class SetTextAction extends Action {

    protected String textToDisplay;
    
    /**
 *  Action Class Constructor to change the Text of a Dialog Box<br>
 *  Updates the Text every frame
 * <p>
 * @param t text to change the dialog box to
 */
    public SetTextAction(String t) {  
        textToDisplay = t;
    }
     
    @Override
    public boolean act(float dt) {
        DialogBox db = (DialogBox)target;
        db.setText( textToDisplay );
        return true;
    }    
}
