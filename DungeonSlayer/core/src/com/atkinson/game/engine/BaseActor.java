/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.atkinson.game.engine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import java.util.ArrayList;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.options.Multiplayer;
import org.json.JSONObject;

/**
 *  The Base for the Game Objects/Actors in the <br>
 * Atkinson Game Engine
 * @author Douglas Atkinson
 */
public class BaseActor extends Group {
    
    protected int network_id;
    
    //debugging stuff (custom)
    protected static boolean debug = true;
    protected ShapeRenderer sRend;
    // Animation support
    protected Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;
    
    // Velocity support
    private Vector2 velocityVec;
    
    // Acceleration support
    private Vector2 accelerationVec;
    private float acceleration;
    private float maxSpeed;
    private float deceleration;
    
    // Collision support
    public Polygon boundaryPolygon;
    
    private static Stage mainStage = null;
    private static Stage uiStage = null;
    
    private static Rectangle worldBounds;
    
    protected boolean canMove = true;
    
    /**
    * Constructor of the Base actor
    * <br><br>
    * Default values:<br>
    * animation = null<br>
    * elapsedTime = 0<br>
    * animationPaused = false<br>
    * velocityVec = new Vector2(0, 0)<br>
    * accelerationVec = new Vector2(0, 0)<br>
    * acceleration = 0<br>        
    * maxSpeed = 1000<br>
    * deceleration = 0<br>
    * 
    * <p>
    * @param    x Spawn x location in pixels from the bottom left of the scene
    * @param    y Spawn y location in pixels from the bottom left of the scene
    * @param    s stage that the actor will attach to
    */
    public BaseActor(float x, float y, Stage s) {
        
        // Call constructor from Actor class
        super();
        
        // perform additional initialization task
        setPosition(x, y);
        
        s.addActor(this);
        
        animation = null;
        elapsedTime = 0;
        animationPaused = false;
        
        velocityVec = new Vector2(0, 0);
        
        accelerationVec = new Vector2(0, 0);
        acceleration = 0;        
        maxSpeed = 1000;
        deceleration = 0;
        
        sRend = new ShapeRenderer();
        
        
    }
    
    public  int getNetworkID(){
        return network_id;
    }
    
    public void networkSkillCast(BaseActor a, Vector2 target, Skill.From f){
        if(Multiplayer.socket == null || !Multiplayer.socket.connected()){
            return;
        }
        JSONObject data = new JSONObject();
        try{
            data.put("networkID", a.getNetworkID());
            data.put("targetX", target.x);
            data.put("targetY", target.y);
            data.put("from", f.ordinal());
            Multiplayer.socket.emit("gameObjectSkillCast", data);
        }catch(Exception e){
            
        }
        
    }
    
    public static void setMainStage(Stage s){
        BaseActor.mainStage = s;
    }
    public static void setUIStage(Stage s){
        BaseActor.uiStage = s;
    }
    
    public static Stage getUiStage(){
        return BaseActor.uiStage;
    }
    public static Stage getMainStage(){
        return BaseActor.mainStage;
    }
    
    public void setCanMove(boolean b){
        canMove = b;
        
    }
    
    public float getMaxSpeed(){
       return maxSpeed;
    }
    
    public BaseActor(){
        this(0,0,mainStage);
    }
    
    /**
    * Returns a List of BaseActors
    * 
    * <p>
    * Searches through all the actors of the provided stage
    * to return a list BaseActors based on the class provided
    * 
    * @param    stage Stage to look through of Actors
    * @param    className fully qualified string name of the class
    * @return   List of BaseActors
    */
    public static ArrayList<BaseActor> getList(Stage stage, String className) {
        ArrayList<BaseActor> list = new ArrayList<BaseActor>();
        Class theClass = null;
        try {  
            theClass = Class.forName(className); 
        }
        catch (Exception error) {  
            error.printStackTrace();
        }
        if(stage == null){
            stage = BaseActor.mainStage;
        }
        for (Actor a : stage.getActors()) {
            if ( theClass.isInstance( a ) ) {
                list.add( (BaseActor)a );
            }
        }
        return list;
    }
    
