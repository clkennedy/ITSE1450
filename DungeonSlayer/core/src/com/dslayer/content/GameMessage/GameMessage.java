/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.GameMessage;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.dslayer.content.Font.FontLoader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class GameMessage extends BaseActor{
    
    private Label message;
            
    private float duration = 3f;
    private float durationTimer = 0f;
    
    private List<String> messages;
    public GameMessage(){
        messages = new ArrayList();
    }
    
    public void AddMessage(String m){
        messages.add(m);
    }
    
    public boolean isEmpty(){
        return messages.isEmpty() && message == null;
    }
    
    public void act(float dt){
        super.act(dt);
        
        if(messages.size() == 0 && message == null){
            return;
        }else if(messages.size() > 0 && message == null){
            
            message = new Label(messages.remove(0), FontLoader.buttonStyle);
            message.setPosition((BaseActor.getMainStage().getWidth()/ 2) - (message.getWidth()/2),
                    BaseActor.getMainStage().getHeight()); 
            message.addAction(Actions.moveBy(0, - 100, 1f));
            BaseActor.getUiStage().addActor(message);
        }
        
        if(message != null){
            durationTimer += dt;
        }
        if(durationTimer > duration){
            message.addAction(Actions.after(Actions.fadeOut(1f)));
            message.addAction(Actions.after(Actions.removeActor()));
            message = null;
            durationTimer = 0;
        }
    }
    
    
    @Override
    public boolean remove(){
        
        return super.remove();
    }
}
