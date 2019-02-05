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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.dslayer.content.options.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    
    private int maxHealth = 100;
    private int health = maxHealth;
    Rectangle healthBar;
    private int healthLowerRate = 1;
    private float healthWaitBeforeLower = 1.3f;
    private float healthLowerWaitTime = 0;
    private int damageTaken = 0;
    
    private int healthRecoverRate = 1;
    private float healthWaitBeforeRecover = .7f;
    private float healthRecoverWaitTime = 0;
    private boolean recovering = false;
    private int recoverAmount = 0;
    
    
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
        
        
       // setAnimation(Unlocks.currentAvatar.getAnim());
        setAnimation(Avatars.load(Avatars.DefaultPlayerRight, 1, 4, .1f, true));
        setMaxSpeed(800);
        setScale(1.5f);
       // setSize(Unlocks.currentAvatar.getWidth() * Options.aspectRatio, Unlocks.currentAvatar.getHeight() * Options.aspectRatio);
        setBoundaryPolygon(8);
        setOrigin(getWidth() /2, getHeight() / 2);
        setMaxSpeed(100);
        setDeceleration(250);
        
        healthBar = new Rectangle(this.getStage().getCamera().viewportWidth - maxHealth - 20, 10, maxHealth, 20);
        
        s.addActor(this);
    }
    
    public void takeDamage(int damage){
        if(health - damageTaken - damage <= 0){
            this.damageTaken += (health - damageTaken);
        }
        else if(health - damageTaken - damage > 0){
            this.damageTaken += damage;
        }
    }
    
    public void recover(int recover){
        if((health - damageTaken + recoverAmount) + recover >= maxHealth)
            this.recoverAmount += maxHealth - (health + recoverAmount);
        else if(health - damageTaken + recoverAmount + recover < maxHealth)
            this.recoverAmount += recover;
            
    }
    
    public boolean isDead(){
        return this.health + recoverAmount <= 0;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha){
        
        super.draw(batch, parentAlpha);
        if(isVisible()) {
           batch.end();
           sRend.setProjectionMatrix(this.getStage().getCamera().combined);
           sRend.getProjectionMatrix().setToOrtho2D(0,  0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
           sRend.setColor(Color.BLACK);
           sRend.begin(ShapeRenderer.ShapeType.Line);
           sRend.rect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);
           sRend.end();
           sRend.setColor(Color.RED);
           sRend.begin(ShapeRenderer.ShapeType.Filled);
           for(int i = 0; i < health - damageTaken; i++){
               sRend.rect(healthBar.x + i, healthBar.y, 1, healthBar.height);
           }
           sRend.setColor(Color.BLUE);
           for(int i = (health - damageTaken) ; i < (health - damageTaken) + recoverAmount ; i++){
               sRend.rect(healthBar.x + i, healthBar.y, 1, healthBar.height);
           }
           sRend.setColor(Color.YELLOW);
           for(int i = (health - damageTaken + recoverAmount); i < health ; i++){
               sRend.rect(healthBar.x + i, healthBar.y, 1, healthBar.height);
           }
           sRend.end();
           batch.begin();
        }
    }
    
    private void calculateHealth(float dt){
        
        if(damageTaken > 0){
            //stops recovering on damage
            if(recoverAmount > 0 && recovering && healthRecoverWaitTime > 0){
                recovering = false;
                recoverAmount = 0;
                healthRecoverWaitTime = 0;
                System.out.println("Clear Recover");
            }
            if(healthLowerWaitTime > healthWaitBeforeLower){
                health -= healthLowerRate;
                damageTaken -= healthLowerRate;   
            }
            healthLowerWaitTime += dt;
        }
        else{
             healthLowerWaitTime = 0;
        }
        if(recoverAmount > 0){
            if(healthRecoverWaitTime > healthWaitBeforeRecover){
                health += healthRecoverRate;
                recoverAmount -= healthRecoverRate;
                recovering = true;
            }
            if(!(damageTaken > 0))
            healthRecoverWaitTime += dt;
        }
        else{
             healthRecoverWaitTime = 0;
        }
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        
        this.setOrigin(this.getWidth() / 2,this.getHeight() /2);
        calculateHealth(dt);
        if(isDead()){
            return;
        }
        
        this.getBoundaryPolygon();
        isMoving = false;
        setAcceleration(0 * Options.aspectRatio);
       
        float MouseWorldY = this.getStage().getCamera().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).y;
        float MouseWorldX = this.getStage().getCamera().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).x;
        //animation control
        if(getX() < MouseWorldX && 
               (Math.abs((MouseWorldX - getX())) > Math.abs(MouseWorldY - getY())))
               {
            setAnimation(Avatars.load(Avatars.DefaultPlayerRight, 1, 4, .2f, true));
        }
       if(getX() > MouseWorldX && 
               (Math.abs((MouseWorldX - getX())) > Math.abs(MouseWorldY - getY()))) 
               {
           setAnimation(Avatars.load(Avatars.DefaultPlayerLeft, 1, 4, .2f, true));
       }
       if((getY()) > MouseWorldY &&
               (Math.abs((MouseWorldX - getX())) < Math.abs(MouseWorldY - getY())))
               {
           setAnimation(Avatars.load(Avatars.DefaultPlayerDown, 1, 4, .2f, true));
       }
       if((getY()) < MouseWorldY &&
               (Math.abs((MouseWorldX - getX())) < Math.abs(MouseWorldY - getY())))
               {
           setAnimation(Avatars.load(Avatars.DefaultPlayerUP, 1, 4, .2f, true));
       }
       
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
        boundToWorld();
        alignCamera();
        
        int count = 0;
        
        //wall Collison
        ArrayList<BaseActor> boo = BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels");
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels")){
            if(wall.boundaryPolygon == null)
                continue;
            preventOverlap(wall);
        }
        
        //System.out.println(getSpeed());
        
    }
    
}
