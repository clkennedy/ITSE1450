/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.options.Difficulty;

/**
 *
 * @author ARustedKnight
 */
public class DungeonRoom extends Room{

    private enum Key{Floor, UpperLeft, Upper, UpperRight,Left,Right,LowerLeft, Lower, LowerRight, URIWall, ULIWall, LRIWall, LLIWall, Empty}
    
    @Override
    public Room generateRoom() {
        this._layout = new int[][]{{1,2,2,2,2,2,2,2,2,2,2,2,2,3},
                                 {4,0,0,0,0,0,0,0,0,0,0,0,0,5},
                                {4,0,0,10,9,0,0,0,0,0,0,0,0,5},
                                {4,0,0,12,11,0,0,0,0,0,0,0,0,5},
                                {4,0,0,0,0,0,0,0,0,0,0,0,0,5},
                                {4,0,0,0,0,0,0,0,0,0,0,0,0,5},
                                {4,0,0,0,0,0,0,0,0,0,0,0,0,5},
                                {4,0,0,0,0,0,0,0,0,0,0,0,0,5},
                                {4,0,0,0,0,0,0,0,0,0,0,0,0,5},
                                {6,7,7,7,7,7,7,7,7,7,7,7,7,8}};
        
        this.roomWidth = 14 * DungeonPanels.defaultSize;
        this.roomHeight = 10 * DungeonPanels.defaultSize;
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
                System.out.print(temp[i][j] + ",");
            }
            System.out.println();
        }
        this.roomWidth = width * DungeonPanels.defaultSize;
        this.roomHeight = height * DungeonPanels.defaultSize;
        this._layout = temp;
        return null;
    }

    @Override
    public void Draw(Stage mainStage) {
        BaseActor temp = new DungeonPanels();
        for(int i = 0; i < this._layout.length; i++){
            for(int j = 0; j < this._layout[i].length; j++){
                temp = Map(Key.values()[this._layout[i][j]]);
                //if(temp == null)
                    //continue;
                temp.setPosition(j * DungeonPanels.defaultSize,Difficulty.worldHeight - DungeonPanels.defaultSize - (i * DungeonPanels.defaultSize));
                temp.getBoundaryPolygon();
                mainStage.addActor(temp);
            }
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
