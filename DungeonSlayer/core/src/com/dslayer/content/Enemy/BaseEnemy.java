/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.ArrayList;

/**
 *
 * @author ARustedKnight
 */
public abstract class BaseEnemy extends BaseActor{
    
    protected int maxHealth;
    protected int health;
    protected Rectangle healthBar;
    private int healthLowerRate = 1;
    private float healthWaitBeforeLower = .4f;
    private float healthLowerWaitTime = 0;
    private int damageTaken = 0;
    
    protected float size;
    protected boolean isDying;
    protected boolean hitWall;
    
    public BaseEnemy() {
        
    }
    
    public BaseEnemy(float x, float y, Stage s){
        super(x,y,s);
        size = 50;
        healthBar = new Rectangle(x, y, maxHealth/2 , 5);
        
    }
    
    public void takeDamage(int damage){
        if(health - damageTaken - damage <= 0){
            this.damageTaken += (health - damageTaken);
        }
        else if(health - damageTaken - damage > 0){
            this.damageTaken += damage;
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
        healthBar.x = getX() - getWidth()/2;
        healthBar.y = getY() + getHeight();
        //wall Collison
        ArrayList<BaseActor> boo = BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels");
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels")){
            if(wall.boundaryPolygon == null)
                continue;
            if(overlaps(wall)){
                hitWall = true;
                //System.out.println(hitWall);
            }
            preventOverlap(wall);
        }
    }
}
