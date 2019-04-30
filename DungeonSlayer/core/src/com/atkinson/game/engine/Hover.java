/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dslayer.content.options.Options;

/**
 *
 * @author cameron.kennedy
 */
public class Hover extends ClickListener{
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
        Sound s = Gdx.audio.newSound(Gdx.files.internal("Sounds/Click2.wav"));
        s.play(Options.soundVolume);
        if(event.getListenerActor() instanceof Label){
            ((Label)event.getListenerActor()).setFontScale(((Label)event.getListenerActor()).getFontScaleX() *  1.2f);
            event.getListenerActor().scaleBy(.4f);
        }else{
            event.getListenerActor().scaleBy(.4f);
        }
    }
    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
        
        if(event.getListenerActor() instanceof Label){
            ((Label)event.getListenerActor()).setFontScale(((Label)event.getListenerActor()).getFontScaleX() /  1.2f);
            event.getListenerActor().scaleBy(-.4f);
        }else{
            event.getListenerActor().scaleBy(-.4f);
        }
        //System.out.println("com.atkinson.game.Hover.enter()");
    }
}
