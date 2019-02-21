/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.screens;

import com.dslayer.content.options.*;
import com.atkinson.game.engine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.dslayer.content.Rooms.Dungeon.DungeonPanels;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import java.awt.Desktop.Action;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import sun.font.TrueTypeFont;
import com.badlogic.gdx.utils.Align;
import com.dslayer.gamemodes.SurvivalGameMode;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import static java.lang.System.gc;
//import org.json.JSONObject;

/**
 *
 * @author douglas.atkinson
 */
public class LogoScreen extends BaseScreen {
    
    BaseActor Logo;
    
    float displayTime = 6f;
    float timer = 0f;
    
    public void initialize()
    {
       Logo = new BaseActor(0,0, mainStage);
       Logo.loadTexture("Logos/BakariLogo_transparent.png");
       Logo.setOrigin(Logo.getWidth() / 2, Logo.getHeight() / 2);
       Logo.setBoundaryRectangle();
       Logo.centerAtPosition(Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight()/ 2);
       Logo.setOpacity(0);
       Logo.addAction(Actions.fadeIn(2f));
       
       mainStage.addActor(Logo);
    }
    
    
    public void update(float dt) {
        timer += dt;
        if (timer > displayTime){
            toMainMenu();
        }
    }
    
    private void toMainMenu(){
        this.dispose();
          BaseGame.setActiveScreen(new MainMenuScreen());
    }
   
    public boolean keyDown(int keyCode) {
      if(keyCode == Keys.ESCAPE){
          toMainMenu();
      }
        return true;
    }
    
    @Override
    public void dispose(){
        if(Logo != null)
            Logo.remove();
        
        super.dispose();
    }
    
}