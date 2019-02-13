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
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Options;

/**
 *
 * @author ARustedKnight
 */
public class DungeonRoom extends Room{
    protected enum Key{Floor, UpperLeft, Upper, UpperRight,Left,Right,LowerLeft, Lower, LowerRight, URIWall, ULIWall, LRIWall, LLIWall, Empty}
    
    public DungeonRoom(){
        super(0,0, 14, 10);
    }
    
    public DungeonRoom(int x, int y, int width, int height){
        super(x,y, width, height);
    }
    
    @Override
    public Room generateRoom() {
        this.generateRoom(roomWidth, roomHeight);
        
        this.roomWidthPixels = _layout.length * DungeonPanels.defaultSize;
        this.roomHeightPixels = _layout[0].length * DungeonPanels.defaultSize;
        return null;
    }

    @Override
    public Room generateRoom(Size size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        this.roomWidthPixels = width * DungeonPanels.defaultSize * Options.aspectRatio;
        this.roomHeightPixels = height * DungeonPanels.defaultSize* Options.aspectRatio;
        this._layout = temp;
        return this;
    }
    
    @Override
    public Room fillRoomWithObjects(int num) {
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
            while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                   b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                float x = MathUtils.random(DungeonPanels.defaultSize, roomWidthPixels - DungeonPanels.defaultSize - b.getWidth());
                float y = MathUtils.random(DungeonPanels.defaultSize, roomHeightPixels - DungeonPanels.defaultSize - b.getHeight());
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
        BaseActor temp = new DungeonPanels();
        for(int i = 0; i < this._layout.length; i++){
            for(int j = 0; j < this._layout[i].length; j++){
                temp = Map(Key.values()[this._layout[i][j]]);
                //if(temp == null)
                    //continue;
                temp.setPosition(j * temp.getWidth(),Difficulty.worldHeight - temp.getHeight() - (i * temp.getHeight()));
                temp.getBoundaryPolygon();
                mainStage.addActor(temp);
            }
        }
        for(BaseActor b: roomObjects){
            b.setZIndex(1200);
            System.out.println(b.getX() + "|" + b.getY());
            mainStage.addActor(b);
        }
    }
    
    private BaseActor Map(Key i){
        switch(i) {
            case Floor:
            return DungeonPanels.Floor();
            case UpperLeft:
            return DungeonPanels.UpperLeftWall();
            case UpperRight:
            return DungeonPanels.UpperRightWall();
            case Upper:
            return DungeonPanels.UpperWall();
            case Left:
            return DungeonPanels.LeftWall();
            case Right:
            return DungeonPanels.RightWall(); 
            case LowerRight:
            return DungeonPanels.LowerRightWall();
            case Lower:
            return DungeonPanels.LowerWall(); 
            case LowerLeft:
            return DungeonPanels.LowerLeftWall();
            case URIWall:
            return DungeonPanels.UpperRightInvertedWall();
            case ULIWall:
            return DungeonPanels.UpperLeftInvertedWall();
            case LRIWall:
            return DungeonPanels.LowerRightInvertedWall();
            case LLIWall:
            return DungeonPanels.LowerLeftInvertedWall();
            default:
                return null;
        }
    }
}