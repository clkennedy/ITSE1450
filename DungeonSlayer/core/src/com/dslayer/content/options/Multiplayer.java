/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.dslayer.content.screens.MutliplayerLobbyScreen;
import com.dslayer.content.screens.multiplayerRoomScreen;
import io.socket.client.Socket;

/**
 *
 * @author ARustedKnight
 */
public class Multiplayer {
    public static Socket socket;
    public static boolean connected = true;
    public static boolean host = false;
    public static multiplayerRoomScreen baseScreen = new multiplayerRoomScreen();
    public static MutliplayerLobbyScreen lobbyScreen = new MutliplayerLobbyScreen();
}
