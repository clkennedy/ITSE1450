/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.dslayer.content.LevelGenerator.LevelGenerator;
import com.dslayer.content.Rooms.Dungeon.DungeonDoor;

/**
 *
 * @author cameron.kennedy
 */
public abstract class RoomDoor extends RoomPanels{
    
    public abstract DungeonDoor Bottom();
    public abstract DungeonDoor Left();
    public abstract DungeonDoor Right();
    public abstract DungeonDoor Top();
    
    private Circle openDistance;
    
    public static LevelGenerator lg;
    
    public RoomDoor(){
        openDistance = new Circle(getX() +(getWidth() / 2), getY()+(getHeight()/ 2), defaultSize * 2);
    }
    
    protected void resetTriggerPoint(){
        openDistance = new Circle(getX() +(getWidth() / 2), getY()+(getHeight()/ 2), defaultSize * 2);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
                
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if(debug){
            batch.end();
            sRend.setProjectionMatrix(this.getStage().getCamera().combined);
            sRend.setColor(Color.RED);
            sRend.begin(ShapeRenderer.ShapeType.Line);

            sRend.circle(openDistance.x, openDistance.y, openDistance.radius);
            
            sRend.end();
            batch.begin();
        }
        super.draw(batch, parentAlpha);
    }
    
    @Override
    public void setPosition(float x, float y){
        resetTriggerPoint();
        super.setPosition(x, y);
    }
    
    @Override
    public BaseActor DefaultSize(){
        resetTriggerPoint();
        return super.DefaultSize();
    }
    
    @Override
    public void act(float dt){
         super.act(dt);
         
         for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
            if(Intersector.overlapConvexPolygons(this.getBoundaryPolygon(), player.getBoundaryPolygon())){
                this.loadTexture(lg.getFloorTexture());
                this.DefaultSize();
            }
        }
    }
}
