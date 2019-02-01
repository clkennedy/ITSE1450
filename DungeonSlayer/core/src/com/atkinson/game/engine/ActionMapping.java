/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import java.util.ArrayList;

/**
 * Key Mapping Class for the<br>
 * Atkinson Game Engine<br><br>
 * Handles a Key/Value pairing to used in conjuction with the PlayerControls Class
 * @author Cameron Kennedy
 */
public class ActionMapping{
    
    private String _mappingName;
    //private boolean _comboMap = false;
    private ArrayList<Mapping> _keys;
    
    /**
 * Constructor
 * 
 * <p>
 * @param mapName the name of the mapping
 */
    public ActionMapping(String mapName){
        
        _keys = new ArrayList();
        _mappingName = mapName;
        
    }
    /**
    * add a keyboard to the mapping
    * 
    * <p>
    * @param key the key to add to the mapping
    */
    public void addKeyboardMapping(int key){
        _keys.add(new KeyboardMap(key));
    }
     /**
    * add a Mouse Button to the mapping
    * 
    * <p>
    * @param key the Mouse Button to add to the mapping
    */
    public void addMouseMapping(int key){
        _keys.add(new MouseMap(key));
    }
    
     /**
    * add a Keyboard Combo to the mapping<br>
    * (Shift + a)
    * 
    * <p>
    * Key1 or Key2 has to be either Left Shift, Left Alt, or Left control to work
    * or it will fail, these three will always be mapped to Key1, but you can 
    * enter it in either slot
    * 
    * @param key1 the first Keyboard Key to add to the mapping
    * @param key2 the second Keyboard Key to add to the mapping
    */
    public void addKeyBoardComboMapping(int key1,int key2){
        try{
            _keys.add(new KeyboardComboMap(key1, key2));
        }catch(Exception e){
            
        }
        
    }
    /**
    * returns the mapping name
    * 
    * <p>
    * @return the name of the mapping
    */
    public String getMapName(){
        return this._mappingName;
    }
    
    /**
    * returns all the key mappings in the mapping
    * 
    * <p>
    * @return all the keys in the mapping
    */
    public ArrayList<Mapping> getMappings(){
        return this._keys;
    }
    
    /**
    * returns Whether anyKey is Pressed in the mapping
    * 
    * <p>
    * @return true if any key in the mapping is pressed
    */
    public boolean isPressed(){
        boolean pressed = false;
        for(Mapping map : _keys){
            pressed = pressed || map.isPressed();
        }
        return pressed;
    }
    /**
    * returns Whether anyKey is Pressed in the mapping<br>
    * was just pressed.
    * <br><br>
    * useful for checking one clicks
    * 
    * <p>
    * @return true if any key in the mapping is pressed
    */
    public boolean isJustPressed(){
        boolean pressed = false;
        for(Mapping map : _keys){
            pressed = pressed || map.isJustPressed();
        }
        return pressed;
    }
    /**
    * returns if Key exists in any of the mappings
    * 
    * <p>
    * @return true if any key is Present in any Mapping
    */
    public boolean contains(int key){
        boolean contained = false;
        for(Mapping map : _keys){
            if(!(map instanceof KeyboardComboMap))
                contained = contained || map.contains(key);
        }
        return contained;
    }
    /**
    * returns if a mapping equals the other Mapping
    * <br>
    * More Useful when comparing KeyboardComboMap Comparisons
    * <p>
    * @return true if mapping keys are the same
    */
    public boolean equals(Mapping otherMap){
        boolean contained = false;
        for(Mapping map : _keys){
            if(!(map instanceof KeyboardComboMap))
                contained = contained || map.equals(otherMap);
        }
        return contained;
    }
    /**
    * Removes a Mapping for the this ActionMap
    * <p>
    * @param map the Mapping to remove for the list of mappings
    */
    public void remove (Mapping map){
        map.remove();
        this._keys.remove(map);
    }
    
}
/**
 * Mapping Class for the<br>
 * Atkinson Game Engine<br><br>
 * Handles Presses and other comparisons for different types of Key Maps
 * @author Cameron Kennedy
 */
abstract class Mapping{
    /**
    * returns if this Map is Currently Pressed
    * <p>
    * @return true if Map is currently Pressed
    */
    public abstract boolean isPressed();
    /**
    * returns if this Map was just Pressed
    * <p>
    * @return true if Map was just Pressed
    */
    public abstract boolean isJustPressed();
    /**
    * returns whether or not the key passed is in this Map
    * <p>
    * @param map the mapping to compare to
    * @return true if key is in the mapping
    */
    public abstract boolean contains(int key);
    /**
    * returns whether or not the Passed mapping has the same keys
    * <p>
    * @param map the mapping to compare to
    * @return true if the keys in both Mappings are the same
    * 
    */
    public abstract boolean equals(Mapping map);
    /**
    * returns the key in the Map
    * <p>
    * @return true if the keys in both Mappings are the same
    */
    public abstract int getMap();
    /**
    * Removes itself from the InputProcessor<br>
    * if it needs to be
    */
    public abstract void remove();
}

