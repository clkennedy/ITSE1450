/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Player;

import com.atkinson.game.engine.ActionMapping;
import com.atkinson.game.engine.BaseActor;
import static com.atkinson.game.engine.BaseActor.getWorldBounds;
import com.atkinson.game.engine.PlayerControls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
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
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import java.util.ArrayList;
import com.dslayer.content.Hero.*;
import com.dslayer.content.Rooms.Dungeon.DungeonObject;
import com.dslayer.content.Rooms.RoomFloor;
import com.dslayer.content.Rooms.RoomObject;
import com.dslayer.content.screens.HeroSelectionScreen;
import com.dslayer.content.screens.MultiplayerHeroSelectionScreen;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */
public class Player extends BaseActor{
    
    public boolean isLocalPlayer = true;
    
    public String UserName;
    
    protected float attackCooldown = 2f;
    protected float attackCooldownTime = 0f;
    protected boolean attacking;
    protected boolean canAttack = true;
    
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
    
    private float defaultDamageModifier = 1;
    private float damageModifier = 1;
    
    public Hero hero;
    
    private boolean isMoving = false;
    private Vector2 resetCoords;
    private boolean ignoreRoomObjects = false;
    
    public static enum direction{up, down, left, right};
    public direction dir;
    private boolean directionChanged = false;
    
    private float accel = 150f;
    
    private int points = 0;
    
    public Player(float x, float y, Stage s){
        super(x,y,s);
        
        this._playerControls = new PlayerControls();
        
        _playerControls.setDefaultControls();
        //_playerControls.removeMapping("Up");
        
        _playerControls.get("jump").addKeyboardMapping(Input.Keys.W);
        _playerControls.get("jump").addKeyboardMapping(Input.Keys.UP);
        
        _playerControls.get("fire").addKeyboardMapping(Input.Keys.F);
        _playerControls.get("fire").addKeyboardMapping(Input.Keys.SHIFT_RIGHT);
        
        ActionMapping altFire = new ActionMapping("Alt Fire");
        altFire.addMouseMapping(1);
        _playerControls.addMapping(altFire);
        
        resetCoords = new Vector2(x, y);
        
        
       // setAnimation(Unlocks.currentAvatar.getAnim());\
        if(Multiplayer.socket != null && Multiplayer.socket.connected()){
            hero = MultiplayerHeroSelectionScreen.currentSelection;
        }else{
            hero = HeroSelectionScreen.currentSelection;
        }
        hero.setup(this);
        setAnimation(hero.playRight());
        setScale(1.5f);
        setSize(hero.getDSize(), hero.getDSize());
        setOrigin(getWidth() /2, getHeight() / 2);
        
       // setSize(Unlocks.currentAvatar.getWidth() * Options.aspectRatio, Unlocks.currentAvatar.getHeight() * Options.aspectRatio);
        setBoundaryPolygonLong(10);
        
        setMaxSpeed(100 * Options.aspectRatio);
        setDeceleration(250 * Options.aspectRatio);
        
        healthBar = new Rectangle(this.getStage().getCamera().viewportWidth - maxHealth - 20, 10, maxHealth, 20);
        
        s.addActor(this);
    }
    
    public void setIgnoreRoomObjects(boolean b){
        ignoreRoomObjects = b;
    }
    
    public void takeDamage(int damage){
        if(!isLocalPlayer)
            return;
        
        damage = (int)(damage / damageModifier);
        
        if(health - damageTaken - damage <= 0){
            this.damageTaken += (health - damageTaken);
        }
        else if(health - damageTaken - damage > 0){
            this.damageTaken += damage;
        }
        if(Multiplayer.socket != null && Multiplayer.socket.connected()){
            JSONObject data = new JSONObject();
            try{
                data.put("damage", damage);
                Multiplayer.socket.emit("heroDamageTaken", data);
            }
            catch(Exception e){
                   System.out.println("Failed to push Hero Damage");
            }
        }
    }
    
    public void multiplayerTakeDamage(int damage){
        if(health - damageTaken - damage <= 0){
            this.damageTaken += (health - damageTaken);
        }
        else if(health - damageTaken - damage > 0){
            this.damageTaken += damage;
        }
    }
    
    public void setMaxHealth(int health){
        maxHealth = health;
    }
    
    public int getMaxHealth(){
        return maxHealth;
    }
    
    public void setHealth(int health){
        this.health = health;
    }
    
    public void setDamageModifier(float dm){
        damageModifier = dm;
    }
    
    public void resetDamageModifier(){
        damageModifier = defaultDamageModifier;
    }
    
