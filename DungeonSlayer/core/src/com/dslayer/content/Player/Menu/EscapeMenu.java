/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Player.Menu;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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
        mainMenu.setPosition(200, 200);
        this.addActor(mainMenu);
        s.addActor(this);
        
        this.setVisible(false);
        
    }
    
    public void Show(){
        this.setVisible(true);
    }
    public void Hide(){
        this.setVisible(false);
    }
}
