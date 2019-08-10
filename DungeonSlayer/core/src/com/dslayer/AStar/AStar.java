/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.AStar;

/**
 *
 * @author ARustedKnight
 */
//http://www.codebytes.in/2015/02/a-shortest-path-finding-algorithm.html
import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.Vector2;
import java.util.*;

public class AStar {
    public static final int DIAGONAL_COST = 99;
    public static final int V_H_COST = 10;
    
    static class Cell{  
        int heuristicCost = 0; //Heuristic cost
        int finalCost = 0; //G+H
        int i, j;
        Cell parent; 
        
        Cell(int i, int j){
            this.i = i;
            this.j = j; 
        }
        
        @Override
        public String toString(){
            return "["+this.i+", "+this.j+"]";
        }
    }
    
    //Blocked cells are just null Cell values in grid
    static Cell [][] grid = new Cell[5][5];
    
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;
            
    public static void setBlocked(int i, int j){
        grid[i][j] = null;
    }
    
    public static void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }
    
    public static void setEndCell(int i, int j){
        endI = i;
        endJ = j; 
    }
    
    static void checkAndUpdateCost(Cell current, Cell t, int cost){
        if(t == null || closed[t.i][t.j])return;
        int t_final_cost = t.heuristicCost+cost;
        
        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    
    public static void AStar(){ 
        
        //add the start location to open list.
        if(grid[startI][startJ] == null){
            int u = 0;
        }
        open.add(grid[startI][startJ]);
        
        Cell current;
        
        while(true){ 
            current = open.poll();
            if(current==null)break;
            closed[current.i][current.j]=true; 

            if(current.equals(grid[endI][endJ])){
                return; 
            } 

            Cell t;  
            if(current.i-1>=0){
                t = grid[current.i-1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

                if(current.j-1>=0){                      
                    t = grid[current.i-1][current.j-1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
                }

                if(current.j+1<grid[0].length){
                    t = grid[current.i-1][current.j+1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
                }
            } 

            if(current.j-1>=0){
                t = grid[current.i][current.j-1];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
            }

            if(current.j+1<grid[0].length){
                t = grid[current.i][current.j+1];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 
            }

            if(current.i+1<grid.length){
                t = grid[current.i+1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+V_H_COST); 

                if(current.j-1>=0){
                    t = grid[current.i+1][current.j-1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
                }
                
                if(current.j+1<grid[0].length){
                   t = grid[current.i+1][current.j+1];
                    checkAndUpdateCost(current, t, current.finalCost+DIAGONAL_COST); 
                }  
            }
        } 
    }
    
    public static List<Vector2> getPath(Vector2 from, Vector2 to, Integer[][] map){
        return getPath((int)from.x, (int)from.y, (int)to.x, (int)to.y, map);
    }
    
    public static List<Vector2> getPath(int si, int sj, int ei, int ej, Integer[][] map){
            //Reset
           grid = new Cell[map.length][map[0].length];
           closed = new boolean[map.length][map[0].length];
           open = new PriorityQueue<>((Object o1, Object o2) -> {
                Cell c1 = (Cell)o1;
                Cell c2 = (Cell)o2;

                return c1.finalCost<c2.finalCost?-1:
                        c1.finalCost>c2.finalCost?1:0;
            });
           //Set start position
            setStartCell(si, sj);  //Setting to 0,0 by default. Will be useful for the UI part
           
           //Set End Location
            setEndCell(ei, ej); 
            if(BaseActor.debug){
                System.out.println(new Vector2(si, sj));
                System.out.println(new Vector2(ei, ej));
            }
           
           for(int i=0;i<map.length;++i){
              for(int j=0;j<map[0].length;++j){
                  grid[i][j] = new Cell(i, j);
                  grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
//                  System.out.print(grid[i][j].heuristicCost+" ");
              }
//              System.out.println();
           }
           grid[si][sj].finalCost = 0;
           
           /*
             Set blocked cells. Simply set the cell values to null
             for blocked cells.
           */
           for(int row=0;row<map.length;row ++){
               for(int col = 0; col < map[0].length; col++){
                   if(map[row][col] != 0){
                       setBlocked(row, col);
                   }
               }
           }
           
            AStar(); 
            if(BaseActor.debug){
                System.out.println("\nScores for cells: ");
                for(int i=0;i<map.length;i++){
                    for(int j=0;j<map[0].length;j++){
                        if(grid[i][j]!=null)System.out.printf("%-5d ", grid[i][j].finalCost);
                        else System.out.printf("%-5s", "BL");
                    }
                    System.out.println();
                }
                System.out.println();
            
            }
           List<Vector2> moveTos = new ArrayList();
           if(closed[endI][endJ]){
               //Trace back the path 
                //System.out.println("Path: ");
                Cell current = grid[endI][endJ];
                moveTos.add(new Vector2(current.j, current.i));
                if(BaseActor.debug){
                    System.out.print(current);
                }
                while(current.parent!=null){
                    if(BaseActor.debug){
                        System.out.print(" -> "+current.parent);
                    }
                    current = current.parent;
                    if(current.parent != null){
                        moveTos.add(0, new Vector2(current.j, current.i));
                    }
                } 
               // System.out.println();
           }//else System.out.println("No possible path");
           return moveTos;
    }

}

