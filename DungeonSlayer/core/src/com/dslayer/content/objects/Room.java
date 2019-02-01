/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.objects;

/**
 *
 * @author cameron.kennedy
 */
public abstract class Room {
    
    private Room _room;
    private int[][] _layout;
    
    public abstract Room generateRoom();
    
    
    public Room getRoom(){
        return _room;
    }
    public int[][] getLayout(){
        return _layout;
    }    
}