    public void setDefaultDamageModifer(float dm){
        defaultDamageModifier = dm;
        damageModifier = dm;
    }
    
    public void addPoints(int points){
        if(!isLocalPlayer)
            return;
        this.points += points;
        if(Multiplayer.socket != null && Multiplayer.socket.connected()){
            JSONObject data = new JSONObject();
            try{
                data.put("points", points);
                Multiplayer.socket.emit("heroAddPoints", data);
            }
            catch(Exception e){
                   System.out.println("Failed to push Hero Points");
            }
        }
    }
    
    public void multiplayerAddPoints(int points){
        this.points += points;
    }
    
    public int getPoints(){
        return this.points;
    }
    
    public boolean canMove(){
        return canMove;
    }
    
    public void recover(int recover){
        if(!isLocalPlayer)
            return;
        if((health - damageTaken + recoverAmount) + recover >= maxHealth)
            this.recoverAmount += maxHealth - (health + recoverAmount);
        else if(health - damageTaken + recoverAmount + recover < maxHealth)
            this.recoverAmount += recover;
        if(Multiplayer.socket != null && Multiplayer.socket.connected()){
            JSONObject data = new JSONObject();
            try{
                data.put("recover", recover);
                Multiplayer.socket.emit("heroRecover", data);
            }
            catch(Exception e){
                   System.out.println("Failed to push Hero Recover");
            }
        }  
    }
    
