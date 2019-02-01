/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 *
 * @author ARustedKnight
 */
public class Progress {
    
    private static Preferences prefs;
    
    public static void Save(){
        prefs = Gdx.app.getPreferences("SavedData");
        prefs.putInteger("HighScore", Unlocks.getHighScore());
        prefs.putInteger("selectedAvatar", Unlocks.getCurrentAvatarIndex());
        prefs.putInteger("selectedEnemy", Unlocks.getCurrentEnemyIndex());
        prefs.putInteger("selectedCollectable", Unlocks.getCurrentCollectableIndex());
        prefs.putInteger("selectedBullet", Unlocks.getCurrentBulletIndex());
        
        prefs.putFloat("musicVol", (Options.musicVolume * 10));
        prefs.putFloat("soundVol", (Options.soundVolume * 10));
        
        prefs.putInteger("Display", Options.displayType.ordinal());
        
        prefs.flush();
    }
    
    public static void Load(){
        prefs = Gdx.app.getPreferences("SavedData");
        int i = prefs.getInteger("HighScore", Unlocks.getHighScore());
        Unlocks.Unlock(i);
        Unlocks.setSelects(prefs.getInteger("selectedAvatar", Unlocks.getCurrentAvatarIndex()),
                prefs.getInteger("selectedEnemy", Unlocks.getCurrentEnemyIndex()),
                prefs.getInteger("selectedCollectable", Unlocks.getCurrentCollectableIndex()),
                prefs.getInteger("selectedBullet", Unlocks.getCurrentBulletIndex()));
        
        Options.musicVolume = prefs.getFloat("musicVol", 10) / 10;
        Options.soundVolume = prefs.getFloat("soundVol", 10) / 10;
        
        Options.displayType = Options.DisplayType.values()[prefs.getInteger("Display", 0)];
        
        Options.setDisplay();
        
    }
}
