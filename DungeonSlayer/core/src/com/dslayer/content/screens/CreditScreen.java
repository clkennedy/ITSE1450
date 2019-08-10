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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import java.awt.Desktop.Action;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import sun.font.TrueTypeFont;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Font.FontLoader;
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
public class CreditScreen extends BaseScreen {
        
    Label mainmenu;
    Label credits;
    
    public void initialize()
    {
        credits = new Label("Credits\r\n"
                + "--Production of--\r\n"
                + "Bakari Games\r\n"
                + "--Developed By--\r\n"
                + "Cameron Kennedy\r\n"
                + "--Hero Design--\r\n"
                + "Carlos Fuentes\r\n"
                + "--Sounds--\r\n"
                + "Samantha Zapata\r\n"
                + "--Backing Track--\r\n"
                + "Samantha Zapata\r\n"
                + "--Core Game Design--\r\n"
                + "Cameron/Carlos/Samantha\r\n"
                + "--Extended Game Design--\r\n"
                + "Cameron Kennedy\r\n"
                + "--Additional Assets--\r\n"
                + "Characters - LPC Generator\r\n"
                + "FireBall - CodeManu\r\n"
                + "Phantom - Warren Clark\r\n"
                + "Shadow Hand - Cameron Kennedy\r\n"
                + "Ground Slam - Cameron Kennedy\r\n"
                + "Emote Bubbles - Game Dev Alliance\r\n"
                + "Golem - William.Thompsonj\r\n"
                + "Goblin - William.Thompsonj\r\n"
                + "Traps - Stealthix\r\n"
                + "Health Bars - Adwit Rahman\r\n"
                + "Hole Obstacles - vaious\r\n"
                + "Dungeon Tile Set - Piratepoots\r\n"
                + "Grass - pboop\r\n"
                + "\r\n"
                + "If you see your asset or know\r\n the author of an asset\r\n"
                + "That I did not list here please contact me\r\n"
                + "!Thank you for Playing!\r\n", FontLoader.buttonStyle);
        credits.setAlignment(Align.center);
        ScrollPane sp = new ScrollPane(credits);
        sp.setX(0);
        sp.setHeight(Gdx.graphics.getHeight() - 100);
        sp.setY(Gdx.graphics.getHeight() - sp.getHeight());
        sp.setWidth(Gdx.graphics.getWidth());
        
        mainStage.addActor(sp);
        
        mainmenu = new Label("Back to Main Menu", FontLoader.buttonStyle);
        mainmenu.setFontScale(1f * Options.aspectRatio);
        mainmenu.setSize((mainmenu.getWidth() / 3) * Options.aspectRatio,(mainmenu.getHeight() /3) * Options.aspectRatio);
        mainmenu.setOriginX(mainmenu.getWidth() / 2);
        mainmenu.setOriginY(mainmenu.getHeight()/ 2);
        mainmenu.setAlignment(Align.center);
        mainmenu.setPosition(Gdx.graphics.getWidth() / 2,50);
        mainmenu.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.getListenerActor().remove();
                Progress.Save();
                BaseGame.setActiveScreen( new MainMenuScreen());
            }
        
        });
        mainStage.addActor(mainmenu);
    }
    
    
    public void update(float dt) {
        
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
        
        super.dispose();
    }
    
}