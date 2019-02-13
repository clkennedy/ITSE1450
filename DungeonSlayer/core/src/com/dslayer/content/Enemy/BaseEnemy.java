/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.Goblin.GoblinAssassin;
import com.dslayer.content.Enemy.Golem.BaseGolem;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonArmored;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */
public abstract class BaseEnemy extends BaseActor{

    protected boolean chaseTarget = true;
    
    public static enum type{SkeletionWarrior, SkeletonMage, ArmoredSkeleton, BlueGolem, GoblinAssassin};
    
    protected int maxHealth;
    protected int health;
    protected Rectangle healthBar;
    private int healthLowerRate = 1;
    private float healthWaitBeforeLower = .4f;
    private float healthLowerWaitTime = 0;
    private int damageTaken = 0;
    
    protected Skill skill;
    
    protected float size;
    protected boolean isDying;
    protected boolean hitWall;
    
    public BaseActor target = null;
    protected Circle AttackRange;
    protected Circle TargetRange;
    
    protected float attackDamage;
    
    protected float attackCooldown = 5f;
    protected float attackCooldownTime = 0f;
    protected boolean attacking;
    protected boolean canAttack = true;
    
    protected int pointsWorth = 10;
    
    public Vector2 moveTo;
    protected Circle moveToRange;
    
    protected Texture walk;
    protected Texture attack;
    protected Texture die;
    
    protected boolean canMove = true;
    
    public static BaseEnemy getNewEnemy(type en,float x,float y){
        switch (en) {
            case SkeletionWarrior:
                return new SkeletonWarrior(x, y, BaseActor.getMainStage());
            case SkeletonMage:
                return new SkeletonMage(x, y, BaseActor.getMainStage());
            case BlueGolem:
                return new BlueGolem(x, y, BaseActor.getMainStage());
            case ArmoredSkeleton:
                return new SkeletonArmored(x, y, BaseActor.getMainStage());
            case GoblinAssassin:
                return new GoblinAssassin(x, y, BaseActor.getMainStage());
            default:
                throw new AssertionError();
        }
    }
    
    public BaseEnemy() {
        
    }
    
    public BaseEnemy(float x, float y, Stage s){
        super(x,y,s);
        size = 50;
        healthBar = new Rectangle(x, y, maxHealth , 5);
    }
    
    public void setCanMove(boolean b){
        canMove = b;
    }
    
    public void takeDamage(int damage, Player player){
        if(!player.isLocalPlayer)
            return;
        if(health - damageTaken - damage <= 0){
            this.damageTaken += (health - damageTaken);
        }
        else if(health - damageTaken - damage > 0){
            this.damageTaken += damage;
        }
        if(Multiplayer.socket != null && Multiplayer.socket.connected()){
            JSONObject data = new JSONObject();
            try{
                data.put("id", this.network_id);
                data.put("damage", damage);
                Multiplayer.socket.emit("enemyDamageTaken", data);
            }
            catch(Exception e){
                   System.out.println("Failed to push enemy Damage");
            }
        }
        if(isDead()){
            player.addPoints(pointsWorth);
            if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
                JSONObject data = new JSONObject();
                try{
                    data.put("id", this.network_id);
                    Multiplayer.socket.emit("enemyDied", data);
                }catch(Exception e){
                    System.out.println("Failed to push enemy Died");
                }
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
    
    protected void lookForTarget(){
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && !Multiplayer.host)
            return;
        
        for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
            if(player.boundaryPolygon == null)
                continue;
            if(Intersector.overlaps(TargetRange,player.getBoundaryPolygon().getBoundingRectangle()) && !((Player)player).isDead()){
                if(target == null)
                    target = player;
            }
            else{
                target = null;
            }
        }
        
        if(target == null && (hitWall || Intersector.overlaps(moveToRange, getBoundaryPolygon().getBoundingRectangle()))){
            moveTo.x = MathUtils.random(Difficulty.worldWidth);
            moveTo.y = MathUtils.random(Difficulty.worldHeight);
            if(hitWall)
                setSpeed(0);
            hitWall = false;
            moveToChanged();
        }
        else if(target != null && chaseTarget){
            moveTo.x = target.getX() + (target.getWidth()/2);
            moveTo.y = target.getY() + (target.getHeight()/2);
            moveToChanged();
        }
        
    }
    
    public abstract void die();
    
    @Override
    public void draw(Batch batch, float parentAlpha){
        
        super.draw(batch, parentAlpha);
        
        //System.out.println(healthBar.x + " " + healthBar.y);
        if(isVisible()) {
           batch.end();
           sRend.setProjectionMatrix(batch.getProjectionMatrix());
           sRend.setColor(Color.BLACK);
           sRend.begin(ShapeRenderer.ShapeType.Line);
           sRend.rect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);
           sRend.end();
           sRend.setColor(Color.RED);
           sRend.begin(ShapeRenderer.ShapeType.Filled);
           for(int i = 0; i < health - damageTaken; i++){
               sRend.rect(healthBar.x + i, healthBar.y, 1f, healthBar.height);
           }
           sRend.setColor(Color.YELLOW);
           for(int i = (health - damageTaken); i < health ; i++){
               sRend.rect(healthBar.x + i, healthBar.y, 1, healthBar.height);
           }
           sRend.end();
           batch.begin();
        }
    }
    
    public abstract void attack(BaseActor player);
    
    protected void moveToChanged(){
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            JSONObject data = new JSONObject();
            try{
                data.put("id", this.network_id);
                data.put("x", moveTo.x);
                data.put("y", moveTo.y);
                Multiplayer.socket.emit("enemyTargetChange", data);
            }catch(Exception e){
                System.out.println("Failed to push target change" + e.getMessage());
            }
        }
    }
    
    public void setMoveTo(Vector2 v){
        moveTo = v;
    }
    
    private void calculateHealth(float dt){
        
        if(damageTaken > 0){
            
            if(healthLowerWaitTime > healthWaitBeforeLower){
                health -= healthLowerRate;
                damageTaken -= healthLowerRate;   
            }
            healthLowerWaitTime += dt;
        }
        else{
             healthLowerWaitTime = 0;
        }
        if(isDead()){
            //setBoundaryPolygon(0);
        }
    }
    
    public void cast(BaseActor actor, Vector2 v2, Skill.From from){
        
    }
    
    public boolean isDead(){
        return this.health - damageTaken <= 0;
    }
    @Override
    public void act(float dt){
        super.act(dt);
        calculateHealth(dt);
        if(isDead() && !isDying){
            die();
        }
        if(isDying && isAnimationFinished()){
            remove();
        }
        if(isDying)
            return;
        healthBar.x = (getX()) + ((getWidth()/2) - (healthBar.width /2));
        healthBar.y = getY() + getHeight();
        //wall Collison
        ArrayList<BaseActor> allRoomObjects = BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.RoomPanels");
        for(BaseActor wall: allRoomObjects){
            if(wall.boundaryPolygon == null)
                continue;
            if(overlaps(wall)){
                hitWall = true;
                //System.out.println(hitWall);
            }
            preventOverlap(wall);
        }
    }
    @Override
    public boolean remove(){
        if(attack != null)
            attack.dispose();
        if(walk != null)
            walk.dispose();
        if(die != null)
            die.dispose();
        return super.remove();
    }
    
}
