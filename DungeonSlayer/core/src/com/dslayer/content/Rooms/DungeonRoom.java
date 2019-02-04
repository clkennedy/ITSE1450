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
import com.dslayer.content.objects.Room;

/**
 *
 * @author ARustedKnight
 */
public class DungeonRoom extends Room{

    private enum Key{Floor, UpperLeft, Upper, UpperRight,Left,Right,LowerLeft, Lower, LowerRight}
    
    @Override
    public Room generateRoom() {
        this._layout = new int[][]{{1,2,2,2,2,2,2,2,3},
                                 {4,0,0,0,0,0,0,0,5},
                                {4,0,0,0,0,0,0,0,5},
                                {4,0,0,0,0,0,0,0,5},
                                {6,7,7,7,7,7,7,7,8},};
        return null;
    }

    @Override
    public Room generateRoom(Size size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Room generateRoom(int length, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Draw(Stage mainStage) {
        BaseActor temp = new BaseActor();
        for(int i = 0; i < this._layout.length; i++){
            for(int j = 0; j < this._layout[i].length; j++){
                temp = Map(Key.values()[this._layout[i][j]]);
                temp.setPosition(j * defaultSize, Gdx.graphics.getHeight()/1.5f - (i * defaultSize));
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
            default:
                throw new AssertionError();
        }
    }
}
