/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.LevelGenerator;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.options.Options;
import com.sun.javafx.scene.traversal.Direction;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ARustedKnight
 * 
 * @Algorithym: http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
 */
public class LevelGenerator {
    
    private static List<Vector2> Directions = new ArrayList(){{
        add(new Vector2(0,1));
        add(new Vector2(0,-1));
        add(new Vector2(1,0));
        add(new Vector2(-1,0));
    }};
    
    protected List<Room> _rooms;
    
    protected Integer[][] mapLayout;
    protected Integer[][] mapRegions;
    
    protected int _currentRegion = -1;
    
    protected int minimumRoomSize = 7;
    protected int maximumRoomSize = 17;
    
    protected int mapWidth;
    protected int mapHeight;
    protected float mapWidthPixels;
    protected float mapHeightPixels;
    
    Room _room;
    
    
   public LevelGenerator(int width, int height){
       _rooms = new ArrayList();
       
       mapLayout = new Integer[width][height];
       mapRegions = new Integer[width][height];
       mapWidth = width;
       mapHeight = height;
       mapWidthPixels = mapWidth * RoomPanels.defaultSize * Options.aspectRatio;
       mapHeightPixels = mapHeight * RoomPanels.defaultSize * Options.aspectRatio;
   }
   
   public float getPixelWidth(){
       return mapWidthPixels;
   }
   public float getPixelHeight(){
       return mapHeightPixels;
   }
   
   public void setRoom(Room room){
       _room = room;
       
       int fill = _room.getFillerObjectKey();
       for(int r = 0; r < mapHeight; r++){
           for(int c = 0; c < mapWidth; c++){
               mapLayout[r][c] = fill;
           } 
       }
       
   }
   
   public void draw(Stage stage){
       BaseActor temp = new RoomPanels();
        for(int i = 0; i < this.mapLayout.length; i++){
            //System.out.print("[");
            for(int j = 0; j < this.mapLayout[i].length; j++){
                //System.out.print(mapLayout[i][j] + " | ");
                temp = _room.Map(this.mapLayout[i][j]);
                if(temp == null)
                    continue;
                temp.setPosition(j * temp.getWidth(),mapHeightPixels - temp.getHeight() - (i * temp.getHeight()));
                temp.getBoundaryPolygon();
                stage.addActor(temp);
            }
            //System.out.println("]");
        }
   }
   
   public void generateMap(){
       mapRooms();
       System.out.println("Current R: " + _currentRegion);
       for(int y = 1; y < mapHeight; y += 2 ){
           for(int x = 1; x < mapWidth; x += 2 ){
               if(mapLayout[x][y] != _room.getFillerObjectKey()){
                   continue;
               }
               //System.out.println(x + "|" + y);
               mapMaze(x, y);
           }
       }
       System.out.println("Current R: " + _currentRegion);
       connectRegions();
       
   }
   
   private void mapRooms(){
       for(int i = 0; i < 200; i++){
           int width = MathUtils.random(minimumRoomSize, maximumRoomSize);
           int height = MathUtils.random(minimumRoomSize, maximumRoomSize);
           
           int x = MathUtils.random(mapWidth - 1 - width);
           int y = MathUtils.random(mapHeight - 1 - height);   
           
           Room room = _room.generateNewRoom(x,y, width, height);
           //room.generateRoom();
           
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
           for(int r = y; r < y + height; r ++){
               for(int c = x; c < x + width; c++){
                   mapRegions[r][c] = _currentRegion;
                   mapLayout[r][c] = room.getLayout()[row][col];
                   col++;
               }
               row++;
               col = 0;
           }
       }
       System.out.println("Rooms: " + _rooms.size());
   }
   
