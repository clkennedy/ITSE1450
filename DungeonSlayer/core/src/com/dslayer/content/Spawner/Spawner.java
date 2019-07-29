/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Spawner;

import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Rooms.Room;

/**
 *
 * @author ARustedKnight
 */
public class Spawner {
    
    public static enum enemyTypes{Golem, Goblin, skeleton};
    public static enum Bosses{};
    
    private Spawner(){}
    
    public static BaseEnemy spawnRandomEnemy(){
        return null;
    }
    
    public static BaseEnemy spawnRandomEnemy(Room room){
        return null;
    }
    
}
