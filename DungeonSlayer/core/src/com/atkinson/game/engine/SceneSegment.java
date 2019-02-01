/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Takes an Actor and Applies an Action to it<br>
 * used with com.atkinson.game.engine.Scene<br>
 * @author Douglas Atkinson
 */
public class SceneSegment {
    
    private Actor actor;
    private Action action;
    
    public SceneSegment(Actor a1, Action a2) {
        actor = a1;
        action = a2;
    }
    
    public void start() {
        actor.clearActions();
        actor.addAction(action);
    }
    
    public boolean isFinished() {
        return (actor.getActions().size == 0);
    }
    
    public void finish() {
        // simulate 100000 seconds elapsed time to complete in-progress action
        if ( actor.hasActions() )
            actor.getActions().first().act(100000);
        // remove any remaining actions
        actor.clearActions();
    }
}
