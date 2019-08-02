/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.atkinson.game.engine.BaseActor;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Inventory.Items.Potions.HealthPotion;
import com.dslayer.content.screens.LevelScreen;
import com.dslayer.content.screens.MutliplayerLobbyScreen;
import com.dslayer.content.screens.multiplayerRoomScreen;
import io.socket.client.Socket;

/**
 *
 * @author ARustedKnight
 */
public class Multiplayer {
    
    private static int networkid= 0;
    
    public static enum GameObjects{HealthPotion, SkeletonWarrior, SkeletonMage, BlueGolem};
    public static enum GameModeTypes{Survival, Crawl};
    public static GameModeTypes GameModeType = GameModeTypes.Survival;
    public static Socket socket;
    public static boolean connected = true;
    public static String myID;
    public static String myUserName = "";
    public static boolean host = false;
    
    public static multiplayerRoomScreen baseScreen;
    public static MutliplayerLobbyScreen lobbyScreen;
    public static LevelScreen levelScreen;
    
    public static BaseActor getNewGameObject(GameObjects obj, float x, float y){
        switch (obj) {
            case HealthPotion:
                return new HealthPotion(x, y, BaseActor.getMainStage());
            case SkeletonWarrior:
                return new SkeletonWarrior(x, y, BaseActor.getMainStage());
            case SkeletonMage:
                return new SkeletonMage(x, y, BaseActor.getMainStage());
            case BlueGolem:
                return new BlueGolem(x, y, BaseActor.getMainStage());
            default:
        }
        return null;
    }
    
    public static int getNextID(){
        return networkid ++;
    }
    public static void restartNetworkid(){
        networkid = 0;
    }
}
