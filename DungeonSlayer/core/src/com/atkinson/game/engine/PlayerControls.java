/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import java.util.ArrayList;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
/**
 * Player Control System for the <br>
 * Atkinson Game Engine<br><br>
 * Handles Keyboard Mappings
 * @author Cameron Kennedy
 */
public class PlayerControls {
    
    ArrayList<ActionMapping> _keyMappings;
    
    /**
    * Constructor, instantiates an empty list of key mappings
    */
    public PlayerControls(){
        _keyMappings = new ArrayList();
    }
    
    /**
    * add a Mapping with a single Key
    * 
    * <p>
    * Test for duplicate mapping name (Case Insensitive)and
    * for the key in other mappings, and will fail
    * if either condition is true
    * 
    * @param mapName the name to give the mapping
    * @param key the input key on the keyboard to apply to the mapping
    */
    public void addMapping(String mapName, int key){
        //_keyMappings.add(new ActionMapping(mapName, new int[] {key}));
        
    }
    /**
    * add a Mapping with a list of Keys
    * 
    * <p>
    * Test for duplicate mapping name (Case Insensitive) and
    * for the key in other mappings, and will fail
    * if either condition is true
    * 
    * @param mapping the mapping to add
    */
    public void addMapping(ActionMapping mapping){
       if(exists(mapping.getMapName())){
           System.err.println(mapping.getMapName() + " Already Exists in KeyMappings");
           System.err.println("Quitting addMapping Method");
           return;
       }
       for(Mapping key : mapping.getMappings()){
           if(keyMapped(key)){
                System.err.println(key + " Already Exists in KeyMappings");
                System.err.println("Quitting addMapping Method");
                return;
           }     
        }
       
        _keyMappings.add(mapping);
        
        
    }
    /**
    * removes a Mapping
    * 
    * <p>
    * @param mapName the name of the mapping to remove
    */
    public void removeMapping(String mapName){
       ActionMapping mapping = get(mapName);
        if(mapping != null){
           this._keyMappings.remove(mapping);
       } 
       else{
           System.err.println(mapName + " Does not exist");
       }
        
    }
    
    /**
    * sets a list of default controls: up, down, left, right, jump
    * 
    * <p>
    * Default Values:<br>
    * up: W, up arrow<br>
    * down: S, down arrow<br>
    * left: A, left arrow<br>
    * Right: D, right arrow<br>
    * Jump: space bar<br>
    * Fire: Left Mouse Button
    */
    public void setDefaultControls(){
        //this.addMapping("Up", new int[]{Input.Keys.UP, Input.Keys.W});
        ActionMapping up = new ActionMapping("Up");
        up.addKeyboardMapping(Input.Keys.UP);
        up.addKeyboardMapping(Input.Keys.W);
        this.addMapping(up);
        //this.addMapping("Down", new int[]{Input.Keys.DOWN, Input.Keys.S});
        ActionMapping Down = new ActionMapping("Down");
        Down.addKeyboardMapping(Input.Keys.DOWN);
        Down.addKeyboardMapping(Input.Keys.S);
        this.addMapping(Down);
        //this.addMapping("Left", new int[]{Input.Keys.LEFT, Input.Keys.A});
        ActionMapping Left = new ActionMapping("Left");
        Left.addKeyboardMapping(Input.Keys.LEFT);
        Left.addKeyboardMapping(Input.Keys.A);
        this.addMapping(Left);
        //this.addMapping("Right", new int[]{Input.Keys.RIGHT, Input.Keys.D});
        ActionMapping Right = new ActionMapping("Right");
        Right.addKeyboardMapping(Input.Keys.RIGHT);
        Right.addKeyboardMapping(Input.Keys.D);
        this.addMapping(Right);
        //this.addMapping("Jump", new int[]{Input.Keys.SPACE});
        ActionMapping jump = new ActionMapping("jump");
        jump.addKeyboardMapping(Input.Keys.SPACE);
        this.addMapping(jump);
        
        ActionMapping fire = new ActionMapping("fire");
        fire.addMouseMapping(Input.Buttons.LEFT);
        this.addMapping(fire);
        
        ActionMapping menu = new ActionMapping("menu");
        menu.addKeyboardMapping(Input.Keys.ESCAPE);
        this.addMapping(menu);
    }
    
    /**
    * checks if the key is already mapped
    */
    private boolean keyMapped(Mapping map){
        boolean keyMapped = false;
        for(ActionMapping mapping : this._keyMappings){
            keyMapped = mapping.equals(map);
        }
        return keyMapped;
    }
    
    /**
    * checks if the name is already mapped
    */
    private boolean exists(String mapName){
        boolean contained = false;
        for(ActionMapping mapping : this._keyMappings){
            if(mapping.getMapName().toUpperCase().trim().equals( mapName.toUpperCase().trim())){
                contained = true;
                break;
            }
        }
        return contained;
    }
    
    /**
    * gets a key mapping
    */
    public ActionMapping get(String mapName){
        for(ActionMapping mapping : this._keyMappings){
            if(mapping.getMapName().toUpperCase().trim().equals( mapName.toUpperCase().trim())){
                return mapping;
            }
        }
        return null;
    }
    
    /**
    * clears all key mappings
    */
    public void clearMaps(){
        this._keyMappings.clear();
    }
    
    
    /**
    * Gets whether any key associated with a mapping is pressed down
    * 
    * <p>
    * @param mapName mapped name to look for
    * @return if any key in the mapping is pressed
    */
    public boolean isPressed(String mapName){
        for(ActionMapping mapping : _keyMappings){
            if(mapping.getMapName().toUpperCase().trim().equals(mapName.toUpperCase().trim())){
                return mapping.isPressed();
            }
        }
        return false;
    }
    
    /**
    * Gets whether any key associated with a mapping was just pressed<br>
    * useful for if you need to get a one time press of the key
    * 
    * <p>
    * @param mapName mapped name to look for
    * @return if any key in the mapping was just pressed
    */
    public boolean isJustPressed(String mapName){
        for(ActionMapping mapping : _keyMappings){
            if(mapping.getMapName().toUpperCase().trim().equals(mapName.toUpperCase().trim())){
                return mapping.isJustPressed();
            }
        }
        return false;
    }
}


    

