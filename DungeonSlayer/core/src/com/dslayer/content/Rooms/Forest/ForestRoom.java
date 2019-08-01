/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms.Forest;

import com.dslayer.content.Rooms.Dungeon.*;
import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Rooms.Dungeon.DungeonHole;
import com.dslayer.content.Rooms.Dungeon.DungeonPillar;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class ForestRoom extends Room{

    protected enum Key{Floor, UpperLeft, Upper, UpperRight,Left,Right,LowerLeft, Lower, LowerRight, URIWall, ULIWall, LRIWall, LLIWall, Empty, Pillar, Door, DoorLeft, DoorRight,DoorTop}
    
    public ForestRoom(){
        super(0,0, 14, 10);
    }
    
    public ForestRoom(int x, int y, int width, int height){
        super(x,y, width, height);
    }
    
    @Override
    public int getFillerObjectKey(){
        return Key.Left.ordinal();
    }
    
    @Override
    public int getDoor() {
        return Key.Door.ordinal();
    }

    @Override
    public int getDoorLeft() {
        return Key.DoorLeft.ordinal();
        
    }

    @Override
    public int getDoorRight() {
        return Key.DoorRight.ordinal();
    }

    @Override
    public int getDoorTop() {
        return Key.DoorTop.ordinal();
    }
    
    @Override
    public Room generateRoom() {
        this.generateRoom(roomWidth, roomHeight);
        
        this.roomWidthPixels = _layout.length * (RoomPanels.defaultSize);
        this.roomHeightPixels = _layout[0].length * (RoomPanels.defaultSize);
        return this;
    }

    @Override
    public Room generateRoom(Size size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

     @Override
    public String getFloorTexture() {
        return new ForestFloor().getFloorTexture();
    }
    @Override
    public Room generateRoom(int width, int height) {
        int[][] temp = new int[height][width];
        for(int i = 0; i < height; i ++){
            for (int j = 0; j < width; j ++){
                temp[i][j] = Key.Floor.ordinal();
                if(i == 0)
                    temp[i][j] = Key.Upper.ordinal();
                if(i == height - 1)
                    temp[i][j] = Key.Lower.ordinal();
                if(j == 0)
                    temp[i][j] = Key.Left.ordinal();
                if(j == width - 1)
                    temp[i][j] = Key.Right.ordinal();
                if(i == 0 && j == 0)
                    temp[i][j] = Key.UpperLeft.ordinal();
                if(i == 0 && j == width - 1)
                    temp[i][j] = Key.UpperRight.ordinal();
                if(i == height - 1 && j == 0)
                    temp[i][j] = Key.LowerLeft.ordinal();
                if(i == height - 1 && j == width - 1)
                    temp[i][j] = Key.LowerRight.ordinal();
                //System.out.print(temp[i][j] + ",");
            }
            //System.out.println();
        }
        this.roomWidthPixels = width * RoomPanels.defaultSize;
        this.roomHeightPixels = height * RoomPanels.defaultSize;
        this._layout = temp;
        return this;
    }
    
    @Override
    public Room generateNewRoom(int x, int y, int width, int height){
        ForestRoom room = new ForestRoom(x, y, width, height);
        return room.generateRoom();
    }
    
    @Override
    public Room fillRoomWithObjects(int num) {
        //System.out.println(roomWidthPixels);
        for(int i = 0; i < num; i++){
            BaseActor b = null;
            switch(MathUtils.random(1)){
                case 0:
                    b = new ForestHole();
                    break;
                case 1:
                    b = new ForestPillar();
                    break;
                default:
                    b = new ForestHole();
                    break;
            }
            
            b.setPosition(-50, -50);
            
            while(b.getX() > roomWidthPixels || b.getX() < 0 ||
                   b.getY() > roomHeightPixels || b.getY() < 0 ){
                float x = MathUtils.random(RoomPanels.defaultSize, roomWidthPixels - (RoomPanels.defaultSize) - b.getWidth());
                float y = MathUtils.random(RoomPanels.defaultSize, roomHeightPixels - (RoomPanels.defaultSize) - b.getHeight());
                //System.out.println("X: " + x + "|Y: "+ y);
                b.setPosition(x, y);
                for(BaseActor obj : roomObjects){
                    if(b.overlaps(obj)){
                        b.setPosition(-50, -50);
                    }
                }
            }
            roomObjects.add(b);
        }
        return this;
    }

    @Override
    public void Draw(Stage mainStage) {
        BaseActor temp = new RoomPanels();
        for(int i = 0; i < this._layout.length; i++){
            for(int j = 0; j < this._layout[i].length; j++){
                temp = Map(this._layout[i][j]);
                if(temp == null)
                    continue;
                temp.setPosition(j * temp.getWidth(),Difficulty.worldHeight - temp.getHeight() - (i * temp.getHeight()));
                temp.getBoundaryPolygon();
                mainStage.addActor(temp);
            }
        }
        for(BaseActor b: roomObjects){
            b.setZIndex(1200);
            //System.out.println(b.getX() + "|" + b.getY());
            mainStage.addActor(b);
        }
    }
    
    @Override
    public BaseActor Map(int key){
        Key k = Key.values()[key];
        switch(k) {
            case Floor:
            return new ForestFloor();
            case UpperLeft:
            return new ForestWall().UpperLeft();
            case UpperRight:
            return new ForestWall().UpperRight();
            case Upper:
            return new ForestWall().Upper();
            case Left:
            return new ForestWall().Left();
            case Right:
            return new ForestWall().Right(); 
            case LowerRight:
            return new ForestWall().LowerRight();
            case Lower:
            return new ForestWall().Lower(); 
            case LowerLeft:
            return new ForestWall().LowerLeft();
            case URIWall:
            return new ForestWall().UpperLeft();
            case ULIWall:
            return new ForestWall().UpperLeft();
            case LRIWall:
            return new ForestWall().UpperLeft();
            case LLIWall:
            return new ForestWall().UpperLeft();
            case Pillar:
            return new ForestPillar(RoomPanels.defaultSize);
            case Door:
            return new ForestDoor().Bottom();
            case DoorLeft:
            return new ForestDoor().Left();
            case DoorRight:
            return new ForestDoor().Right();
            case DoorTop:
            return new ForestDoor().Top();
            default:
                return null;
        }
    }
}