    /**
    * Static method that sets the world bounds for the stage using passed in Width and Height
    * 
    * <p>
    * @param    width Width in Pixels of how big the world is
    * @param    height Height in Pixels of how big the world is
    */
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }
    
    
    /**
    * Static methods that sets the world bounds for the stage using a BaseActor
    * 
    * <p>
    * calls setWorldBounds(float width, float height)
    * @param    ba BaseActor that is the base of the world
    * 
    */
    public static void setWorldBounds(BaseActor ba) {
        setWorldBounds(ba.getWidth(), ba.getHeight());
    }
    
    /**
    * Returns the the number of objects left in the stage based
    * on the fully qualified class name provided
    * 
    * <p>
    * calls getList(Stage stage, String className)
    * @param    stage Stage to look through of Actors
    * @param    className fully qualified string name of the class
    * @return the number of class objects left in the stage 
    */
    public static int count(Stage stage, String className) {
        return getList(stage, className).size();
    }
    
    /**
    * Sets the Animation for the Base Actor
    * 
    * <p>
    * @param    anim Animation to be used
    */
    public void setAnimation(Animation<TextureRegion> anim) {
        animation = anim;
        //elapsedTime = 0;
        if(animation != null){
            TextureRegion tr = animation.getKeyFrame(0);
            float w = tr.getRegionWidth();
            float h = tr.getRegionHeight();
            setSize(w, h);
            setOrigin(w/2, h/2);
            
            if(boundaryPolygon == null)
            setBoundaryRectangle();
        }
    }
     public void setAnimationWithReset(Animation<TextureRegion> anim) {
        animation = anim;
        elapsedTime = 0;
        if(animation != null){
            TextureRegion tr = animation.getKeyFrame(0);
            float w = tr.getRegionWidth();
            float h = tr.getRegionHeight();
            setSize(w, h);
            setOrigin(w/2, h/2);
            
            if(boundaryPolygon == null)
            setBoundaryRectangle();
        }
    }
    /**
    * Pauses and un-pauses the Animation
    * 
    * <p>
    * sets the animationPaused private variable
    * @param    pause true pauses, false un-pauses
    */
    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }
    
    @Override
    public void act(float dt) {
        super.act(dt);
        if(!animationPaused) {
            elapsedTime += dt;
        }
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
                
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if(animation != null && isVisible())
        {
            batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            if(debug){
               batch.end();
                sRend.setProjectionMatrix(this.getStage().getCamera().combined);
                sRend.setColor(Color.WHITE);
                sRend.begin(ShapeRenderer.ShapeType.Line);
                //this.getBoundaryPolygon();
                sRend.polygon(boundaryPolygon.getTransformedVertices());
                sRend.end();
                batch.begin();
            }
        }
        
        super.draw(batch, parentAlpha);
    }
    
    /**
    * Converts a batch of files to an animation that the base actor can use.
    * 
    * <p>
    * @param    fileNames array of file names that will be used in the animation
    * @param    frameDuration the duration of each frame
    * @param    loop sets whether the animation will loop or only play once
    * @return the Converted Animation
    */
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop) {
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        for(int n = 0; n < fileCount; n++) {
            String fileName = fileNames[n];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }
        
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        if(loop) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        else {
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        }
        if(animation == null) {
            setAnimation(anim);
        }
        
        return anim;
    }
    public Animation<TextureRegion> loadAnimationFromTexture(TextureRegion texture, float frameDuration, boolean loop) {
       
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        textureArray.add(texture);
        
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        if(loop) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        else {
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        }
        if(animation == null) {
            setAnimation(anim);
        }
        
        return anim;
    }
    /**
    * Converts a sprite sheet to an animation that the base actor can use.
    * 
    * <p>
    * automatic determines the height and width of each image 
    * based on rows to width and columns to height ratios
    * 
    * @param    fileName the name of the sprite sheet to be used
    * @param    rows number of rows in the sprite sheet
    * @param    cols number of columns in the sprite sheet
    * @param    frameDuration the duration of each frame
    * @param    loop sets whether the animation will loop or only play once
    * @return the Converted Animation
    */
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                textureArray.add(temp[r][c]);
            }
        }
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);        
        if(loop) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        else {
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        }
        if(animation == null) {
            setAnimation(anim);
        }
        
        return anim;        
    }
    
    /**
    * Loads a single texture to be used as the sprite in the BaseActor
    * 
    * <p>
    * Uses the the Animation but set only the first position
    * as only a single image will be used
    * 
    * @param    fileName the name of the sprite to be used
    * @return the Converted Animation
    */
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);
    }
    
     public Animation<TextureRegion> loadTexture(TextureRegion texture) {
        //String[] fileNames = new String[1];
        //fileNames[0] = texture;
        return loadAnimationFromTexture(texture, 1, true);
    }
    
    /**
    * Returns whether of not a non looping animation has finished playing
    * 
    * <p>
    * return false for looping animations
    * 
    * @return whether the animation has finished playing or not
    */
    public boolean isAnimationFinished() {
        if(animation.getPlayMode() == Animation.PlayMode.LOOP){
            return false;
        }
        return animation.isAnimationFinished(elapsedTime);
    }
    
    /**
    * sets the speed of the BaseActor
    * 
    * <p>
    * 
    * @param speed the new speed of the BaseActor
    */
    public void setSpeed(float speed) {
        // if length is zero, then assume motion angle is zero degrees
        if(velocityVec.len() == 0)
            velocityVec.set(speed, 0);
        else
            velocityVec.setLength(speed);
    }
    
    /**
    * gets the speed of the BaseActor
    * 
    * <p>
    * 
    * @return the Speed of the BaseActor
    */
    public float getSpeed() {
        return velocityVec.len();
    }
    
    /**
    * Sets the angle of the vector in degrees relative to the x-axis, towards the positive y-axis (typically counter-clockwise)
    * 
    * <p>
    * @param angle The angle in degrees to set.
    */
    public void setMotionAngle(float angle) {
        velocityVec.setAngle(angle);
    }
    
    /**
    * gets the angle of the vector in degrees relative to the x-axis, towards the positive y-axis (typically counter-clockwise)
    * 
    * <p>
    * @return the angle in degrees of this vector (point) relative to the x-axis. Angles are towards the positive y-axis (typically counter-clockwise) and between 0 and 360.
    */
    public float getMotionAngle() {
        return velocityVec.angle();
    }
    
    /**
    * gets Whether the BaseActor has a Speed greater than zero
    * 
    * <p>
    * @return Whether the BaseActor is moving
    */
    public boolean isMoving() {
        return (getSpeed() > 0);
    }
          
    /**
    * sets the Acceleration of the BaseActor
    * 
    * <p>
    * @param acc how fast the object can accelerate
    */
    public void setAcceleration(float acc) {
        acceleration = acc;
    }
    
    /**
    * accelerate at the given angle
    * 
    * <p>
    * @param angle angle to accelerate at
    */
    public void accelerateAtAngle(float angle) {
        accelerationVec.add(new Vector2(acceleration, 0).setAngle(angle));
    }
    
    /**
    * accelerate forward at the angle the BaseActor is pointing
    * 
    */
    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }
    
    /**
    * sets the Max Speed the BaseActor can move
    * 
    * <p>
    * @param ms max speed the BaseActor can move
    */
    public void setMaxSpeed(float ms) {
        maxSpeed = ms;
    }
    
    /**
    * sets how fast the BaseActor Decelerates
    * 
    * <p>
    * @param dec how fast Deceleration is
    */
    public void setDeceleration(float dec) {
        deceleration = dec;
    }
    
    /**
    * applies the physics of the BaseActor<br>
    * 
    * <p>
    * uses MaxSpeed, Speed, Acceleration, and Deceleration to move the BaseActor
    * 
    * @param dt change over time (delta Time)
    */
    public void applyPhysics(float dt) {
        
        // apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);
        float speed = getSpeed();
        
        // decrease speed (decelerate) when not accelerating
        if(accelerationVec.len() == 0)
            speed -= deceleration * dt;
        
        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);
        
        // update velocity
        setSpeed(speed);
        
        // apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt);
        
        // reset acceleration
        accelerationVec.set(0, 0);
    }
    
    /**
    * Creates a collision Polygon in the shape of a rectangle<br>
    * based on the width and height of the BaseActor
    * 
    * <p>
    * Used in Collision Detection
    */
    public void setBoundaryRectangle()
    {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundaryPolygon = new Polygon(vertices);
    }
    
     /**
    * Creates a collision Polygon with the specified number of sides<br>
    * based on the width and height of the BaseActor
    * 
    * <p>
    * Used in Collision Detection
    * @param numSides the number of sides the polygon will have
    */
    public void setBoundaryPolygon(int numSides) {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = new float[2*numSides];
        
        for(int i = 0; i < numSides; i++) {
            float angle = i * MathUtils.PI2 / numSides;
            // x-coordinate
            vertices[2*i] = w / 2 * MathUtils.cos(angle) + w / 2;
            // y-coordinate
            vertices[2*i+1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }
        boundaryPolygon = new Polygon(vertices);
    }
    
    public void setBoundaryPolygonLong(int numSides) {
        float w = getWidth() / 2;
        float h = getHeight();
        float[] vertices = new float[2*numSides];
        
        for(int i = 0; i < numSides; i++) {
            float angle = i * MathUtils.PI2 / numSides;
            // x-coordinate
            vertices[2*i] = w / 2 * MathUtils.cos(angle) + w / 2 + (getWidth()/4);
            // y-coordinate
            vertices[2*i+1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }
        boundaryPolygon = new Polygon(vertices);
    }
    public void setBoundaryPolygonWide(int numSides) {
        float w = getWidth();
        float h = getHeight() / 2;
        float[] vertices = new float[2*numSides];
        
        for(int i = 0; i < numSides; i++) {
            float angle = i * MathUtils.PI2 / numSides;
            // x-coordinate
            vertices[2*i] = w / 2 * MathUtils.cos(angle) + w / 2;
            // y-coordinate
            vertices[2*i+1] = h / 2 * MathUtils.sin(angle) + h / 2 + (getHeight()/4);
        }
        boundaryPolygon = new Polygon(vertices);
    }
    public void setBoundaryPolygonHalf(int numSides) {
        float w = getWidth() / 2;
        float h = getHeight()/ 2;
        float[] vertices = new float[2*numSides];
        
        for(int i = 0; i < numSides; i++) {
            float angle = i * MathUtils.PI2 / numSides;
            // x-coordinate
            vertices[2*i] = w / 2 * MathUtils.cos(angle) + w / 2 + (getWidth()/4);
            // y-coordinate
            vertices[2*i+1] = h / 2 * MathUtils.sin(angle) + h / 2+ (getHeight()/4);
        }
        boundaryPolygon = new Polygon(vertices);
    }
    
    public void setBoundaryPolygonHalfLong(int numSides) {
        float w = getWidth() / 4;
        float h = getHeight()/ 2;
        float[] vertices = new float[2*numSides];
        
        for(int i = 0; i < numSides; i++) {
            float angle = i * MathUtils.PI2 / numSides;
            // x-coordinate
            vertices[2*i] = w / 4 * MathUtils.cos(angle) + w / 4 + (getWidth()/2);
            // y-coordinate
            vertices[2*i+1] = h / 2 * MathUtils.sin(angle) + h / 2+ (getHeight()/4);
        }
        boundaryPolygon = new Polygon(vertices);
    }
    
    /**
    * Returns the collision Polygon<br>
    * 
    * updates the position, origin, rotation, and scale of the polygon<br>
    * based of the values of the BaseActor
    * 
    * <p>
    * Used in Collision Detection
    * @return the Collision Polygon of the BaseActor
    */
    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(), getScaleY());
        return boundaryPolygon;
    }
    
    /**
    * Returns Whether the BAseActor is overlapping with the passed in BaseActor<br>
    * 
    * <p>
    * Used in Collision Detection
    * @param other the BaseActor to test Collision with
    * @return Whether this BaseActor is overlapping with the passed in BaseActor
    */
    public boolean overlaps(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
        
        // initial test to improve performance
        if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;
        
        return Intersector.overlapConvexPolygons(poly1, poly2);
    }
    
    /**
    * Centers the BaseActor at float x, float y
    * 
    * <p>
    * @param x the x coordinate to center at 
    * @param y the y coordinate to center at 
    */
    public void centerAtPosition(float x, float y) {
        getBoundaryPolygon();
        setPosition(x - getWidth()/2, y - getHeight()/2);
    }
    
    /**
    * Centers the BaseActor at the Position of another Actor
    * 
    * <p>
    * calls centerAtPosition(float x, float y)
    * @param other the BaseActor to center this Actor at
    */
    public void centerAtActor(BaseActor other) {
        getBoundaryPolygon();
        centerAtPosition(other.getX() + other.getWidth()/2, other.getY() + other.getHeight()/2);
    }
    
    /**
    * changes the Opacity of the BaseActor
    * 
    * <p>
    * Opacity is a float value from 0 - 1;
    * @param opacity opacity to set the BaseActor at
    */
    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }
    
    /**
    * Prevents this BaseActor from overlapping with another
    * 
    * <p>
    * @param other BAseActor to prevent overlap with
    * @return the normalized MinimumTranslationVector
    */
    public Vector2 preventOverlap(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
        
        // initial test to improve performance
        if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return null;
        
        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        if(!polygonOverlap)
            return null;
        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        return mtv.normal;
    }
    
    /**
    * Prevents this BaseActor from exiting the World Bounds
    * 
    */
    public void boundToWorld()
    {
        // check left edge
        if(getX() < 0)
            setX(0);
        // check right edge
        if(getX() + getWidth() > worldBounds.width)
            setX(worldBounds.width - getWidth());
        // check bottom edge
        if(getY() < 0)
            setY(0);
        // check top edge
        if(getY() + getHeight() > worldBounds.height)
            setY(worldBounds.height - getHeight());
    }
        
    /**
    * Aligns and clamps the Camera to World Bounds
    * 
    */
    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();
        // center camera on actor
        cam.position.set( this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0 );
        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x,
            cam.viewportWidth/2,  worldBounds.width -  cam.viewportWidth/2);
        cam.position.y = MathUtils.clamp(cam.position.y,
            cam.viewportHeight/2, worldBounds.height - cam.viewportHeight/2);
        cam.update();
    }
    
    
    /**
    * Allows the Actor to treat the world as if it were round.
    * 
    */
    public void wrapAroundWorld() {
        if(getX() + getWidth() < 0)
            setX(worldBounds.width);
        if(getX() > worldBounds.width)
            setX(-getWidth());
        if(getY() + getHeight() < 0)
            setY(worldBounds.height);
        if(getY() > worldBounds.height)
            setY(-getHeight());
    }
    
    /**
    * Return whether this BaseActor is within a certain distance of another
    * <p>
    * @param distance the distance to test of the objects
    * @param other the other Actor to calculate distance from
    * @return returns true if Actors are within the distance
    */
    public boolean isWithinDistance(float distance, BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        float scaleX = (this.getWidth() + 2 * distance) / this.getWidth();
        float scaleY = (this.getHeight() + 2 * distance) / this.getHeight();
        poly1.setScale(scaleX, scaleY);
        Polygon poly2 = other.getBoundaryPolygon();
        // initial test to improve performance
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return false;
        return Intersector.overlapConvexPolygons( poly1, poly2 );
    }
    
    
    /**
    * Returns a Rectangle that has the World Bounds
    * <p>
    * @return returns a Rectangle that is has the World Bounds
    */
    public static Rectangle getWorldBounds() {
       return worldBounds;
    }

}
