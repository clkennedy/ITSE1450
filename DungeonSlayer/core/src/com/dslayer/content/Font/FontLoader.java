/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 *
 * @author ARustedKnight
 */
public class FontLoader {
    
    public static Label.LabelStyle titleStyle;
    public static Label.LabelStyle menuStyle;
    public static Label.LabelStyle buttonStyle;
    public static Label.LabelStyle pointStyle;
    
    private static BitmapFont fontPoint;
    private static BitmapFont fontTitle;
    private static BitmapFont fontButton;
    private static BitmapFont fontMenu;
    
    public static void Init(){
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("HumbleFonts/compass/CompassPro.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = Color.BROWN;
        parameter.borderColor = Color.GOLD;
        parameter.borderWidth = 1f;
        fontTitle = generator.generateFont(parameter);
        parameter.size = 80;
        fontMenu = generator.generateFont(parameter);
        parameter.borderColor = Color.GOLD;
        parameter.borderWidth = 1f;
        parameter.size = 40;
        fontButton = generator.generateFont(parameter);
        parameter.borderColor = Color.GOLD;
        parameter.borderWidth = 0f;
        parameter.color = Color.WHITE;
        parameter.size = 15;
        fontPoint = generator.generateFont(parameter);
        generator.dispose();
        
        titleStyle = new Label.LabelStyle();
        titleStyle.font = fontTitle;
        menuStyle = new Label.LabelStyle();
        menuStyle.font = fontMenu;
        buttonStyle = new Label.LabelStyle();
        buttonStyle.font = fontButton;
        pointStyle = new Label.LabelStyle();
        pointStyle.font = fontPoint;
    }
    
    public static void dispose(){
        if(fontButton != null)
            fontButton.dispose();
        if(fontMenu !=  null)
            fontMenu.dispose();
        if(fontPoint != null)
            fontPoint.dispose();
        if(fontTitle != null)
            fontTitle.dispose();
    }
    
}
