/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 *  The Base for Setting up Levels and Displays in the <br>
 * Atkinson Game Engine
 * @author Douglas Atkinson
 */
public abstract class BaseScreen implements Screen, InputProcessor {
    
    protected Stage mainStage;
    protected Stage uiStage;
    protected Table uiTable;
    
    private boolean paused = false;
    
    /**
    * Basic Screen setup for the game<br>
    *
    * instantiates the Main Stage, the UiStage, the uiTable and calls initialize in child classes
    * <p>
    */
    public BaseScreen() {
        mainStage = new Stage();
        uiStage = new Stage();
        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);
        initialize();
    }
    
    public void Pause(){
        this.paused = !paused;
    }
    
    /**
    * Used in child classes to initialize themselves
    */
    public abstract void initialize();
    
    /**
    * Used in child classes to render themselves
    */
    public abstract void update(float dt);
    
    /**
    * Renders the Screen<br><br>
    * 
    * calls update in child classes
    */
    public void render(float dt) {
        
        uiStage.act(dt);
        mainStage.act(dt);
        update(dt);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
        uiStage.draw();
    }
    
    // methods required by Screen interface
    @Override
    public void resize(int width, int height) {  }
    @Override
    public void pause()   {  }
    @Override
    public void resume()  {  }
    @Override
    public void dispose() {  }
    
    @Override
    public void show() {
        InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
        im.clear();
        im.addProcessor(this);
        im.addProcessor(uiStage);
        im.addProcessor(mainStage);
    }
    
    @Override
    public void hide() {
        InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
        im.removeProcessor(this);
        im.removeProcessor(uiStage);
        im.removeProcessor(mainStage);
    }
    
    // methods required by InputProcessor interface
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char c) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    
    public boolean isTouchDownEvent(Event e) {
        return (e instanceof InputEvent) && ((InputEvent)e).getType().equals(Type.touchDown);
    }
    
}