    public void multiplayerRecover(int recover){
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
           if(isLocalPlayer)
                sRend.getProjectionMatrix().setToOrtho2D(0,  0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
           sRend.setColor(Color.BLACK);
           sRend.begin(ShapeRenderer.ShapeType.Line);
           sRend.rect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);
           sRend.end();
            if(isLocalPlayer)
                sRend.setColor(Color.RED);
            else
                sRend.setColor(Color.BLUE);
           sRend.begin(ShapeRenderer.ShapeType.Filled);
           for(int i = 0; i < health - damageTaken; i++){
               sRend.rect(healthBar.x + i, healthBar.y, 1, healthBar.height);
           }
           sRend.setColor(Color.GREEN);
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
    
    public void setHero(Hero hero){
        this.hero = hero;
        this.setAnimation(this.hero.playRight());
        setSize(hero.getDSize(), hero.getDSize());
        healthBar = new Rectangle(this.getStage().getCamera().viewportWidth - maxHealth - 20, 10, maxHealth, 5);
        this.hero.setup(this);
        if(!isLocalPlayer){
            this.hero.trackCD(false);
        }
    }
    
    public void cast(int x, int y, int skill){
        int deltaY = (int)getY() - y;
        int deltaX = (int)getX() - x;
        
        if(checkDying())
                return;
        
        if(skill == 0){
            hero.attack(x, y, this);
        }
        else{
            hero.altAttack(x, y, this);
        }
        if(dir == direction.left){
            setAnimationWithReset(hero.playLeft());
        }
        if(dir == direction.right){
            setAnimationWithReset(hero.playRight());
        }
        if(dir == direction.up){
            setAnimationWithReset(hero.playUp());
        }
        if(dir == direction.down){
            setAnimationWithReset(hero.playDown());
        }
        setAnimationPaused(false);
        setSize(hero.getDSize(), hero.getDSize());
    }
    public void updatePos(float x, float y){
            float deltaY = (int)getY() - y;
            float deltaX = (int)getX() - x;
            
            if(!canMove || checkDying())
                return;
            
            setPosition(x, y);
            
            setAnimationPaused(deltaY == 0 && deltaX == 0);
            
            if(dir == direction.left){
                setAnimation(hero.playLeft());
            }
            if(dir == direction.right){
                setAnimation(hero.playRight());
            }
            if(dir == direction.up){
                setAnimation(hero.playUp());
            }
            if(dir == direction.down){
                setAnimation(hero.playDown());
            }
            
            setSize(hero.getDSize(), hero.getDSize());
    }
    
    private void calculateHealth(float dt){
        
        if(damageTaken > 0){
            //stops recovering on damage
            if(recoverAmount > 0 && recovering && healthRecoverWaitTime > 0){
                recovering = false;
                recoverAmount = 0;
                healthRecoverWaitTime = 0;
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
    
    public boolean checkDying(){
        if(isDead()){
            if(!hero.isDying())
            {
                setAnimationWithReset(hero.playDie());
                setAnimationPaused(false);
                setSize(hero.getDSize(), hero.getDSize());
                hero.isDying(true);
            }
        }
        return hero.isDying();
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        hero.act(dt);
        if(!isLocalPlayer){
            healthBar.x = (getX()) + ((getWidth()/2) - (healthBar.width /2));
            healthBar.y = getY() + getHeight();
            
            calculateHealth(dt);
            if(checkDying()){
                return;
            }
            if(isAnimationFinished()){
                setCanMove(true);
            }
            applyPhysics(dt);
            return;
        }
        calculateHealth(dt);
        
        if(checkDying()){
            return;
        }
        
        if(isAnimationFinished()){
            setCanMove(true);
        }
        hero.checkAttack(dt);
        //this.getBoundaryPolygon();
        isMoving = false;
        //setAcceleration(0 * Options.aspectRatio);
       
        float MouseWorldY = this.getStage().getCamera().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).y;
        float MouseWorldX = this.getStage().getCamera().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0)).x;
        //animation control
        if(getX() < MouseWorldX && 
               (Math.abs((MouseWorldX - getX())) > Math.abs(MouseWorldY - getY())))
               {
            if(hero.isAttacking()){
                setAnimationWithReset(hero.playRight());
            }
            if(canMove)
                setAnimation(hero.playRight());
            setSize(hero.getDSize(), hero.getDSize());
            directionChanged = dir != direction.right;
            dir = direction.right;
        }
       if(getX() > MouseWorldX && 
               (Math.abs((MouseWorldX - getX())) > Math.abs(MouseWorldY - getY()))) 
               {
                    if(hero.isAttacking()){
                        setAnimationWithReset(hero.playLeft());
                    }
                    if(canMove)
                        setAnimation(hero.playLeft());
           
           setSize(hero.getDSize(), hero.getDSize());
           directionChanged = dir != direction.left;
            dir = direction.left;
       }
       if((getY()) > MouseWorldY &&
               (Math.abs((MouseWorldX - getX())) < Math.abs(MouseWorldY - getY())))
               {
                   if(hero.isAttacking()){
                        setAnimationWithReset(hero.playDown());
                    }
                    if(canMove)
                        setAnimation(hero.playDown());
           
           setSize(hero.getDSize(), hero.getDSize());
           directionChanged = dir != direction.down;
            dir = direction.down;
       }
       if((getY()) < MouseWorldY &&
               (Math.abs((MouseWorldX - getX())) < Math.abs(MouseWorldY - getY())))
               {
                   if(hero.isAttacking()){
                        setAnimationWithReset(hero.playUp());
                    }
                    if(canMove)
                        setAnimation(hero.playUp());
           
           setSize(hero.getDSize(), hero.getDSize());
           directionChanged = dir != direction.up;
            dir = direction.up;
       }
       
       //movement control
       if(canMove){
            if(_playerControls.isPressed("Fire") && hero.canBasicAttack()){
                hero.attack(MouseWorldX, MouseWorldY, this);
                if(Multiplayer.socket != null && Multiplayer.socket.connected()){
                    JSONObject data = new JSONObject();
                    try{
                        data.put("targetX", MouseWorldX);
                        data.put("targetY", MouseWorldY);
                        data.put("skill", 0);
                        Multiplayer.socket.emit("heroCast", data);
                        }catch(Exception e){

                        }
                }
            }
            if(_playerControls.isPressed("Alt Fire") && hero.canAltAttack()){
                hero.altAttack(MouseWorldX, MouseWorldY, this);
                if(Multiplayer.socket != null && Multiplayer.socket.connected()){
                    JSONObject data = new JSONObject();
                    try{
                        data.put("targetX", MouseWorldX);
                        data.put("targetY", MouseWorldY);
                        data.put("skill", 1);
                        Multiplayer.socket.emit("heroCast", data);
                        }catch(Exception e){

                        }
                }
            }
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
       }
       
       applyPhysics(dt);
       if(!isMoving() && canMove){
           setAnimationPaused(true);
       }else{
           setAnimationPaused(false);
       }
       setOrigin(getWidth() / 2,getHeight() /2);
        boundToWorld();
        alignCamera();
        
        int count = 0;
        
        //wall Collison
        ArrayList<BaseActor> allRoomObjects = BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.RoomPanels");
        for(BaseActor obj: allRoomObjects){
            if(obj.boundaryPolygon == null || (ignoreRoomObjects && obj instanceof RoomObject) || (obj instanceof RoomFloor)){
                continue;
            }
            preventOverlap(obj);
        }
    }
    
    public boolean directionChanged(){
        return directionChanged;
        
    }
    
}
