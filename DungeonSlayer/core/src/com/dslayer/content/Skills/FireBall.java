/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.Dungeon.DungeonObject;
import com.dslayer.content.Rooms.RoomFloor;
import com.dslayer.content.Rooms.RoomObject;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Options;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.projectiles.Spells.Projectiles;
import static com.dslayer.content.projectiles.Spells.Projectiles.FireBall;

/**
 *
 * @author ARustedKnight
 */
public class FireBall extends Skill{
    
    private String ico = "Icons/FireBall.png";
    private String icoCD = "Icons/FireBallCooldown.png";
    
    private String soundPath = "";
    
    private Sound sound;
    
    public FireBall(){
        super();
        setup();
        
    }
    
    public FireBall(float x, float y, Stage s){
        super(x,y,s);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setup();
    }
    
    private void setup(){
        /*setAnimation(Projectiles.getFireBallAnim());
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygonHalfWidth(12);
        setRotation(90);
        damage = 50;*/
         skillHit = Gdx.audio.newSound(Gdx.files.internal("Sounds/BasicDamage.mp3"));
    }
    @Override
    public void setupIcon(float x, float y){
        super.setupIcon(x, y);
        loadCastTexture();
    }
    public void act(float dt){
        
        super.act(dt);
        if(!isAction)
            return;
        if(!isCast && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
            Sound s = Gdx.audio.newSound(Gdx.files.internal("Sounds/FireBallCast.mp3"));
            s.play(Options.soundVolume);
           
            isCast = true;
        }
        getBoundaryPolygon();
        accelerateAtAngle(direction);
        applyPhysics(dt);
        
        if(from == Skill.From.Enemy){
            for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                if(overlaps(player)){
                    ((Player)player).takeDamage((int)damage);
                    if(skillHit != null && BaseActor.getMainStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                        skillHit.play(Options.soundVolume);
                    }
                    remove();
                }
            }
        }
        if(from == Skill.From.Player){
            for(BaseActor enemy: BaseActor.getList(this.getStage(), "com.dslayer.content.Enemy.BaseEnemy")){
                if(overlaps(enemy)){
                    ((BaseEnemy)enemy).takeDamage((int)damage, player);
                    //if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                        //skillHit.play(Options.soundVolume);
                    //}
                    remove();
                }
            }
        }
        
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.RoomPanels")){
            if(wall.boundaryPolygon == null  || (wall instanceof RoomObject)|| (wall instanceof RoomFloor))
                continue;
            if(overlaps(wall)){
                remove();
            }
        }
     }

    @Override
    public BaseActor cast(BaseActor caster, Vector2 target, Skill.From from) {
        float degrees = (float)(MathUtils.atan2((target.y - (caster.getY() + caster.getHeight()) )
                , target.x - (caster.getX() + caster.getWidth())) * 180.0d / Math.PI);
        
        BaseActor b = new FireBall(caster.getX() + caster.getWidth() / 2,caster.getY() + caster.getHeight() / 2 , 
                BaseActor.getMainStage()).isProjectile()
                .setProjectileAcceleration(300 * Options.aspectRatio).
                setProjectileRotation(degrees).
                setDirection(degrees)
                .setFrom(from);
                canCast = false;
        if(from == Skill.From.Player){
            ((Skill)b).player = ((Player)caster);
            
        }  
        return b;
    }
    
    public FireBall isProjectile(){
        isAction = true;
        loadAnimationFromSheet(FireBall, 8, 8, .02f, true);
        setScale(1f * Options.aspectRatio);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setPosition(getX() - (getWidth() /2) , getY() - (getHeight() / 2));
        setBoundaryPolygon(12);
        setRotation(90);
        damage = 50;
        return this;
    }
    
    @Override
    protected void loadCDTexture() {
        //baIcon.loadTexture(icoCD);
        baIcon.loadTexture(icoCD);
        baIcon.setSize(iconSize* Options.aspectRatio, iconSize* Options.aspectRatio);
    }
    @Override
    protected void loadCastTexture() {
        //baIcon.loadTexture(ico);
        baIcon.loadTexture(ico);
        baIcon.setSize(iconSize* Options.aspectRatio, iconSize* Options.aspectRatio);
    }

    @Override
    public BaseActor cast(BaseActor arg0, Vector2 target,float degrees, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BaseActor cast(BaseActor arg0, BaseActor arg1, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
