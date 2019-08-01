package com.dslayer.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dslayer.Launcher.DungeonSlayer;
import com.dslayer.content.Font.FontLoader;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Options;

public class DesktopLauncher {
	public static void main (String[] args)
    {
        Game myGame = new DungeonSlayer();
        
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

    cfg.title = "Ironside";
    cfg.width = (int)Difficulty.worldWidth;
    cfg.height = (int)Difficulty.worldHeight;
    
    Options.baseWidth = Difficulty.worldWidth;
    Options.baseHeight = Difficulty.worldHeight;
    
    //Options.displayType = Options.DisplayType.WINDOWED;
    cfg.resizable = false;
    //cfg.fullscreen = true;
    //cfg.vSyncEnabled = true;
    //Options.desktopWidth = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
    //Options.desktopheight = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        
        LwjglApplication launcher = new LwjglApplication( myGame, cfg);
    }
}
