/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.LevelGenerator;

import com.badlogic.gdx.math.MathUtils;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class LevelGenerator {
    
    protected List<Room> _rooms;
    
    protected Integer[][] mapLayout;
    protected Integer[][] mapRegions;
    
    protected int _currentRegion = -1;
    
    protected int minimumRoomSize = 7;
    protected int maximumRoomSize = 17;
    
    protected int mapWidth;
    protected int mapHeight;
    
    
   public LevelGenerator(int width, int height){
       _rooms = new ArrayList();
       
       mapLayout = new Integer[width][height];
       mapRegions = new Integer[width][height];
       mapWidth = width;
       mapHeight = height;
   }
   
   public void generateMap(){
       mapRooms();
       
   }
   
   private void mapRooms(){
       for(int i = 0; i < 200; i++){
           int width = MathUtils.random(minimumRoomSize, maximumRoomSize);
           int height = MathUtils.random(minimumRoomSize, maximumRoomSize);
           
           int x = MathUtils.random(mapWidth - width);
           int y = MathUtils.random(mapHeight - height);   
           
           Room room = new DungeonRoom(x,y, width, height);
           room.generateRoom();
           
           boolean overlaps = false;
           for(Room g : _rooms){
               if(room.overlaps(g)){
                   overlaps = true;
                   break;
               }
           }
           
           if(overlaps)
               continue;
           
           _rooms.add(room);
           
           _currentRegion ++;
           int row = 0;
           int col  = 0;
           for(int r = x; r < x + height; r ++){
               for(int c = y; c < y + width; c++){
                   mapRegions[r][c] = _currentRegion;
                   mapLayout[r][c] = room.getLayout()[row][col];
                   col++;
               }
               row++;
           }
       }
   }
    
}
