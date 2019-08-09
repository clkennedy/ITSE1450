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
import com.dslayer.content.Rooms.RoomDoor;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.options.Options;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

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
    protected int maximumRoomSize = 12;
    
    protected int mapWidth;
    protected int mapHeight;
    protected float mapWidthPixels;
    protected float mapHeightPixels;
    protected int bossRoomRegion = -1;
    
    protected Vector2 spawnPos;
    
    Room _room;
    
    
   public LevelGenerator(int width, int height){
       _rooms = new ArrayList();
       
       mapLayout = new Integer[width][height];
       mapRegions = new Integer[width][height];
       mapWidth = width;
       mapHeight = height;
       mapWidthPixels = mapWidth * RoomPanels.defaultSize * Options.aspectRatio;
       mapHeightPixels = mapHeight * RoomPanels.defaultSize * Options.aspectRatio;
       
       RoomDoor.lg = this;
   }
   
   public float getPixelWidth(){
       return mapWidthPixels;
   }
   public float getPixelHeight(){
       return mapHeightPixels;
   }
   
   public Room getRandomNonBossRoom(){
       int rand = MathUtils.random(_rooms.size() - 1);
       while(_rooms.get(rand).isBossRoom()){
           rand = MathUtils.random(_rooms.size() - 1);
       }
       return _rooms.get(rand);
   }
   
   public List<Room> getNonBossRooms(){
       List<Room> tempRooms = new ArrayList();
       
       for(Room r: _rooms){
           if(!r.isBossRoom()){
               tempRooms.add(r);
           }
       }
       
       return tempRooms;
   }
   public Room getBossRooms(){
       Room tempRooms = null;
       
       for(Room r: _rooms){
           if(r.isBossRoom()){
               tempRooms= r;
               break;
           }
       }
       
       return tempRooms;
   }
   
   public void setDefaultSize(float size){
       Room.setDefaultSize(size);
       mapWidthPixels = mapWidth * RoomPanels.defaultSize;
       mapHeightPixels = mapHeight * RoomPanels.defaultSize;
   }
   public float getDefaultSize(){
       return Room.getDefaultSize();
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
   
   public void clearTiles(){
       ArrayList<BaseActor> allRoomObjects = BaseActor.getList(BaseActor.getMainStage(), "com.dslayer.content.Rooms.RoomPanels");
       for(int i = 0; i < allRoomObjects.size(); i ++){
           allRoomObjects.get(i).remove();
       }
   }
   
   public void draw(Stage stage){
       clearTiles();
       BaseActor temp = new RoomPanels();
        for(int i = 0; i < this.mapLayout.length; i++){
            //System.out.print("[");
            for(int j = 0; j < this.mapLayout[i].length; j++){
                //System.out.print(((mapRegions[i][j] == null)? "-" : mapRegions[i][j]) + " | ");
                temp = _room.Map(this.mapLayout[i][j]);
                if(temp == null)
                    continue;
                temp.setPosition(j * temp.getWidth(),mapHeightPixels - temp.getHeight() - (i * temp.getHeight()));
                temp.getBoundaryPolygon();
                temp.setZIndex(0);
                stage.addActor(temp);
            }
            //System.out.println("]");
        }
   }
   
   public Integer[][] getMapLayout(){
       return mapLayout;
   }
   
   public void setMapLayout(Integer[][] mapL){
       for(int i = 0; i < mapL.length; i++){
           for(int j = 0; j < mapL[i].length; j++){
               mapLayout[i][j] = mapL[i][j];
           }
       }
   }
   
   public void generateMap(){
       mapRooms();
       //System.out.println("Current Region: " + _currentRegion);
       for(int y = 1; y < mapHeight; y += 2 ){
           for(int x = 1; x < mapWidth; x += 2 ){
               if(mapLayout[x][y] != _room.getFillerObjectKey() || mapRegions[x][y] != null){
                   continue;
               }
               //System.out.println(x + "|" + y);
               mapMaze(x, y);
           }
       }
       //System.out.println("Current Region: " + _currentRegion);
       connectRegions();
       
   }
   
   public String getFloorTexture(){
       return _room.getFloorTexture();
   }
   
   private void mapRooms(){
       for(int i = 0; i < 200; i++){
           int width = MathUtils.random(minimumRoomSize, maximumRoomSize);
           int height = MathUtils.random(minimumRoomSize, maximumRoomSize);
           
           if(width % 2 == 0) width += 1;
           if(height % 2 == 0) height += 1;
           
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
           room.setRoomRegion(_currentRegion);
           for(int r = y; r < y + height; r ++){
               for(int c = x; c < x + width; c++){
                   mapRegions[r][c] = _currentRegion;
                   if(room.getLayout()[row][col] != 0){
                       mapLayout[r][c] = room.getLayout()[row][col];
                   }else{
                       mapLayout[r][c] = room.getLayout()[row][col];
                   }
                   col++;
               }
               row++;
               col = 0;
           }
       }
       
       int rand = MathUtils.random(_rooms.size() - 1);
       _rooms.get(rand).setBossRoom(true);
       bossRoomRegion = _rooms.get(rand).getRoomRegion();
       //System.out.println("Generated Rooms: " + _rooms.size());
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
           
           if((currentCell.x + 3 < mapWidth) && (mapLayout[(int)currentCell.x + 2][(int)currentCell.y] == fillerTile)
                   && (mapRegions[(int)currentCell.x + 2][(int)currentCell.y] == null)){
               unMadeCells.add(new Vector2(1,0));
           }
           if((currentCell.x - 3 > 0) && (mapLayout[(int)currentCell.x - 2][(int)currentCell.y] == fillerTile)
                   && (mapRegions[(int)currentCell.x - 2][(int)currentCell.y] == null)){
               unMadeCells.add(new Vector2(-1,0));
           }
           if((currentCell.y + 3 < mapHeight) && (mapLayout[(int)currentCell.x][(int)currentCell.y + 2] == fillerTile)
                   && (mapRegions[(int)currentCell.x][(int)currentCell.y + 2] == null)){
               unMadeCells.add(new Vector2(0,1));
           }
           if((currentCell.y - 3 > 0) && (mapLayout[(int)currentCell.x][(int)currentCell.y - 2] == fillerTile)
                   && (mapRegions[(int)currentCell.x][(int)currentCell.y - 2] == null)){
               unMadeCells.add(new Vector2(0,-1));
           }
          
           if(!unMadeCells.isEmpty()){
                Vector2 dir = new Vector2(0,0);
               if(unMadeCells.contains(lastDir) && MathUtils.randomBoolean(.5f)){
                   dir = lastDir;
               }
               else{
                   dir = unMadeCells.get(MathUtils.random(unMadeCells.size() - 1));
               }
               
               mapLayout[(int)(currentCell.x + dir.x)][(int)(currentCell.y + dir.y)] = 0;
               mapLayout[(int)(currentCell.x + dir.x * 2)][(int)(currentCell.y + dir.y * 2)] = 0;
               mapRegions[(int)(currentCell.x + dir.x)][(int)(currentCell.y + dir.y)] = _currentRegion;
               mapRegions[(int)(currentCell.x + dir.x * 2)][(int)(currentCell.y + dir.y * 2)] = _currentRegion;
               
               cells.add(new Vector2(currentCell.x + dir.x * 2, currentCell.y + dir.y * 2));
               lastDir = dir;
           }
           else{
               cells.remove(cells.size() - 1);
               
               lastDir = null;
           }
       }
   }
    
    public Vector2 getSpawnPosition(){
        return spawnPos;
    }
   
   private void drawMapRegions(){
       for(int i = 0; i < this.mapLayout.length; i++){
            System.out.print("[");
            for(int j = 0; j < this.mapLayout[i].length; j++){
                System.out.print(((mapRegions[i][j] == null)? "-" : mapRegions[i][j]) + " | ");
            }
            System.out.println("]");
        }
   }
   private void drawMapLayout(){
       for(int i = 0; i < this.mapLayout.length; i++){
            System.out.print("[");
            for(int j = 0; j < this.mapLayout[i].length; j++){
                System.out.print(((mapLayout[i][j] == null)? "-" : String.format("%02d",mapLayout[i][j])) + " | ");
            }
            System.out.println("]");
        }
   }
   
   private boolean isCornerOfRoom(Vector2 pos){
        for(Room r : _rooms){
            if(r.isCorner(pos)){
                return true; 
            }
        }
        return false;
   }
   
   private boolean checkIfRegionIsRoom(float x, float y){
       return checkIfRegionIsRoom(new Vector2(x,y));
   }
   
   private boolean checkIfRegionIsRoom(Vector2 pos){
       for(Room r: getNonBossRooms() ){
           if((mapRegions[(int)pos.x][(int)pos.y]) == r.getRoomRegion()){
               return true;
           }
       }
       return false;
   }
   
   private void connectRegions(){
       Map<String, Set<Integer>> connectorRegions = new HashMap();
       Map<String, Vector2> connectorRegionsPos = new HashMap();
       Map<String, Set<Vector2>> connectorRegionsDir = new HashMap();
       
       drawMapRegions();
       System.out.println("Boss Room Region: " + getBossRooms().getRoomRegion());
       for(int r = 1; r < mapRegions.length; r++){
           for (int c = 1; c < mapRegions[r].length; c++) {
               if(mapLayout[r][c] == 0 ) continue;
               
               Set<Integer> regions = new HashSet<Integer>();
               Set<Vector2> dir = new HashSet<Vector2>();
               boolean regionAdded = false;
               if(mapRegions[(int)r][(int)c] != null){
                       regions.add(mapRegions[(int)r][(int)c]);
                       regionAdded = true;
                }
               int curRegion = (mapRegions[r][c] == null) ? -1 : mapRegions[r][c];
               for(int i = 0; i < Directions.size(); i++){
                   regionAdded = false;
                   Vector2 region = new Vector2(r,c).add(Directions.get(i));
                   if(region.x < 0 || region.y < 0 || region.x >= mapWidth || region.y >= mapHeight){
                       continue;
                   }
                   if(mapRegions[(int)region.x][(int)region.y] != null){
                       regions.add(mapRegions[(int)region.x][(int)region.y]);
                       regionAdded = true;
                   }else{
                        region = new Vector2(r,c).add(Directions.get(i)).add(Directions.get(i));
                        if(region.x < 0 || region.y < 0 || region.x >= mapWidth || region.y >= mapHeight){
                            continue;
                        }
                        if(mapRegions[(int)region.x][(int)region.y] != null){
                            regions.add(mapRegions[(int)region.x][(int)region.y]);
                            regionAdded = true;
                        }
                   }
                   if(regionAdded && mapRegions[(int)region.x][(int)region.y] != curRegion){
                       dir.add( Directions.get(i));
                      // break;
                   }
               }
               
               if(regions.size() < 2) continue;
               
               connectorRegions.put(r + "," + c, regions);
               connectorRegionsPos.put(r + "," + c, new Vector2(r,c));
               connectorRegionsDir.put(r + "," + c, dir);
           }
        }
           
           List<Integer> merged = new ArrayList();
           Set<Integer> openRegions = new HashSet();
           for(int i = 0; i<= _currentRegion; i ++){
               merged.add(i);
               openRegions.add(i);
           }
           //drawMapLayout();
           Set connectors = connectorRegions.keySet();
           //System.out.println("Current Region is : " + _currentRegion);
           while(openRegions.size() > 0){
               
               //System.out.println("KeySet Size: " + connectors.size());
               //System.out.println("openRegions Size: " + openRegions.size());
               boolean validPos = false;
               Vector2 pos = null;
               Object obj = null;
               while(pos == null || isCornerOfRoom(pos)){
                    int rand = MathUtils.random(connectors.size() - 1);
                    //System.out.println("Selected Index: "+rand);
               
                    obj = connectors.toArray()[rand];
               
                    Object connector = connectorRegions.get(obj).toArray()[0];
               
                    pos = connectorRegionsPos.get(obj);
               }
               
               Vector2 tmpPos = new Vector2(pos.x, pos.y);
               //Vector2 dir = connectorRegionsDir.get(obj);
               
               //System.out.println("Position: "+tmpPos);
               mapLayout[(int)tmpPos.x][(int)tmpPos.y] = 0;
               
                //mapLayout[(int)tmpPos.x][(int)tmpPos.y] = 0;
                Set<Vector2> directions = connectorRegionsDir.get(obj);
                //System.out.println("Starting Pos: " + pos);
                
                for(Vector2 curDir: directions){
                    Vector2 curPos = new Vector2(tmpPos.x, tmpPos.y);
                    System.out.println("Starting Pos: " + curPos);
                    System.out.println("Current Direction: " + curDir);
                    int region = (mapRegions[(int)(curPos.x + curDir.x)][(int)(curPos.y+curDir.y)] == null) ? 
                            ((mapRegions[(int)(curPos.x + (curDir.x * 2))][(int)(curPos.y+(curDir.y * 2))] == null)? -1:
                            mapRegions[(int)(curPos.x + (curDir.x * 2))][(int)(curPos.y+(curDir.y * 2))]):
                            mapRegions[(int)(curPos.x + curDir.x)][(int)(curPos.y+curDir.y)];
                    System.out.println("Region: " + region);
                    boolean foundFloor = false;
                    while(!foundFloor){
                        if((curPos.x + curDir.x >= mapWidth || curPos.x + curDir.x < 0 ||
                                    curPos.y + curDir.y >= mapHeight || curPos.y + curDir.y < 0)
                                || mapLayout[(int)(curPos.x + curDir.x)][(int)(curPos.y+curDir.y)] == 0){
                            break;
                        }
                        System.out.println("Current Pos: " + curPos);
                        if(mapLayout[(int)(curPos.x + curDir.x)][(int)(curPos.y+curDir.y)] != 0
                            && (curPos.x + curDir.x < mapWidth && curPos.x + curDir.x > 0 &&
                                curPos.y + curDir.y < mapHeight && curPos.y + curDir.y > 0)){
                        mapLayout[(int)(curPos.x + curDir.x)][(int)(curPos.y+curDir.y)] = 0;
                        
                        if((mapLayout[(int)(curPos.x + curDir.y)][(int)(curPos.y+curDir.x)] == 0 
                            && mapRegions[(int)(curPos.x + curDir.y)][(int)(curPos.y+curDir.x)] != null
                            && mapRegions[(int)(curPos.x + curDir.y)][(int)(curPos.y+curDir.x)] == region) ||
                                (mapLayout[(int)(curPos.x - curDir.y)][(int)(curPos.y-curDir.x)] == 0 
                                && mapRegions[(int)(curPos.x - curDir.y)][(int)(curPos.y-curDir.x)] != null
                                && mapRegions[(int)(curPos.x - curDir.y)][(int)(curPos.y-curDir.x)] == region)){
                            foundFloor = true;
                            break;
                        }
                        } 
                        curPos.add(curDir);
                    }
                }
               
               List<Integer>regions = new ArrayList();
               System.out.println("Regions");
               for(Integer value : connectorRegions.get(obj)){
                   System.out.println(value);
                   regions.add(merged.get(value));
               }
               System.out.println("__________");
               /*System.out.println("Current Regions");
               for(Integer i : regions){
                   System.out.println(i);
               }*/
               
               Integer dest = regions.get(0);
               
               List<Integer> sources = regions.subList(1, regions.size());
               
               
               for(Integer i = 0; i <= _currentRegion; i ++){
                   if(sources.contains(merged.get(i))){
                       merged.set(i, dest);
                   }
               }
               
               Iterator t = openRegions.iterator();
               
               /*System.out.println("Current open Regions");
               while(t.hasNext()){
                   Object i = t.next();
                   System.out.println(i);
               }
               System.out.println("Current Sources");
               for(Integer i : sources){
                   System.out.println(i);
               }
               
               for(Integer i : sources){
                   openRegions.remove(i);
               }*/
               Set<Integer> posRegions = connectorRegions.get(obj);
               boolean containsBossRoom = false;
               for(Integer i : posRegions){
                   containsBossRoom = (posRegions.size() == 2) && (containsBossRoom || (i == bossRoomRegion));
               }
               if(!containsBossRoom){
                    for(Integer i : posRegions){
                        openRegions.remove(i);
                    }
               }else{
                   openRegions.remove(bossRoomRegion);
               }
               
               
               List<String> keysToRemove = new ArrayList<String>();
               for(String key : connectorRegions.keySet()){
                   if(connectorRegions.get(key).equals(posRegions)){
                       if(MathUtils.randomBoolean(.8f)){
                           keysToRemove.add(key);
                       }
                   }
               }
               
               
               for(String key: connectorRegionsPos.keySet()){
                   Vector2 connPos = connectorRegionsPos.get(key);
                   tmpPos = new Vector2(pos.x, pos.y);
                   if(tmpPos.sub(connPos).isZero(2)){
                       connectors.remove(key);
                   }
                  // if()
               }
               for(String key : keysToRemove){
                    connectors.remove(key);
               }
               
           }
           
           /*for(int i = 0; i <= _currentRegion; i ++){
               System.out.println(i+": "+merged.get(i));
           }*/
           for(Room r : _rooms){
               if(r.isBossRoom()){
                   /*System.out.println("Room X: " + r.getRoomX());
                   System.out.println("Room Y: " + r.getRoomY());
                   System.out.println("Room Width: " + r.getRoomWidth());
                   System.out.println("Room Height: " + r.getRoomHeight());
                   System.out.println("Room Region: " + r.getRoomRegion());*/
                   
                   int actualW = (int)r.getRoomWidth() - 1;
                   int actualH = (int) r.getRoomHeight() - 1;
                   int roomX = (int) r.getRoomX();
                   int roomY = (int) r.getRoomY();
                   
                   for(int row = (int)r.getRoomY(); row <= (int)r.getRoomY() + actualH; row++){
                       for(int col = (int)r.getRoomX(); col <=  (int)r.getRoomX() + actualW; col++){
                           if(col != r.getRoomX() && row != r.getRoomY()) break;
                        try{   
                           if( mapLayout[row][col] == 0 && mapRegions[row][col] == r.getRoomRegion()){
                               mapLayout[row][col] = r.getDoor();
                           }
                           if(row == r.getRoomY() && mapLayout[row+ actualH][col] == 0 && mapRegions[row+ actualH][col] != null && mapRegions[row+ actualH][col] == r.getRoomRegion()){
                               mapLayout[row + actualH][col] = r.getDoor();
                           }
                           if(col == r.getRoomX() && mapLayout[row][col + actualW] == 0 && mapRegions[row][col + actualW] != null && mapRegions[row][col + actualW] == r.getRoomRegion()){
                               mapLayout[row][col + actualW] = r.getDoor();
                           }
                       }catch(Exception e){
                            System.out.println("Line number: " + e.getStackTrace()[0].getLineNumber() + " | " + row + " " + actualH + " | " + col + " " + actualW);
                        }
                       }
                   }
                   
                   break;
               }
           }
           
            boolean foundSpawn = false;
            //Top-Bottom
            if(MathUtils.randomBoolean(.5f)){
                while(!foundSpawn){
                    int pos = (int)MathUtils.random(1 ,mapWidth - 1);
                    if(MathUtils.randomBoolean(.5f)){//Top
                        if(mapLayout[1][pos] == 0 && mapRegions[1][pos] != bossRoomRegion && !checkIfRegionIsRoom(1, pos)){
                            mapLayout[0][pos] = 0;
                            spawnPos = new Vector2((pos + .5f) * RoomPanels.defaultSize, (mapHeightPixels) - (RoomPanels.defaultSize/2) );
                            foundSpawn = true;
                            System.out.println("0 : " + pos );
                        }
                    }else{//Bottom
                        if(mapLayout[mapHeight - 2][pos] == 0 && mapRegions[mapHeight - 2][pos] != bossRoomRegion && !checkIfRegionIsRoom(mapHeight - 2, pos)){
                            mapLayout[mapHeight - 1][pos] = 0;
                            spawnPos = new Vector2((pos + .5f) * RoomPanels.defaultSize, (mapHeightPixels) - ((mapHeight - .5f) * RoomPanels.defaultSize));
                            foundSpawn = true;
                            System.out.println((mapHeight - 1) + " : " + pos );
                        }
                    }
                }
               
            }else{//Left -Right
                while(!foundSpawn){
                    int pos = (int)MathUtils.random(1 ,mapHeight - 1);
                    if(MathUtils.randomBoolean(.5f)){//Left
                        if(mapLayout[pos][1] == 0 && mapRegions[pos][1] != bossRoomRegion && !checkIfRegionIsRoom(pos, 1)){
                            mapLayout[pos][0] = 0;
                            spawnPos = new Vector2((RoomPanels.defaultSize / 2), (mapHeightPixels) - ((pos + .5f) * (RoomPanels.defaultSize)));
                            foundSpawn = true;
                            System.out.println(pos + " : 0" );
                        }
                    }else{//Right
                            if(mapLayout[pos][mapWidth - 2] == 0 && mapRegions[pos][mapWidth - 2] != bossRoomRegion && !checkIfRegionIsRoom(pos, mapWidth - 2)){
                            mapLayout[pos][mapWidth - 1] = 0;
                            spawnPos = new Vector2(((mapWidth - .5f) * RoomPanels.defaultSize), (mapHeightPixels) - ((pos + .5f) * RoomPanels.defaultSize));
                            foundSpawn = true;
                            System.out.println(pos + " : " + (mapWidth - 1));
                        }
                    }
                }
            }
            //drawMapLayout();
   }

    private Stage getStage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
