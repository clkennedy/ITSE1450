/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms.Dungeon;

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
public class DungeonRoom extends Room{

    protected enum Key{Floor, UpperLeft, Upper, UpperRight,Left,Right,LowerLeft, Lower, LowerRight, URIWall, ULIWall, LRIWall, LLIWall, Empty, Pillar, Door, DoorLeft, DoorRight,DoorTop}
    
    public DungeonRoom(){
        super(0,0, 14, 10);
    }
    
    public DungeonRoom(int x, int y, int width, int height){
        super(x,y, width, height);
    }
    
    @Override
    public int getFillerObjectKey(){
        return Key.Pillar.ordinal();
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
        
        this.roomWidthPixels = _layout.length * RoomPanels.defaultSize;
        this.roomHeightPixels = _layout[0].length * RoomPanels.defaultSize;
        return this;
    }

    @Override
    public Room generateRoom(Size size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

     @Override
    public String getFloorTexture() {
        return new DungeonFloor().getFloorTexture();
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
        this.roomWidthPixels = width * RoomPanels.defaultSize * Options.aspectRatio;
        this.roomHeightPixels = height * RoomPanels.defaultSize* Options.aspectRatio;
        this._layout = temp;
        return this;
    }
    
    @Override
    public Room generateNewRoom(int x, int y, int width, int height){
        DungeonRoom room = new DungeonRoom(x, y, width, height);
        return room.generateRoom();
    }
    
    @Override
    public Room fillRoomWithObjects(int num) {
        //System.out.println(roomWidthPixels);
        for(int i = 0; i < num; i++){
            BaseActor b = null;
            switch(MathUtils.random(1)){
                case 0:
                    b = new DungeonHole();
                    break;
                case 1:
                    b = new DungeonPillar();
                    break;
                default:
                    b = new DungeonHole();
                    break;
            }
            
            b.setPosition(-50, -50);
            
            while(b.getX() > roomWidthPixels || b.getX() < 0 ||
                   b.getY() > roomHeightPixels || b.getY() < 0 ){
                float x = MathUtils.random(RoomPanels.defaultSize * Options.aspectRatio, roomWidthPixels - (RoomPanels.defaultSize * Options.aspectRatio) - b.getWidth());
                float y = MathUtils.random(RoomPanels.defaultSize * Options.aspectRatio, roomHeightPixels - (RoomPanels.defaultSize * Options.aspectRatio) - b.getHeight());
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
            return new DungeonFloor();
            case UpperLeft:
            return new DungeonWall().UpperLeft();
            case UpperRight:
            return new DungeonWall().UpperRight();
            case Upper:
            return new DungeonWall().Upper();
            case Left:
            return new DungeonWall().Left();
            case Right:
            return new DungeonWall().Right(); 
            case LowerRight:
            return new DungeonWall().LowerRight();
            case Lower:
            return new DungeonWall().Lower(); 
            case LowerLeft:
            return new DungeonWall().LowerLeft();
            case URIWall:
            return new DungeonWall().UpperLeft();
            case ULIWall:
            return new DungeonWall().UpperLeft();
            case LRIWall:
            return new DungeonWall().UpperLeft();
            case LLIWall:
            return new DungeonWall().UpperLeft();
            case Pillar:
            return new DungeonPillar(RoomPanels.defaultSize);
            case Door:
            return new DungeonDoor().Bottom();
            case DoorLeft:
            return new DungeonDoor().Left();
            case DoorRight:
            return new DungeonDoor().Right();
            case DoorTop:
            return new DungeonDoor().Top();
            default:
                return null;
        }
    }
}