class KeyboardMap extends Mapping{
    
        int _key;
        
        public KeyboardMap(int key){
           super();
           this._key = key;
        }
    
        @Override
        public boolean isPressed(){
            return Gdx.input.isKeyPressed(this._key);
        }
        @Override
        public boolean isJustPressed(){
            return Gdx.input.isKeyJustPressed(this._key);
        }
        @Override
        public boolean contains(int key){
            return this._key == key;
        }
        @Override
        public boolean equals(Mapping map){
            if(!(map instanceof KeyboardMap))
                return false;
            return this._key == ((KeyboardMap)map).getMap();
        }
        @Override
        public int getMap(){
            return this._key;
        }
        @Override
        public void remove(){
        }
    
    }
    
    class KeyboardComboMap extends Mapping{
    
        int _key1;
        int _key2;
        
        public KeyboardComboMap(int key1, int key2) throws Exception{
            
            if((key2 == Input.Keys.SHIFT_LEFT || 
                    key2 == Input.Keys.ALT_LEFT ||
                    key2 == Input.Keys.CONTROL_LEFT) &&
                   (key1 != Input.Keys.SHIFT_LEFT || 
                    key1 != Input.Keys.ALT_LEFT ||
                    key1 != Input.Keys.CONTROL_LEFT)){
                
                this._key1 = key2;
                this._key2 = key1;
            }else if((key1 == Input.Keys.SHIFT_LEFT || 
                        key1 == Input.Keys.ALT_LEFT ||
                        key1 == Input.Keys.CONTROL_LEFT) &&
                       (key2 != Input.Keys.SHIFT_LEFT || 
                        key2 != Input.Keys.ALT_LEFT ||
                        key2 != Input.Keys.CONTROL_LEFT)){
                
                this._key1 = key1;
                this._key2 = key2;
            }else if(key1 != Input.Keys.SHIFT_LEFT || 
                    key1 != Input.Keys.ALT_LEFT ||
                    key1 != Input.Keys.CONTROL_LEFT ||
                    key2 != Input.Keys.SHIFT_LEFT || 
                    key2 != Input.Keys.ALT_LEFT ||
                    key2 != Input.Keys.CONTROL_LEFT){
                throw new Exception("One of the Keys has to be Left Shift, Left Control, or Left Alt Keys");
            }else{
                throw new Exception("Could not create a Combo Mapping");
            }
            
        }
    
        @Override
        public boolean isPressed(){
            return (Gdx.input.isKeyPressed(this._key1) && Gdx.input.isKeyPressed(this._key2));
        }
        @Override
        public boolean isJustPressed(){
            
            return (Gdx.input.isKeyPressed(this._key1) && Gdx.input.isKeyJustPressed(this._key2));
        }
        @Override
        public boolean contains(int key){
            return false;
        }
        
        public boolean contains(int key1, int key2){
            return ((this._key1 == key2 || 
                    this._key1 == key1) &&
                    (this._key2 == key2 || 
                    this._key2 == key1));
        }
        
        @Override
        public boolean equals(Mapping map){
            if(!(map instanceof KeyboardComboMap))
                return false;
            
            return (this._key1 == ((KeyboardComboMap)map).getMap()) && (this._key2 == ((KeyboardComboMap)map).getMap2());
            
        }
        @Override
        public int getMap(){
            return this._key1;
        }
        public int getMap2(){
            return this._key2;
        }
        @Override
        public void remove(){
        }
        
    }
    
    class MouseMap extends Mapping implements InputProcessor{
    
        boolean pressed = false;
        int _key;
        public MouseMap(int key){
            
            this._key = key;
            InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
            im.addProcessor(this);
        }
    
        @Override
        public boolean isPressed(){
            return Gdx.input.isButtonPressed(this._key);
        }
        @Override
        public boolean isJustPressed(){
            boolean justPressed =Gdx.input.isButtonPressed(this._key) && pressed;
                
            if(justPressed){
                pressed = !pressed;
            }
            
            return justPressed;
        }
        
        @Override
        public boolean contains(int key){
            return this._key == key;
        }
        
        @Override
        public boolean equals(Mapping map){
            if(!(map instanceof MouseMap))
                return false;
            
            return (this._key == ((MouseMap)map).getMap());
            
        }
        @Override
        public int getMap(){
            return this._key;
        }
        @Override
        public void remove(){
            InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
            im.removeProcessor(this);
        }
    @Override
    public boolean keyDown(int keycode) {
       return false;
    }

    @Override
    public boolean keyUp(int keycode) {
       return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(button == this._key && !pressed){
            pressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == this._key){
            pressed = false;
            return true;
        }
       return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
    
    }
