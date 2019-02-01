/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;

/**
 *  Action Management for the 
 * Atkinson Game Engine
 * @author Douglas Atkinson
 */
public class Scene extends Actor {
    
    private ArrayList<SceneSegment> segmentList;
    private int index;
    
    
    /**
    * Constructor for Scene Object<br>
    *
    * creates new segment list<br>
    * sets index to -1
    * <p>
    */
    public Scene() {
        super();
        segmentList = new ArrayList<SceneSegment>();
        index = -1;
    }
    
    /**
    * Adds a segment to the list of segments to execute
    * <p>
    * @param segment the SceneSegment to add to the list
    */
    public void addSegment(SceneSegment segment) {
        segmentList.add(segment);
    }
    
    /**
    * Removes all segments from the list
    * <p>
    */
    public void clearSegments() {
        segmentList.clear();
    }
    /**
    * Starts the Scene at the first Segment
    * <p>
    */
    public void start() {
        index = 0;
        segmentList.get(index).start();
    }
    
    
    /**
    * Loads the next segment if the current one is finished and the current one is not the last segment
    * <p>
    * @param dt DeltaTime
    */
    public void act(float dt) {
        if ( isSegmentFinished() && !isLastSegment() )
            loadNextSegment();
    }
    /**
    * Returns Whether or not the current segment is finished
    * <p> 
    * @return true if the segment is finished, else false
    */
    public boolean isSegmentFinished() {
        return segmentList.get(index).isFinished();
    }
     /**
    * Returns Whether the current segment is the last in the list
    * <p>
    * @return true if on the last segment, else false
    */
    public boolean isLastSegment() {
        return (index >= segmentList.size() - 1);
    }
    
    
    /**
    * Loads and starts the next segment in line
    * <p>
    */
    public void loadNextSegment() {
        if ( isLastSegment() )
            return;
        segmentList.get(index).finish();
        index++;
        segmentList.get(index).start();
    }
    
    /**
    * Return whether or not the Scene is over
    * <p>
    * @return isLastSegment and isSegmentFinished
    */
    public boolean isSceneFinished() {
        return ( isLastSegment() && isSegmentFinished() );
    }
}
