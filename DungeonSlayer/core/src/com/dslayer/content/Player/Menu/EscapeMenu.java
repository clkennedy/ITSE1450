/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Player.Menu;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.atkinson.game.engine.Hover;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.screens.MainMenuScreen;

/**
 *
 * @author ARustedKnight
 */
public class EscapeMenu extends BaseActor{
    
    private EscapeMenu(){
        
    }
    
    private EscapeMenu(float x, float y, Stage s){
        
    }
    
    public EscapeMenu(Stage s){
         
        super( (s.getCamera().viewportWidth /2),(s.getCamera().viewportHeight /2), s);
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("HumbleFonts/compass/CompassPro.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 1f;
        BitmapFont fontmm = generator.generateFont(parameter); // font size 12 pixels
        
        generator.dispose();
        
        Label.LabelStyle mm = new Label.LabelStyle(fontmm, Color.BROWN);
        
        Label mainMenu = new Label("MainMenu", mm);
        mainMenu.setAlignment(Align.center);
        mainMenu.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenu();
            }
        
        });
        mainMenu.setPosition(-(mainMenu.getWidth()/2),0);
        
        setPosition((s.getCamera().viewportWidth /2) - (getWidth()/2),(s.getCamera().viewportHeight /2)- (getHeight()/2));
        this.addActor(mainMenu);
        setZIndex(1000);
        //s.addActor(this);
    }
    
    private void mainMenu(){
        BaseGame.setActiveScreen(new MainMenuScreen());
    }
}
