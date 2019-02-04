/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Player;

import com.atkinson.game.engine.BaseActor;
import static com.atkinson.game.engine.BaseActor.getWorldBounds;
import com.atkinson.game.engine.PlayerControls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.dslayer.content.options.*;
import java.util.ArrayList;

/**
 *
 * @author ARustedKnight
 */
public class Player extends BaseActor{
    
    
    private float rotationSpeed = 15f;
    private PlayerControls _playerControls;
    
    private boolean canShoot = true;
    private float reloadInterval = .5f;
    private float reloadTime = 0;
    
    private boolean _grounded = false;
    private boolean canTripleShot = false;
    
    private float boostHeight = 400;
    
    private int lives = 3;
    private ArrayList<BaseActor> livesIcons;
    
    private boolean isMoving = false;
    private Vector2 resetCoords;
    
    private float accel = 150f;
    
    public Player(float x, float y, Stage s){
        super(x,y,s);
        
        this._playerControls = new PlayerControls();
        
        _playerControls.setDefaultControls();
        //_playerControls.removeMapping("Up");
        
        _playerControls.get("jump").addKeyboardMapping(Input.Keys.W);
        _playerControls.get("jump").addKeyboardMapping(Input.Keys.UP);
        
        _playerControls.get("fire").addKeyboardMapping(Input.Keys.F);
        _playerControls.get("fire").addKeyboardMapping(Input.Keys.SHIFT_RIGHT);
        
        resetCoords = new Vector2(x, y);
        
        this.setOrigin(this.getWidth() / 2, this.getHeight() /2);
        
       // setAnimation(Unlocks.currentAvatar.getAnim());
        setAnimation(Avatars.load(Avatars.DefaultPlayerRight, 1, 4, .1f, true));
        setMaxSpeed(800);
        
       // setSize(Unlocks.currentAvatar.getWidth() * Options.aspectRatio, Unlocks.currentAvatar.getHeight() * Options.aspectRatio);
        setBoundaryPolygon(8);
        setOrigin(getWidth() /2, getHeight() / 2);
        setMaxSpeed(100);
        setDeceleration(250);
        s.addActor(this);
    }
    
    public void loseLife(){
        this.lives --;
        livesIcons.get(lives).remove();
        livesIcons.remove(lives);
        //reset();
        
    }
    
    public void addLife(){
        
            BaseActor l = new BaseActor(0,0, this.getStage());
            //livesIcons[i].loadTexture("smallGreenPlane.png");
            Array<TextureRegion> textureArray = new Array<TextureRegion>();
            //textureArray.add(Unlocks.currentAvatar.getAnim().getKeyFrame(0));
            Animation<TextureRegion> anim = new Animation<TextureRegion>(1, textureArray);
            l.setAnimation(anim);
            l.setSize(50  * Options.aspectRatio, 40  * Options.aspectRatio);
            l.setZIndex(853);
            l.centerAtPosition(((livesIcons.size() + 1) * l.getWidth()) -(l.getWidth() / 2), Gdx.graphics.getHeight() - l.getHeight());
            livesIcons.add(l);
        
        this.lives ++;
        
    }
    
    public boolean dead(){
        return this.lives == 0;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha){
        
        super.draw(batch, parentAlpha);
        
        
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        
        this.getBoundaryPolygon();
       isMoving = false;
       setAcceleration(0 * Options.aspectRatio);
        
        //animation control
       if(getX() < Gdx.input.getX() && 
               (Math.abs((Gdx.input.getX() - getX())) > Math.abs(Gdx.input.getY() - ((Gdx.graphics.getHeight() - getY()))))
               ){
           setAnimation(Avatars.load(Avatars.DefaultPlayerRight, 1, 4, .2f, true));
       }
       if(getX() > Gdx.input.getX() && 
               (Math.abs((Gdx.input.getX() - getX())) > Math.abs(Gdx.input.getY() - ((Gdx.graphics.getHeight() - getY())))) 
               ){
           setAnimation(Avatars.load(Avatars.DefaultPlayerLeft, 1, 4, .2f, true));
       }
       if((Gdx.graphics.getHeight() - getY()) < Gdx.input.getY() &&
               (Math.abs((Gdx.input.getX() - getX())) < Math.abs(Gdx.input.getY() - ((Gdx.graphics.getHeight() - getY()))))
               ){
           setAnimation(Avatars.load(Avatars.DefaultPlayerDown, 1, 4, .2f, true));
       }
       if((Gdx.graphics.getHeight() - getY()) > Gdx.input.getY() &&
               (Math.abs((Gdx.input.getX() - getX())) < Math.abs(Gdx.input.getY() - ((Gdx.graphics.getHeight() - getY()))))
               ){
           setAnimation(Avatars.load(Avatars.DefaultPlayerUP, 1, 4, .2f, true));
       }
       
        System.out.println("Player Y: " + (Gdx.graphics.getHeight() - getY()));
       System.out.println("Mouse Y: " + Gdx.input.getY());
       //movement control
       if(_playerControls.isPressed("Right")){
           setAcceleration(accel * Options.aspectRatio);
           isMoving = true;
           accelerateAtAngle(0);
       }
       if(_playerControls.isPressed("Left")){
           setAcceleration(accel * Options.aspectRatio);
           isMoving = true;
           accelerateAtAngle(180);
       }
       if(_playerControls.isPressed("Down")){
           setAcceleration(accel * Options.aspectRatio);
           isMoving = true;
           accelerateAtAngle(270);
       }
       if(_playerControls.isPressed("Up")){
           setAcceleration(accel * Options.aspectRatio);
           isMoving = true;
           accelerateAtAngle(90);
       }
        applyPhysics(dt);
        //System.out.println();
       if(!isMoving()){
           setAnimationPaused(true);
       }else{
           setAnimationPaused(false);
       }
       
        if(getY() + getHeight() > getWorldBounds().height * Options.aspectRatio){
            setSpeed(0);
            boundToWorld();
        }
        int count = 0;
        ArrayList<BaseActor> boo = BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels");
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels")){
            if(wall.boundaryPolygon == null)
                continue;
            preventOverlap(wall);
        }
        
        //System.out.println(getSpeed());
        
    }
}
