/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.GameMessage;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class GameMessage extends BaseActor{
    
    private Label message;
    Label.LabelStyle mStyle;
            
    private float duration = 3f;
    private float durationTimer = 0f;
    
    private List<String> messages;
    
    public GameMessage(){
        messages = new ArrayList();
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("HumbleFonts/compass/CompassPro.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 1f;
        BitmapFont fontTitle = generator.generateFont(parameter); // font size 12 pixels
        
        generator.dispose();
        mStyle = new Label.LabelStyle(fontTitle, Color.BROWN);
    }
    
    public void AddMessage(String m){
        messages.add(m);
    }
    
    public void act(float dt){
        super.act(dt);
        
        if(messages.size() == 0 && message == null){
            return;
        }else if(messages.size() > 0){
            
            message = new Label(messages.remove(0), mStyle);
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
    
}
