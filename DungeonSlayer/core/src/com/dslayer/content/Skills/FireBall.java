/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.projectiles.Spells.Projectiles;

/**
 *
 * @author ARustedKnight
 */
public class FireBall extends Skill{
    
    private String ico = "Icons/FireBall.png";
    private String icoCD = "Icons/FireBallCooldown.png";
    
    public FireBall(){
        super();
        setup();
    }
    
    public FireBall(float x, float y, Stage s){
        super(x,y,s);
        setup();
    }
    
    private void setup(){
        /*setAnimation(Projectiles.getFireBallAnim());
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygonHalfWidth(12);
        setRotation(90);
        damage = 50;*/
    }
    @Override
    public void setupIcon(float x, float y){
        super.setupIcon(x, y);
        loadCastTexture();
    }
    public void act(float dt){
        super.act(dt);
        /*getBoundaryPolygon();
        accelerateAtAngle(direction);
        applyPhysics(dt);
        
        if(from == Skill.From.Enemy){
            for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                if(overlaps(player)){
                    ((Player)player).takeDamage((int)damage);
                    //remove();
                }
            }
        }
        if(from == Skill.From.Player){
            for(BaseActor enemy: BaseActor.getList(this.getStage(), "com.dslayer.content.Enemy.BaseEnemy")){
                if(overlaps(enemy)){
                    ((BaseEnemy)enemy).takeDamage((int)damage);
                    //remove();
                }
            }
        }
        
        for(BaseActor wall: BaseActor.getList(this.getStage(), "com.dslayer.content.Rooms.DungeonPanels")){
            if(wall.boundaryPolygon == null)
                continue;
            if(overlaps(wall)){
                //remove();
            }
        }*/
     }

    @Override
    public void cast(Vector2 caster, Vector2 target, ProjectileSpell.From from) {
        float degrees = (float)(MathUtils.atan2((target.y - caster.y ), target.x - caster.x) * 180.0d / Math.PI);
                BaseActor b = new ProjectileSpell(caster.x ,caster.y , BaseActor.getMainStage()).fireBall()
                        .setProjectileSpeed(300).
                        setProjectileRotation(degrees).
                        setDirection(degrees)
                        .setFrom(from);
                canCast = false;
    }
    
    @Override
    protected void loadCDTexture() {
        //baIcon.loadTexture(icoCD);
        baIcon.setAnimation(Avatars.load(icoCD));
        baIcon.setSize(iconSize, iconSize);
    }
    @Override
    protected void loadCastTexture() {
        //baIcon.loadTexture(ico);
        baIcon.setAnimation(Avatars.load(ico));
        baIcon.setSize(iconSize, iconSize);
    }
}
