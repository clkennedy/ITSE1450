/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.LevelGenerator;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.sun.javafx.scene.traversal.Direction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 * 
 * @Algorithym: http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
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
       
       for(int y = 1; y < mapHeight; y += 2 ){
           for(int x = 1; x < mapWidth; x += 2 ){
               if(mapRegions[x][y] != null){
                   continue;
               }
               mapMaze(x, y);
           }
       }
       
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
   
   private void mapMaze(int x, int y){
       List<Vector2> cells = new ArrayList<Vector2>();
       Vector2 lastDir = new Vector2(0,0);
       
       _currentRegion ++;
       mapLayout[x][y] = 0;
       mapRegions[x][y] = _currentRegion;
       
       cells.add(new Vector2(x,y));
       
       while(!cells.isEmpty()){
           Vector2 currentCell = cells.get(cells.size() - 1);
           
           List<Vector2> unMadeCells = new ArrayList<Vector2>();
           
           if((currentCell.x + 3 < mapWidth) && (mapLayout[(int)currentCell.x + 2][(int)currentCell.y] != 0)){
               unMadeCells.add(new Vector2(1,0));
           }
           if((currentCell.x - 3 > 0) && (mapLayout[(int)currentCell.x - 2][(int)currentCell.y] != 0)){
               unMadeCells.add(new Vector2(-1,0));
           }
           if((currentCell.y + 3 < mapHeight) && (mapLayout[(int)currentCell.x][(int)currentCell.y + 2] != 0)){
               unMadeCells.add(new Vector2(0,1));
           }
           if((currentCell.x - 3 < mapHeight) && (mapLayout[(int)currentCell.x][(int)currentCell.y - 2] != 0)){
               unMadeCells.add(new Vector2(0,-1));
           }
          
           if(!unMadeCells.isEmpty()){
                Vector2 dir = new Vector2(0,0);
               if(unMadeCells.contains(lastDir) && MathUtils.random(100) > 0){
                   dir = lastDir;
               }
               else{
                   dir = unMadeCells.get(MathUtils.random(unMadeCells.size()));
               }
               
               mapLayout[(int)(currentCell.x + dir.x)][(int)(currentCell.y + dir.y)] = 0;
               cells.add(new Vector2(currentCell.x + dir.x, currentCell.y + dir.y));
               lastDir = dir;
           }
           else{
               cells.remove(cells.size() - 1);
               
               lastDir = null;
           }
       }
   }
    
}