   private void mapMaze(int x, int y){
       List<Vector2> cells = new ArrayList<Vector2>();
       Vector2 lastDir = new Vector2(0,0);
       //System.out.println(x + " | " +  y);
       _currentRegion ++;
       mapLayout[x][y] = 0;
       mapRegions[x][y] = _currentRegion;
       
       int fillerTile = _room.getFillerObjectKey();
       
       cells.add(new Vector2(x,y));
       
       while(!cells.isEmpty()){
           //System.out.println("Cells: " + cells.size());
           Vector2 currentCell = cells.get(cells.size() - 1);
           
           List<Vector2> unMadeCells = new ArrayList<Vector2>();
           
           if((currentCell.x + 3 < mapWidth) && (mapLayout[(int)currentCell.x + 2][(int)currentCell.y] == fillerTile)){
               unMadeCells.add(new Vector2(1,0));
           }
           if((currentCell.x - 3 > 0) && (mapLayout[(int)currentCell.x - 2][(int)currentCell.y] == fillerTile)){
               unMadeCells.add(new Vector2(-1,0));
           }
           if((currentCell.y + 3 < mapHeight) && (mapLayout[(int)currentCell.x][(int)currentCell.y + 2] == fillerTile)){
               unMadeCells.add(new Vector2(0,1));
           }
           if((currentCell.y - 3 > 0) && (mapLayout[(int)currentCell.x][(int)currentCell.y - 2] == fillerTile)){
               unMadeCells.add(new Vector2(0,-1));
           }
          
           if(!unMadeCells.isEmpty()){
                Vector2 dir = new Vector2(0,0);
               if(unMadeCells.contains(lastDir) && MathUtils.random(100) > 0){
                   dir = lastDir;
               }
               else{
                   dir = unMadeCells.get(MathUtils.random(unMadeCells.size() - 1));
               }
               
               mapLayout[(int)(currentCell.x + dir.x)][(int)(currentCell.y + dir.y)] = 0;
               mapLayout[(int)(currentCell.x + dir.x * 2)][(int)(currentCell.y + dir.y * 2)] = 0;
               
               cells.add(new Vector2(currentCell.x + dir.x * 2, currentCell.y + dir.y * 2));
               lastDir = dir;
           }
           else{
               cells.remove(cells.size() - 1);
               
               lastDir = null;
           }
       }
   }
   
   private void connectRegions(){
       Map<String, Set<Integer>> connectorRegions = new HashMap();
       Map<String, Vector2> connectorRegionsPos = new HashMap();
       for(int r = 0; r < mapRegions.length; r++){
           for (int c = 0; c < mapRegions[r].length; c++) {
               if(mapLayout[r][c] == 00 || mapLayout[r][c] == _room.getFillerObjectKey()) continue;
               
               Set<Integer> regions = new HashSet<Integer>() ;
               
               for(int i = 0; i < Directions.size(); i++){
                   Vector2 region = new Vector2(r,c).add(Directions.get(i));
                   if(region.x < 0 || region.y < 0 || region.x >= mapWidth || region.y >= mapHeight) continue;
                   if(mapRegions[(int)region.x][(int)region.y] != null){
                       regions.add(mapRegions[(int)region.x][(int)region.y]);
                   }
               }
               
               if(regions.size() < 2) continue;
               
               connectorRegions.put(r + "," + c, regions);
               connectorRegionsPos.put(r + "," + c, new Vector2(r,c));
           }
        }
           
           Map<String,Integer> merged = new HashMap();
           Set<Integer> openRegions = new HashSet();
           for(int i = 0; i< _currentRegion; i ++){
               merged.put(Integer.toString(i),i);
               openRegions.add(i);
           }
           
           Set connectors = connectorRegions.keySet();
           System.out.println(_currentRegion);
           while(openRegions.size() > 1){
               
               System.out.println("KeySet Size: " + connectors.size());
               int rand = MathUtils.random(connectors.size() - 1);
               System.out.println(rand);
               
               Object obj = connectors.toArray()[rand];
               Object connector = connectorRegions.get(obj).toArray()[0];
               Vector2 pos = connectorRegionsPos.get(obj);
               
               mapLayout[(int)pos.x][(int)pos.y] = 0;
               
               
               List<Integer>regions = new ArrayList();
               for(Integer value : connectorRegions.get(obj)){
                    regions.add(merged.get(value));
               }
               Integer dest = regions.get(0);
               
               List<Integer> sources = regions.subList(1, regions.size() - 1);
               
               
               for(int i = 0; i < _currentRegion; i ++){
                   if(sources.contains(merged.get(i))){
                       merged.put(Integer.toString(i), dest);
                   }
               }
               openRegions.removeAll(sources);
               
               
               for(String key: connectorRegionsPos.keySet()){
                   Vector2 connPos = connectorRegionsPos.get(key);
                   if(pos.sub(connPos).isZero(2)){
                       connectors.remove(key);
                   }
                  // if()
               }
           }
       
       
   }
    
}
