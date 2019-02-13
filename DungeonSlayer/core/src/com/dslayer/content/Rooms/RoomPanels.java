/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Rooms;

import com.atkinson.game.engine.BaseActor;
import com.dslayer.content.options.Options;

/**
 *
 * @author cameron.kennedy
 */
public class RoomPanels extends BaseActor{
    public static final float defaultSize = 50;
    
    
    protected static BaseActor DefaultSize(BaseActor t){
        t.setSize(defaultSize * Options.aspectRatio, defaultSize*Options.aspectRatio);
        return t;
    }
}
