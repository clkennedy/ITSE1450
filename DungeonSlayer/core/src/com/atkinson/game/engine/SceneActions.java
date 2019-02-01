/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Align;

/**
 * Static Methods of actions to perform
 * @author Douglas Atkinson
 */
public class SceneActions extends Actions {
    
    /**
    * Sets the text of the action and returns it
    * 
    * <p>
    * calls new SetTextAction(s);
    * Used with Dialog Boxs
    * will throw an error if actor is not a dialog box
    * 
    * @return Action with the new text
    */
    public static Action setText(String s) {
        return new SetTextAction(s);
    }
    
    /**
    * Returns an Action that will pause an actor
    * 
    * <p>
    * @return Actions.forever( Actions.delay(1) )
    */
    public static Action pause() {
        return Actions.forever( Actions.delay(1) );
    }
    
    /**
    * Returns an Action that will move the actor to the bottom left
    * 
    * <p>
    * @param duration how long it will take in seconds
    * @return Action that move an actor to the bottom left of the screen
    */
    public static Action moveToScreenLeft(float duration) {
        return Actions.moveToAligned( 0, 0, Align.bottomLeft, duration );
    }
    
    /**
    * Returns an Action that will move the actor to the bottom right
    * 
    * <p>
    * @param duration how long it will take in seconds
    * @return Action that move an actor to the bottom right of the screen
    */
    public static Action moveToScreenRight(float duration) {
        return Actions.moveToAligned( BaseActor.getWorldBounds().width, 0, Align.bottomRight, duration );
    }
    
    /**
    * Returns an Action that will move the actor to the bottom center
    * 
    * <p>
    * @param duration how long it will take in seconds
    * @return Action that move an actor to the bottom center of the screen
    */
    public static Action moveToScreenCenter(float duration) {
        return Actions.moveToAligned( BaseActor.getWorldBounds().width / 2, 0, Align.bottom, duration);
    }
    /**
    * Returns an Action that will move the actor to outside of the screen in the bottom left corner
    * 
    * <p>
    * @param duration how long it will take in seconds
    * @return Action that moves an actor to the to outside of the screen in the bottom left corner
    */   
    public static Action moveToOutsideLeft(float duration) {
        return Actions.moveToAligned( 0, 0, Align.bottomRight, duration );
    }
    
    /**
    * Returns an Action that will move the actor to outside of the screen in the bottom right corner
    * 
    * <p>
    * @param duration how long it will take in seconds
    * @return Action that moves an actor to the to outside of the screen in the bottom right corner
    */   
    public static Action moveToOutsideRight(float duration) {
        return Actions.moveToAligned( BaseActor.getWorldBounds().width, 0, Align.bottomLeft, duration );
    }
}
